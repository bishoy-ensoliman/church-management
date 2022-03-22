#!/bin/bash

./gradlew clean build

scp build/libs/St-Mina-Mass-Reservation-0.0.1-SNAPSHOT.jar ubuntu@ubuntu.fritz.box:/home/ubuntu/stminakirchemuenchen/backend
scp Dockerfile ubuntu@ubuntu.fritz.box:/home/ubuntu/stminakirchemuenchen/backend


ssh ubuntu@ubuntu.fritz.box "cd /home/ubuntu/stminakirchemuenchen/backend &&\
docker build -t stminakirchemuenchen-backend . &&\
docker rm -f stminakirchemuenchen-backend &&\
docker run --name stminakirchemuenchen-backend -d --restart always -p 9065:8080 stminakirchemuenchen-backend"