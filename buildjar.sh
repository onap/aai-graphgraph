#!/bin/sh 
cd ./graphgraph-fe/ 
npm install
npm run build
cd ..
mvn clean package spring-boot:repackage
