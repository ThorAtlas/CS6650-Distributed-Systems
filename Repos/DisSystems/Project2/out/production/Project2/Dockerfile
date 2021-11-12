FROM bellsoft/liberica-openjdk-alpine-musl:11 AS HashServerImpl-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac Server/*.java
# cmd to run server locally - java server.ServerApp 1111 5555
#CMD ["java", "Server/HashServerImpl"]


FROM bellsoft/liberica-openjdk-alpine-musl:11 AS HashClient-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac Server/*.java



