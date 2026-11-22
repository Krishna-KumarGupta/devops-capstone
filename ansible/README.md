# Ansible host automation

The playbooks target Debian- and Ubuntu-family hosts. Edit `inventory.ini` with
the Docker hosts and Kubernetes control-plane/worker hosts, and configure SSH
access and sudo privileges before running them. Keep private keys and other
credentials outside this repository.

Run commands from this directory so `ansible.cfg` picks up the inventory:

```sh
ansible-playbook playbooks/site.yml --syntax-check
ansible-playbook playbooks/site.yml
```

The master playbook configures host networking and swap, installs Docker on
`docker_hosts`, installs containerd and Kubernetes packages on the
`kubernetes` group, then applies the Task Manager base manifests from the
controller. Install `kubectl` on the controller and configure `KUBECONFIG` for
the target cluster before running the deployment play.

The Kubernetes playbook installs the v1.35 package stream. To upgrade, change
`kubernetes_minor_version` deliberately and plan a cluster upgrade with
`kubeadm`; package installation alone does not initialize or join cluster
nodes.
