#!/bin/bash

./gradlew clean build

scp build/libs/St-Mina-Mass-Reservation-0.0.1-SNAPSHOT.jar bishoy@main-server.fritz.box:/home/bishoy/stminakirchemuenchen/deployment-scripts/

ssh bishoy@main-server.fritz.box "cd /home/bishoy/stminakirchemuenchen/deployment-scripts &&\
docker rm -f stminakirchemuenchen-backend &&\
docker build -t stminakirchemuenchen-backend . &&\
docker run --name stminakirchemuenchen-backend -d --restart always -p 9065:8080 stminakirchemuenchen-backend"