Docker Secrets and IG secrets
====
Docker Secrets are only accessible to services which have been granted explicit access to it.
The Docker service can manage one or n containers. The Docker Secrets are encrypted during transit between host and 
container(s) and the Docker Secrets are exposed in flat files accessible in the containers under /run/secrets/
As there is no way to define a specific encryption for the Docker Secrets, our secrets management with flat files
(using a FileSystemSecretStore) is enough to insure compatibility with Docker Secrets and IG Secrets.

**STEP-1**: Retrieve latest IG Docker image: `docker pull forgerock-docker-public.bintray.io/forgerock/ig:latest`

**STEP-2**: Copy the folder `.openig` where the hello.json route and script are available.
  
**STEP-3**: **Configure your docker** 
- Init Swarm: `$ docker swarm init`
- Create a Docker secret: `$ printf "Sam_Carter" |  docker secret create mysecret -`
- Create a service using that Docker secret:
`$ docker service create --name ig_secrets --secret mysecret --publish published=8080,target=8080 forgerock/ig`
- Copy your IG folder from Docker host to container, example:
`$ docker cp /path_to_ig/.openig/. <container_name>:/var/openig/.`
- Run your container in interactive mode: ` docker exec -it <docker_container_sha1> sh`
- View the Docker secret under `/run/secret/mysecret`
- Access to the container from your host: `http://<Host IP>:8080/hello`

![Hello Running](https://raw.githubusercontent.com/openig-contrib/script-util-for-openig/master/media/hello.png)

