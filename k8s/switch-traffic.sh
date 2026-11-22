#!/bin/sh
set -eu

usage() {
    echo "Usage: $0 <blue|green> [namespace]" >&2
    exit 2
}

[ "$#" -ge 1 ] && [ "$#" -le 2 ] || usage

color=$1
namespace=${2:-task-manager}

case "$color" in
    blue|green) ;;
    *) usage ;;
esac

deployment="task-manager-$color"

kubectl rollout status "deployment/$deployment" \
    --namespace "$namespace" \
    --timeout=180s

ready_replicas=$(kubectl get "deployment/$deployment" \
    --namespace "$namespace" \
    -o jsonpath='{.status.readyReplicas}')
if [ "${ready_replicas:-0}" -lt 1 ]; then
    echo "Deployment '$deployment' has no ready replicas; traffic was not changed." >&2
    exit 1
fi

kubectl patch service task-manager \
    --namespace "$namespace" \
    --type merge \
    --patch "{\"spec\":{\"selector\":{\"app\":\"task-manager\",\"color\":\"$color\"}}}"

echo "Service traffic now selects color '$color' in namespace '$namespace'."
