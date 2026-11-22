# Terraform compute

The root configuration uses the reusable `modules/compute` module to create
one AWS EC2 instance in an existing subnet. Supply a valid AMI and at least
one security group from the same VPC:

```sh
terraform init
terraform validate
terraform plan \
  -var='ami_id=ami-0123456789abcdef0' \
  -var='subnet_id=subnet-0123456789abcdef0' \
  -var='vpc_security_group_ids=["sg-0123456789abcdef0"]'
```

Set `aws_region`, `instance_name`, and `instance_type` as needed. The module
does not create a VPC, subnet, or security group; review the supplied network
and security group rules before applying. Credentials should be provided using
the AWS provider's standard environment or profile configuration, never stored
in Terraform variables or committed to the repository.
