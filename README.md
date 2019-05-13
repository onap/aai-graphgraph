GraphGraph is a tool for viewing A&AI schemas.

## Limitations

* This is a prototype!
* Works in Firefox. Don't use Chrome if possible
* v13 schema doesn't work (schema is broken)

## Running frontend and backend separately (development)

To start the frontend just run the following command:

### `cd ./graphgraph-fe && npm install && npm start

Then go to http://localhost:3000

In order to start the webservices run the App.java from your Java IDE (the service runs on localhost port 8080). 

## Building docker images


### `mvn clean install -Pdocker
