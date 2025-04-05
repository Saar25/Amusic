# Amusic

A client-server application to share and listen to audio

## Manual Running

The application requires docker (to run database), and java 17 (for server and client)  
Then you can the project libraries using

```bash
./mvnw clean install
```

### Database

The server would not be able to run if the database is down so first run the database

```bash
docker compose up postgres
```

### Server

After the database is up, you can run the server using either maven or docker

Using maven:

```bash
./mvnw spring-boot:run -pl server
```
### Client

And at last you can run the client using the following command

```bash
./mvnw javafx:run -pl client
```
