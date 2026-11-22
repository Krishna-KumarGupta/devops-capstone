# Task Manager DevOps Technical Report

**Course:** ENSP461  
**Project:** Task Manager REST API and DevOps automation  
**Author(s):** `[Insert name(s)]`  
**Date:** `[Insert submission date]`

## 1. Problem Statement

Delivering a REST application reliably involves more than implementing API
endpoints. The project needs a repeatable build and test workflow, portable
container packaging, controlled deployment and traffic switching, operational
visibility, and documented infrastructure/security practices. Without these,
releases can be inconsistent, difficult to recover, and hard to diagnose.

This project addresses those needs with a Java 25 Spring Boot Task Manager API
and a set of CI/CD, Kubernetes, GitOps, infrastructure automation, monitoring,
logging, and security artifacts.

## 2. Objectives

- Implement task CRUD and a basic login request endpoint with validation and
  consistent API error responses.
- Use Maven, JUnit 5, and environment-configurable H2/PostgreSQL data sources.
- Build a small runtime image and provide a local PostgreSQL Compose option.
- Define Jenkins build, test, scan, and deployment stages.
- Provide Kubernetes base resources and blue-green deployment/traffic-switch
  manifests and procedures.
- Automate host configuration and reusable compute provisioning.
- Provide metrics, dashboard, and log-forwarding configurations.
- Document secure handling of credentials, images, permissions, and analysis.

## 3. Architecture

The application is a Spring Boot service backed by H2 by default or PostgreSQL
when configured. Jenkins checks out the source, tests and packages it, builds
and scans a container image, then deploys to the inactive Kubernetes color.
Traffic can be switched after readiness checks. Argo CD separately watches the
Kubernetes manifests for GitOps reconciliation. Prometheus/Grafana provide
metrics visibility; Fluentd forwards logs to Elasticsearch and stdout.
Terraform provides a reusable AWS EC2 compute module, while Ansible configures
Linux hosts and installs Docker/Kubernetes packages.

See the [architecture diagram](architecture.md) for Mermaid and ASCII
representations and deployment boundaries.

## 4. Tools Used

| Tool | Use |
|---|---|
| Java 25, Spring Boot, Maven | REST API, dependency management, packaging |
| JUnit 5, Spring MVC Test, Mockito | Service and controller tests |
| H2, PostgreSQL | Default in-memory and optional persistent relational databases |
| Docker, Docker Compose | Multi-stage container build and local app/database composition |
| Jenkins, Trivy | CI/CD stages and image vulnerability scanning |
| Kubernetes, Kustomize, Argo CD | Container orchestration, blue-green overlays, GitOps |
| Ansible | Host configuration and package installation |
| Terraform, AWS provider | Reusable EC2 compute provisioning |
| Prometheus, Grafana | Metrics collection and dashboarding |
| Fluentd, Elasticsearch | Log collection, forwarding, and search target |
| SonarQube Scanner | Static analysis configuration |

## 5. Screenshots

Replace each placeholder with a legible screenshot captured from the actual
environment. Redact credentials, tokens, private addresses, and other sensitive
information. Do not present configuration examples as evidence of a live
deployment.

### Phase 1 — API and application

**Caption:** *Figure 1. Task Manager REST API running and returning a task response.*

`[Screenshot placeholder: Show the application running and a successful GET or POST request to /api/tasks, with credentials and sensitive data redacted.]`

### Phase 2 — Tests and Maven build

**Caption:** *Figure 2. Maven test stage completing successfully for the Task Manager application.*

`[Screenshot placeholder: Show the ./mvnw test result with the executed test count and zero failures.]`

### Phase 3 — Container build and security scan

**Caption:** *Figure 3. Multi-stage container image build and Trivy vulnerability scan.*

`[Screenshot placeholder: Show the Docker build and Trivy scan summary for the versioned image; include scan status and redact registry credentials.]`

### Phase 4 — Kubernetes blue-green deployment

**Caption:** *Figure 4. Blue and green deployments ready with the Service selecting the active color.*

`[Screenshot placeholder: Show kubectl get deployments,pods,service -n task-manager and the active Service color selector after a successful rollout.]`

### Phase 5 — Monitoring

**Caption:** *Figure 5. Prometheus scrape health and Grafana CPU, memory, HTTP, and JVM panels.*

`[Screenshot placeholder: Show the Task Manager Grafana dashboard with recent application and infrastructure metrics.]`

### Phase 6 — Logging and search

**Caption:** *Figure 6. Fluentd-collected Task Manager logs available in Elasticsearch or Fluentd stdout.*

`[Screenshot placeholder: Show a representative application/container log event and its source metadata; redact private data.]`

### Phase 7 — Infrastructure automation and GitOps

**Caption:** *Figure 7. Terraform validation, Ansible playbook syntax check, and Argo CD application sync status.*

`[Screenshot placeholder: Show successful validation/syntax output and, if deployed, the Argo CD application sync/health status.]`

### Phase 8 — DevSecOps controls

**Caption:** *Figure 8. SonarQube quality analysis and CI credential configuration without exposing secret values.*

`[Screenshot placeholder: Show the quality-gate result and Jenkins credential IDs/configuration with all secret values hidden.]`

## 6. Results

The repository includes the requested application, deployment, automation, and
observability artifacts. In the implemented API, task endpoints support
create/read/update/delete operations; login is a validated placeholder and
does not authenticate users or issue tokens.

Validation completed during implementation included:

- Java 25 Maven test run: 14 tests passed with zero failures or errors.
- Docker image build and runtime smoke tests for the task API, actuator health,
  and Prometheus endpoint.
- Kustomize rendering/schema validation for the base and blue/green overlays.
- Terraform initialization and `terraform validate` for the compute module.
- Ansible syntax checks for the master playbook and individual playbooks.
- Prometheus configuration validation, Grafana JSON/panel checks, and Fluentd
  configuration syntax parsing.

These are configuration/build checks, not evidence that a production cluster,
cloud instance, Jenkins server, Argo CD controller, Elasticsearch cluster, or
SonarQube server has been provisioned. The target environment still needs
credentials, real repository/registry values, host inventory, monitoring
endpoints, and service access configured.

## 7. Challenges

- Coordinating the image tag, cluster image-pull credentials, and deployment
  selector across CI and blue-green Kubernetes releases.
- Preserving the active Service selector and database credentials while
  preparing the inactive deployment.
- Providing cloud-neutral examples while keeping Terraform and Ansible
  configurations usable and explicit about their AWS and Debian/Ubuntu
  assumptions.
- Avoiding committed secrets while still documenting end-to-end deployment
  commands.
- Aligning static monitoring/logging target names and mounts to actual cluster
  service discovery and node log paths.

## 8. Future Scope

- Replace placeholder repository, image, inventory, and credential values with
  environment-specific configuration and managed secret references.
- Provision a complete production network and managed Kubernetes cluster,
  rather than only a compute instance.
- Add database backups/migrations, authentication and authorization, API rate
  limits, and production-grade login/token handling.
- Add network policies, stricter pod security contexts, TLS, and secret
  encryption at rest.
- Add quality-gate enforcement, software bill of materials generation, signed
  images, deployment approvals, and automated rollback criteria.
- Use Prometheus service discovery and deploy exporters/Fluentd as managed
  cluster components with persistent, access-controlled storage.
- Replace these generic evaluation mappings and screenshot placeholders with
  instructor-approved rubric wording and evidence.
