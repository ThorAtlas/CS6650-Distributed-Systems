#todo edit
CLIENT_IMAGE='project2-hashclient-image'
PROJECT_NETWORK='project2-network'
HASHSERVER_CONTAINER='my-hashserver'
CONTROLLER_CONTAINER='my-controllerserver'

if [ $# -lt 1 ]
then
  echo "Usage: ./run_hashServer.sh <container-name> <OPTIONAL HASHPORT> <OPTIONAL HOSTNAME>
  <OPTIONALCONTROLLERPORT>"
  exit
fi

HOSTIMPORT=$CONTROLLER_CONTAINER
POSTIMPORT=1111
if [ $# -eq 2 ]
then
  HOSTIMPORT="$2"
elif [ $# -eq 3 ]
then
  HOSTIMPORT="$2"
  POSTIMPORT="$3"
fi

# run client docker container with cmd args
docker run -i --rm --name "$1"  \
--network $PROJECT_NETWORK $CLIENT_IMAGE \
java Server.HashServerImpl "$HOSTIMPORT" "$POSTIMPORT"
 # cmd to run client locally - java client.ClientApp localhost 1111 tcp

#docker run --rm --name "test"  --network 'project2-network'
# 'project2-hashclient-image' java Server.HashClient 'my-hashserver'