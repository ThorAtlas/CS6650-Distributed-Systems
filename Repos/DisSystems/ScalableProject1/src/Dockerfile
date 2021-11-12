FROM bellsoft/liberica-openjdk-alpine-musl:11 AS client-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac Clients/*.java

FROM bellsoft/liberica-openjdk-alpine-musl:11 AS TCPServer-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac Servers/*.java
# cmd to run server locally - java server.ServerApp 1111 5555
CMD ["java", "Servers.TCPServer", "1111"]

FROM bellsoft/liberica-openjdk-alpine-musl:11 AS UDPServer-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac Servers/*.java
# cmd to run server locally - java server.ServerApp 1111 5555
CMD ["java", "Servers.UDPServer", "1234"]

