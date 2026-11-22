variable "aws_region" {
  description = "AWS region in which to create the compute instance."
  type        = string
  default     = "us-east-1"
}

variable "instance_name" {
  description = "Name tag for the Task Manager compute instance."
  type        = string
  default     = "task-manager"
}

variable "ami_id" {
  description = "AMI ID to use for the instance."
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type."
  type        = string
  default     = "t3.micro"
}

variable "subnet_id" {
  description = "Subnet ID in which to launch the instance."
  type        = string
}

variable "vpc_security_group_ids" {
  description = "Security group IDs to attach to the instance."
  type        = list(string)
}

variable "key_name" {
  description = "Optional EC2 key pair name."
  type        = string
  default     = null
}

variable "user_data" {
  description = "Optional cloud-init or user-data script."
  type        = string
  default     = null
  sensitive   = true
}
