# ENSP461 Task Manager Demo Guide (10 minutes)

> **Rubric note:** No ENSP461 evaluation rubric was included in this repository.
> The mapping below uses common evaluation areas as placeholders, not official
> rubric wording. Before presenting, rename/map each row to the instructor's
> exact criteria and weights.

## Before the presentation

- [ ] Confirm the demo cluster/context, namespace, registry image, and active
  blue/green color; do not assume the checked-in placeholders are live values.
- [ ] Confirm the application and PostgreSQL are ready:
  `kubectl get deployments,pods,services,pvc -n task-manager`.
- [ ] Confirm API access (port-forward or approved endpoint) and prepare a
  harmless sample task.
- [ ] Open the Jenkins build, Grafana dashboard, Prometheus target status, and
  Fluentd/Elasticsearch log view in advance.
- [ ] Have local artifact checks ready if cloud/cluster infrastructure is not
  available: Maven tests, Docker build, Kustomize render, Terraform validate,
  and Ansible syntax check.
- [ ] Hide credentials, tokens, private IPs, customer data, and unrelated
  browser tabs. Never expose secret values in screenshots or terminal history.

## Timed walkthrough

| Time | Demo action and talking points | Suggested evaluation mapping |
|---|---|---|
| 0:00–0:45 | Introduce the problem, objectives, and end-to-end architecture. Point to Developer → GitHub → Jenkins → image → Kubernetes, with Ansible/Terraform, Argo CD, security, monitoring, and logging. | Problem definition; architecture |
| 0:45–2:00 | Show the API and send one request: create or list a task at `/api/tasks`. Mention validation/error handling and that `/api/auth/login` is a placeholder, not production authentication. | Application functionality; API design |
| 2:00–3:15 | Open the Jenkins run. Show checkout, Maven Test, Maven Package, Docker Build, Trivy, and GitOps/deploy stages. Call out the versioned image and scan result. | CI/CD automation; testing; security |
| 3:15–5:00 | Show Kubernetes deployments, pods, and Service selector. Explain inactive-color rollout and readiness check; if approved, switch traffic with `./k8s/switch-traffic.sh green` (or `blue`) and confirm the selector. | Containerization; deployment strategy; reliability |
| 5:00–6:15 | Show Prometheus target health and Grafana CPU, memory, HTTP, and JVM panels. Explain which metrics indicate application health. | Monitoring; operational readiness |
| 6:15–7:00 | Show one Fluentd-forwarded application/container log in Elasticsearch or stdout. Explain source metadata and why logs complement metrics. | Logging; troubleshooting |
| 7:00–8:15 | Show Terraform module inputs/outputs and validation, then Ansible inventory/playbooks. State that Terraform creates an EC2 instance in existing networking and Ansible installs/configures hosts; neither provisions a complete managed cluster. | Infrastructure as Code; configuration automation |
| 8:15–9:15 | Show Argo CD Application source path and automated prune/self-heal configuration. Show SonarQube properties and discuss secret, RBAC, and non-root container controls. | GitOps; security; maintainability |
| 9:15–10:00 | Summarize verified outcomes, known limitations, and next steps; invite questions. | Results; reflection; communication |

## Evidence checklist mapped to likely criteria

Use the official rubric's wording and weights in place of the generic labels.

### Application implementation and functionality

- [ ] Demonstrate a real task CRUD request and response.
- [ ] Show validation/error behavior or the corresponding test.
- [ ] Clearly state the login endpoint's current limitation.

### Testing and CI/CD

- [ ] Show Maven tests passing, not only the pipeline definition.
- [ ] Show package and image build stages.
- [ ] Show Trivy result and explain the configured severity gate.

### Deployment and operations

- [ ] Show ready Kubernetes pods and the active Service selector.
- [ ] Explain deploying to the inactive color before switching.
- [ ] If switching live, verify rollout/readiness before and after the switch;
  keep the prior color available for rollback.

### Infrastructure automation and GitOps

- [ ] Show Terraform validate and describe required existing AWS networking.
- [ ] Show Ansible syntax check and explain the inventory is environment
  specific.
- [ ] Show Argo CD source/revision/path and sync policy; distinguish configured
  manifests from a live synced application if it is not deployed.

### Security and observability

- [ ] Show scan/quality evidence without showing credentials.
- [ ] Explain secret storage, least privilege, non-root runtime, and RBAC
  boundaries.
- [ ] Show application metrics and at least one collected log.

### Communication and evidence

- [ ] Keep to the 10-minute schedule and use the architecture diagram.
- [ ] Use screenshots from the actual run; label placeholders/configuration
  separately from deployed results.
- [ ] Finish with limitations and prioritized future improvements.

## Fallback commands

If live infrastructure is unavailable, state that explicitly and demonstrate
local or static validation instead. From the repository root:

```sh
cd app && ./mvnw test
cd ..
docker build -f docker/Dockerfile -t task-manager:demo .
kubectl kustomize k8s/overlays/blue
kubectl kustomize k8s/overlays/green
terraform -chdir=terraform validate
ansible-playbook -i ansible/inventory.ini ansible/playbooks/site.yml --syntax-check
```

Do not run a traffic switch or apply infrastructure during the presentation
unless the target environment is approved and the active/inactive colors,
credentials, and rollback path have been verified.
