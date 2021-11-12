# Project Submissions Guidelines

## General guidelines
* Please spend some time to make a proper `ReadME` markdown file, explaining all the steps necessary to execute your source code.
* Do not hardcode IP address or port numbers, try to collect these configurable information from config file/env variables/cmd input args.
* Attach screenshots of your testing done on your local environment.

## Packaging the application
* We'll make use of [Docker](https://en.wikipedia.org/wiki/Docker_(software)) to package and distribute our applications as docker containers! Please spend some time understanding the [basics](https://docs.docker.com/get-started/) of docker.
* Please install Docker desktop
* Feel free to use the sample Dockerfile and scripts provided below

### Sample configuration

#### Project structure
* Before we jump to packaging our application, make sure the source code follows a similar structure with `client` and `server` packages.
```bash
src
├── Dockerfile
├── Project\ 1.iml
├── Project\ Submission\ Guidelines.md
├── Readme.md
├── client
│   ├── AbstractClient.java
│   ├── Client.java
│   ├── ClientApp.java
│   ├── ClientLogger.java
│   ├── TCPClient.java
│   └── UDPClient.java
├── deploy.sh
├── run_client.sh
└── server
    ├── AbstractHandler.java
    ├── KeyValue.java
    ├── Response.java
    ├── ServerApp.java
    ├── ServerLogger.java
    ├── TCPHandler.java
    └── UDPHandler.java

2 directories, 19 files
```
* Compile the code using `javac server/*.java client/*.java`
* server usage should then be similar to `java server.ServerApp <tcp-port-number> <udp-port-number>`
* client usage should then be similar to `java client.ClientApp <host-name> <port-number> <protocol>`
#### Dockerfile

* Use any base image from [openJDK](https://hub.docker.com/_/openjdk) based on Alpine(takes less memory)
* You could use two separate Dockerfiles for client and server, or make use of multistage Dockerfile as shown below and target intermediate images

##### Example Dockerfile
```dockerfile
FROM bellsoft/liberica-openjdk-alpine-musl:11 AS client-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac client/*.java

FROM bellsoft/liberica-openjdk-alpine-musl:11 AS server-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac server/*.java
# cmd to run server locally - java server.ServerApp 1111 5555
CMD ["java", "server.ServerApp", "1111", "5555"]
```

##### 1. Build 

* Run `docker build -t <SERVER_IMAGE> --target server-build .` to build the server docker images.
* This example should create a docker image named <SERVER_IMAGE>.
* Entrypoint is defined for server using CMD, but not for client as we need to run it manually with interactive option to input our desired operations from the console.  
* Note: `bellsoft/liberica-openjdk-alpine-musl:11` is an alpine based image that works on M1 Apple Silicon chips. You could choose any default linux/windows based images from [openJDK](https://hub.docker.com/_/openjdk).

##### 2. Running server

* Run `docker run -p 1111:1111/tcp -p 5555:5555/udp --name <SERVER_CONTAINER> <SERVER_IMAGE>`
* This should run your server image and expose & map the respective ports on the container
* You can now test your server container, with a copy of local client application

##### 3. Running client

* Build `docker build -t <CLIENT_IMAGE> --target client-build .`
* Run `docker run -it --rm --name <CLIENT_CONTAINER> <CLIENT_IMAGE> java client.ClientApp localhost 1111 tcp` should run the client docker image on interactive mode
* This can now be tested with your server running on localhost (not the docker container, yet)

Note: Both server and client docker containers are not on the local host network, so they cannot communicate with each other, yet! But, if one of the programs is running on you local env, then they will be able to communicate!

##### 4. Custom network
* To facilitate the docker containers to communicate with each other, we need to create a virtual network and attach both our containers to this virtual network
* Run `docker network create <PROJECT_NETWORK>` to create a network
* Attach the containers with `--network <PROJECT_NETWORK>` option while running your server or client containers
* Example: `docker run -p 1111:1111/tcp -p 5555:5555/udp --name <SERVER_CONTAINER> --network <PROJECT_NETWORK> <SERVER_IMAGE>`
* Note: dockers attached to custom networks have default DNS as container name, hence we can use docker container name instead of virtual IP address or localhost.

#### Scripting
* We can automate all 4 steps above using shell scripts to avoid repeating frequently used commands
```shell
PROJECT_NETWORK='project1-network'
SERVER_IMAGE='project1-server-image'
SERVER_CONTAINER='my-server'
CLIENT_IMAGE='project1-client-image'
CLIENT_CONTAINER='my-client'

# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
docker container stop $SERVER_CONTAINER 2> /dev/null && docker container rm $SERVER_CONTAINER 2> /dev/null
docker container stop $CLIENT_CONTAINER 2> /dev/null && docker container rm $CLIENT_CONTAINER 2> /dev/null
docker network rm $PROJECT_NETWORK 2> /dev/null

# only cleanup
if [ "$1" == "cleanup-only" ]
then
  exit
fi

# create a custom virtual network
echo "----------creating a virtual network----------"
docker network create $PROJECT_NETWORK

# build the images from Dockerfile
echo "----------Building images----------"
docker build -t $CLIENT_IMAGE --target client-build .
docker build -t $SERVER_IMAGE --target server-build .

# run the image and open the required ports
echo "----------Running sever app----------"
docker run -d -p 1111:1111/tcp -p 5555:5555/udp --name $SERVER_CONTAINER --network $PROJECT_NETWORK $SERVER_IMAGE

echo "----------watching logs from server----------"
docker logs $SERVER_CONTAINER -f
```
* Above script `deploy.sh` should help you build and deploy server images along with a custom network

```shell
CLIENT_IMAGE='project1-client-image'
PROJECT_NETWORK='project1-network'
SERVER_CONTAINER='my-server'

if [ $# -ne 3 ]
then
  echo "Usage: ./run_client.sh <container-name> <port-number> <protocol>"
  exit
fi

# run client docker container with cmd args
docker run -it --rm --name "$1" \
 --network $PROJECT_NETWORK $CLIENT_IMAGE \
 java client.ClientApp $SERVER_CONTAINER "$2" "$3"
 # cmd to run client locally - java client.ClientApp localhost 1111 tcp
```

* `run_client.sh` script above, should help you start a client container on the same network

Note: Do not forget to change the permission of sh files to executable `chmod +x *.sh`

## Helpful tools and commands

### Tools
* [iTerm2](https://iterm2.com/) - should help in managing multiple terminals, we'll be running upto 8 terminals in upcoming projects 
* [Oh my zsh](https://ohmyz.sh/) - helps in code completions and cool themes
  * add `plugins=(git docker docker-compose kubectl)` to `.zshrc` and restart
  * alternatives - [fish](http://fishshell.com/), [ohmybash](https://github.com/ohmybash/oh-my-bash)
* If you prefer GUI, official Docker Plugins on VS Code and IntelliJ or even Docker Desktop 

### Docker commands
* list running containers - `docker container ls`
* list all containers - `docker container ls -a`
* list all networks - `docker network ls`
* inspect containers attached to a network - `docker network inspect <network name>`
* stop running container - `docker container stop <name>`
* delete container - `docker container rm <name>`
* delete network - `docker network rm <name>`


## TL;DR
* Use the Dockerfile provided to package your server and client apps as Docker containers.
* Use the provided shell scripts to automate deployment and running of those containers.
* You only need to modify last line of `Dockerfile` with your server args and last line of `run_client.sh` with your client args

### Feel free to use any of these files as is or modify according to your needs! 
