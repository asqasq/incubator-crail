#!/bin/bash


kubectl delete -f ./crail-namenode.yaml
#kubectl delete -f ./crail-datanode-dram.yaml
#kubectl delete -f ./controller/crail-controller.yaml
kubectl delete secret kubeconfig-secret -n crail
kubectl delete namespace crail

