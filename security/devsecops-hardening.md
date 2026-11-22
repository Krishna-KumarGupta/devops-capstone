# DevSecOps hardening

## SonarQube analysis

The repository-root `security/sonar-project.properties` configures the
Task Manager Java sources and tests. Run tests/package first so the compiled
class paths exist, then invoke the scanner from the repository root:

```sh
cd app
./mvnw clean verify
cd ..
sonar-scanner \
  -Dsonar.host.url="$SONAR_HOST_URL" \
  -Dsonar.token="$SONAR_TOKEN"
```

Provide `SONAR_HOST_URL` and `SONAR_TOKEN` through the CI secret store or
environment, never as committed command-line values. In Jenkins, bind a
SonarQube token credential only for the analysis step and use the Jenkins
SonarQube integration/webhook to enforce the quality gate. Treat scanner
failures and quality-gate failures as failed pipeline checks; do not silently
skip them.

## Secret management

- Never commit passwords, tokens, private keys, registry credentials, or
  production connection strings to Git, Terraform variable files, container
  images, build arguments, or pipeline logs.
- Use a managed secret store (for example, a cloud secret manager or Vault)
  and a Kubernetes integration such as External Secrets Operator or a
  secrets-store CSI driver. Limit read access to the workload namespace and
  identity that needs each secret.
- Kubernetes Secret objects are base64 encoded, not encrypted by default.
  Enable API-server encryption at rest, restrict `get/list/watch` permissions,
  and prefer externally managed secrets. The checked-in `k8s/base/secret.yaml`
  contains placeholder values only; replace this pattern with an external
  secret reference before production use.
- Mark sensitive Terraform inputs as `sensitive`, but remember Terraform state
  can still contain secrets. Store state remotely with encryption, locking,
  and least-privilege access; do not commit state or secret-bearing plan files.
- Rotate credentials, scope tokens by purpose and lifetime, and redact them
  from diagnostic output. Do not enable shell tracing while credentials are
  in scope.

## Container hardening

The Task Manager runtime image uses a slim Temurin JRE and runs as the
unprivileged `app` user. Preserve the multi-stage build so Maven and source
files are not included in the runtime image. For production:

- Pin base images by digest and keep Java, OS packages, and application
  dependencies current; scan images with Trivy and fail on agreed
  high/critical findings.
- Set Kubernetes `securityContext` values such as `runAsNonRoot: true`,
  `allowPrivilegeEscalation: false`, `readOnlyRootFilesystem: true`, and
  `capabilities.drop: ["ALL"]`. Use a writable `emptyDir` only for paths
  demonstrably required at runtime.
- Apply CPU/memory requests and limits, health probes, and a restrictive
  seccomp profile. Do not mount the Docker socket or host filesystem into the
  application container.
- Keep actuator endpoints limited to the required health and metrics paths;
  expose them only to trusted monitoring networks and avoid adding sensitive
  data to metric labels or application logs.

## Kubernetes RBAC

- The application ServiceAccount has token automount disabled and does not
  need Kubernetes API permissions. Keep its Role empty unless a documented
  runtime feature requires narrowly scoped access.
- Use a separate deployment identity for Jenkins/Argo CD. Grant only the
  resource verbs and namespaces required for deployment, and avoid cluster
  administrator bindings. Review `kubectl auth can-i` for that identity.
- Restrict network access to the API, database, registry, and monitoring
  endpoints. Keep namespaces, image pull secrets, and service accounts
  environment-specific.
- Review rendered Kustomize output before applying changes, especially when
  automated Argo CD pruning is enabled.

## Jenkins credential handling

- Store registry, SonarQube, and cluster credentials in Jenkins Credentials
  (or an external credentials provider); refer to credentials by ID and bind
  them only around the steps that need them.
- Use short-lived, least-privilege registry and cluster tokens. Separate
  build/test permissions from deployment permissions and production approvals.
- Disable shell tracing around secret-bearing commands, pass passwords via
  standard input where supported, and never interpolate secrets into command
  strings, artifact names, or build logs.
- Restrict who can configure jobs, edit pipeline code, access agents, and
  retrieve credentials. Use isolated ephemeral agents for untrusted pull
  requests and do not expose production credentials to them.
- Ensure cleanup/logout runs in `post`/`finally` paths and that credentials
  are not persisted in Docker layers, workspaces, caches, or build artifacts.
