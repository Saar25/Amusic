# Amusic

A client-server application to share and listen to audio

## Manual Running

The application requires docker (to run database), and java 17 (for server and client)  
Then you can initialize the project libraries using

```bash
./mvnw clean install
```

### Database

The server would not be able to run if the database is down so first run the database

```bash
docker compose up postgres
```

### Server

#### First time running the application
If you run the application for the first time, you need to initialize the database using spring  
in the file `application.properties` under `resources` directory in `server` module,  
uncomment lines 16-18 under section `Database initialization`, then run the server, then comment them again  
this would make sure spring initializes the tables, and populate them with some initial data specified in data.sql

#### otherwise

After the database is up, you can run the server using maven

```bash
./mvnw spring-boot:run -pl server
```

### Client

And at last you can run the client using the following command

```bash
./mvnw javafx:run -pl client
```
