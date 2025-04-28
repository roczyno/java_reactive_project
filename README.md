# Java Reactive Project

This is a Spring Boot reactive application with a CI/CD pipeline using Jenkins for building, containerizing, and deploying to EC2.

## CI/CD Pipeline

The project includes a Jenkins pipeline that:

1. Builds the application JAR file
2. Creates a Docker image
3. Pushes the Docker image to Docker Hub
4. Deploys the application to an EC2 instance

## Prerequisites

- Jenkins server with the following tools installed:
  - Maven
  - Docker
  - AWS CLI
- Docker Hub account
- AWS account with an EC2 instance

## Jenkins Credentials Setup

The following credentials need to be configured in Jenkins:

1. **aws-access-key-id**: Your AWS Access Key ID
   - Kind: Secret text
   - Scope: Global

2. **aws-secret-access-key**: Your AWS Secret Access Key
   - Kind: Secret text
   - Scope: Global

3. **ec2-host**: The public DNS or IP address of your EC2 instance
   - Kind: Secret text
   - Scope: Global

4. **ec2-ssh-key**: The private SSH key for connecting to your EC2 instance
   - Kind: Secret text
   - Scope: Global
   - Content: The entire private key including header and footer

5. **docker-hub-credentials**: Your Docker Hub credentials
   - Kind: Username with password
   - Scope: Global
   - Username: Your Docker Hub username
   - Password: Your Docker Hub password

## EC2 Instance Setup

Ensure your EC2 instance:

1. Has Docker installed
2. Has proper security group settings to allow:
   - SSH access (port 22) from your Jenkins server
   - HTTP/HTTPS access (ports 80/443) from the internet
   - Application access (port 8080) from the internet
3. Has sufficient permissions to pull Docker images

## Running the Pipeline

1. Create a new Jenkins Pipeline job
2. Configure it to use the Jenkinsfile from this repository
3. Run the pipeline

## Troubleshooting

- If the deployment fails, check the EC2 instance's Docker logs:
  ```
  docker logs reactive-app
  ```
- Verify that all credentials are correctly set up in Jenkins
- Ensure the EC2 instance has Docker installed and running
- Check that the security groups allow the necessary traffic
