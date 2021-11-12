CLIENT_IMAGE='project1-client-image'
PROJECT_NETWORK='project1-network'
TCPSERVER_CONTAINER='my-tcpserver'
UDPSERVER_CONTAINER='my-udpserver'

if [ $# -ne 2 ]
then
  echo "Usage: ./run_client.sh <container-name> <port-number>"
  exit
fi

# run client docker container with cmd args
docker run -it --rm --name "$1" \
 --network $PROJECT_NETWORK $CLIENT_IMAGE \
 java Clients.TCPClient $TCPSERVER_CONTAINER "$2"
 # cmd to run client locally - java client.ClientApp localhost 1111 tcp