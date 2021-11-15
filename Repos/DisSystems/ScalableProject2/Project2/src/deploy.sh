PROJECT_NETWORK='project2-network'
SERVER_IMAGE='project2-hashserverimpl-image'
HASHSERVER_CONTAINER='my-hashserver'
CLIENT_IMAGE='project2-hashclient-image'
CLIENT_CONTAINER='my-hashclient'
# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
#docker container stop $HASHSERVER_CONTAINER 2> /dev/null && docker container rm $HASHSERVER_CONTAINER 2> /dev/null
##docker container stop $UDPSERVER_CONTAINER 2> /dev/null && docker container rm $UDPSERVER_CONTAINER 2> /dev/null
#docker container stop $CLIENT_CONTAINER 2> /dev/null && docker container rm $CLIENT_CONTAINER 2> /dev/null
#docker network rm $PROJECT_NETWORK 2> /dev/null
#POSTIMPORT=1099
if [ $# -eq 1 ]
then
  echo "we in da then"
  #POSTIMPORT="$1"
fi
## create a custom virtual network
#echo "----------creating a virtual network----------"
#docker network create $PROJECT_NETWORK
#
## build the images from Dockerfile
#echo "----------Building images----------"
#docker build -t $CLIENT_IMAGE --target HashClient-build .
#docker build -t $SERVER_IMAGE --target HashServerImpl-build .
##docker build -t $UDPSERVER_IMAGE --target UDPServer-build .
#
## run the image and open the required ports
#echo "----------Running sever app----------"
#docker run --name $HASHSERVER_CONTAINER --network $PROJECT_NETWORK --rm $SERVER_IMAGE java Server/HashServerImpl "$POSTIMPORT"
#echo "We at da end"