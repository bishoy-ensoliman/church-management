#!/bin/bash

rsync -av -e ssh --exclude='node_modules' * bishoy@main-server.fritz.box:/home/bishoy/stminakirchemuenchen/deployment-scripts/frontend

ssh bishoy@main-server.fritz.box "cd /home/bishoy/stminakirchemuenchen/deployment-scripts/frontend &&\
 docker rm -f stminakirchemuenchen-frontend && docker build -t stminakirchemuenchen-frontend . &&\
 docker run --name stminakirchemuenchen-frontend -d --restart always -p 5000:5000 stminakirchemuenchen-frontend"