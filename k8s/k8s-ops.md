# Kubernetes operations

These manifests deploy the Task Manager API and a single PostgreSQL instance.
The PostgreSQL data directory is backed by the `postgres-data` PVC. The
included Secret contains placeholder credentials: replace them before
deployment, and do not commit real credentials. Kubernetes Secrets are
base64-encoded, not encrypted by default; enable encryption at rest or use an
external/sealed secret manager for production.

## Initial install

Replace the placeholder database credentials in `k8s/base/secret.yaml` and the
placeholder image in `k8s/base/deployment.yaml` before bootstrapping the
namespace, database, initial blue deployment, and Service. For a private image
registry, create the `registry-credentials` image pull Secret in the namespace
after creating the namespace and before applying the rest of the base:

```sh
kubectl apply -f k8s/base/namespace.yaml
# Create registry-credentials with your registry's credentials if the image is private.
kubectl apply -k k8s/base
kubectl rollout status deployment/postgres -n task-manager --timeout=180s
kubectl rollout status deployment/task-manager-blue -n task-manager --timeout=180s
kubectl get pods,services,pvc -n task-manager
```

Port-forward the ClusterIP Service to access the API locally:

```sh
kubectl port-forward service/task-manager 8080:80 -n task-manager
curl http://localhost:8080/actuator/health
```

## Blue-Green release and traffic switch

Deploy the inactive color first. Overlay applies intentionally do not modify
the application Service, so applying green leaves blue serving traffic:

```sh
kubectl apply -k k8s/overlays/green
kubectl set image deployment/task-manager-green \
  task-manager=ghcr.io/your-org/task-manager:<tag> -n task-manager
kubectl rollout status deployment/task-manager-green -n task-manager
./k8s/switch-traffic.sh green
```

Switching back to blue uses the same script:

```sh
./k8s/switch-traffic.sh blue
```

The script waits for a successful rollout and at least one ready replica before
changing the Service selector. Keep the previous color available until the new
color has been verified.

## Scale

Scale the active color (or both colors if the next release needs pre-warmed
capacity):

```sh
kubectl scale deployment/task-manager-blue --replicas=3 -n task-manager
kubectl scale deployment/task-manager-green --replicas=3 -n task-manager
kubectl get deployments -n task-manager
```

## Rolling update

Update the image on the chosen deployment. Kubernetes rolls its pods gradually
according to the Deployment strategy and readiness probe:

```sh
kubectl set image deployment/task-manager-blue \
  task-manager=ghcr.io/your-org/task-manager:<tag> -n task-manager
kubectl rollout status deployment/task-manager-blue -n task-manager
```

For a Blue-Green release, prefer updating the inactive color and switching
traffic only after it becomes ready.

## Rollback

Roll back a failed Deployment rollout, then inspect its status:

```sh
kubectl rollout undo deployment/task-manager-green -n task-manager
kubectl rollout status deployment/task-manager-green -n task-manager
kubectl describe deployment/task-manager-green -n task-manager
```

If traffic has already moved to the new color, route it back to the known-good
color:

```sh
./k8s/switch-traffic.sh blue
```

## Jenkins prerequisites

Configure the Jenkins agent with Java 25, Docker access, `kubectl` configured
for the target cluster, and Kustomize support through `kubectl`. Add a Jenkins
username/password credential with ID `container-registry`, matching the
`REGISTRY_HOST` and repository in `IMAGE_REPOSITORY`. The registry image must
be pullable by the cluster; the pipeline refreshes the namespace's
`registry-credentials` Secret during deploy. Bootstrap `k8s/base` and replace
the database placeholder credentials before running the pipeline. Choose the
inactive `DEPLOY_COLOR`; enable `SWITCH_TRAFFIC` only when the new color should
receive traffic. The overlays intentionally omit the database Secret and application Service
from their rendered output so updates preserve credentials and the current
traffic selector.
