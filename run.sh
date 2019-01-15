#!/bin/sh 
cd ./graphgraph-fe/ 
npm run build
cd ..
mvn clean spring-boot:run 
