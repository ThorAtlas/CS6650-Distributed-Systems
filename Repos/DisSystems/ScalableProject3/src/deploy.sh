PROJECT_NETWORK='project3-network'
SERVER_IMAGE='project3-controllerserver-image'

CONTROLLER_CLIENT_CONTAINER='my-controllerserver'
HASHSERVER_CONTAINER='my-hashserver'
CLIENT_IMAGE='project3-hashclient-image'
CLIENT_CONTAINER='my-hashclient'



# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
docker container stop $CONTROLLER_CLIENT_CONTAINER 2> /dev/null && docker container rm $CONTROLLER_CLIENT_CONTAINER 2> /dev/null
docker container stop $HASHSERVER_CONTAINER 2> /dev/null && docker container rm $HASHSERVER_CONTAINER 2> /dev/null
docker container stop $CLIENT_CONTAINER 2> /dev/null && docker container rm $CLIENT_CONTAINER 2> /dev/null
docker network rm $PROJECT_NETWORK 2> /dev/null

CONTROLLERIMPORT=1111
POSTIMPORT=1099
if [ $# -eq 1 ]
then
  CONTROLLERIMPORT="$1"
#elif [ $# -eq 2 ]
#then
#  POSTIMPORT="$2"
#  CONTROLLERIMPORT="$1"
fi


# create a custom virtual network
echo "----------creating a virtual network----------"
docker network create $PROJECT_NETWORK

# build the images from Dockerfile
echo "----------Building images----------"
docker build -t $CLIENT_IMAGE --target HashClient-build .
docker build -t $SERVER_IMAGE --target HashServerImpl-build .
#docker build -t $UDPSERVER_IMAGE --target UDPServer-build .

# run the image and open the required ports
echo "----------Running sever app----------"
docker run --name \
$CONTROLLER_CLIENT_CONTAINER --network $PROJECT_NETWORK $SERVER_IMAGE java Server/ControllerImpl \
"$CONTROLLERIMPORT"
#docker run --name \
#$HASHSERVER_CONTAINER --network $PROJECT_NETWORK $SERVER_IMAGE java Server/HashServerImpl \
#"$POSTIMPORT" "$CONTROLLERIMPORT"


#docker run -d -p 1234:1234/udp --name $UDPSERVER_CONTAINER --network $PROJECT_NETWORK $UDPSERVER_IMAGE
