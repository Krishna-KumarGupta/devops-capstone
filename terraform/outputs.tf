output "instance_id" {
  description = "ID of the Task Manager compute instance."
  value       = module.compute.instance_id
}

output "instance_private_ip" {
  description = "Private IP address of the compute instance."
  value       = module.compute.private_ip
}

output "instance_public_ip" {
  description = "Public IP address of the compute instance, if assigned."
  value       = module.compute.public_ip
}

output "instance_name" {
  description = "Name tag of the compute instance."
  value       = module.compute.name
}
