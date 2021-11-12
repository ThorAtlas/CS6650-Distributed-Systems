PROJECT_NETWORK='project1-network'
TCPSERVER_IMAGE='project1-tcpserver-image'
UDPSERVER_IMAGE='project1-udpserver-image'

TCPSERVER_CONTAINER='my-tcpserver'
UDPSERVER_CONTAINER='my-udpserver'
CLIENT_IMAGE='project1-client-image'
CLIENT_CONTAINER='my-client'

# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
docker container stop $TCPSERVER_CONTAINER 2> /dev/null && docker container rm $TCPSERVER_CONTAINER 2> /dev/null
docker container stop $UDPSERVER_CONTAINER 2> /dev/null && docker container rm $UDPSERVER_CONTAINER 2> /dev/null
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
docker build -t $TCPSERVER_IMAGE --target TCPServer-build .
docker build -t $UDPSERVER_IMAGE --target UDPServer-build .

# run the image and open the required ports
echo "----------Running sever app----------"
docker run -d -p 1111:1111/tcp --name $TCPSERVER_CONTAINER --network $PROJECT_NETWORK $TCPSERVER_IMAGE
docker run -d -p 1234:1234/udp --name $UDPSERVER_CONTAINER --network $PROJECT_NETWORK $UDPSERVER_IMAGE
