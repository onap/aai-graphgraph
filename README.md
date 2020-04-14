GraphGraph is a tool for viewing A&AI schemas.

## Limitations

* This is a prototype!
* Works in Firefox. Don't use Chrome if possible
* v13 schema doesn't work (schema is broken)

## Running frontend and backend separately (development)

To start the frontend just run the following command:

### `cd ./graphgraph-fe && npm install && npm start`

The UI will be available on http://localhost:3000

In order to start the webservices run the App.java from your Java IDE (the service runs on localhost port 8080).

## Building docker images

### `mvn clean install -Pdocker`

## Running GraphGraph locally

You have to start a local instance (i.e. localhost) of schema-service. Afterwards build the GraphGraph JAR file with:

### `mvn clean install`

Go into the target directory and run the JAR file with -d switch:

### `java -jar graphgraph-X.Y.jar -d`

where X.Y is the current version. For more options run the help via:

### `java -jar graphgraph-X.Y.jar -h`
