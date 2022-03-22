#!/bin/bash

rsync -av -e ssh --exclude='node_modules' * ubuntu@ubuntu.fritz.box:/home/ubuntu/stminakirchemuenchen/frontend

ssh ubuntu@ubuntu.fritz.box "cd /home/ubuntu/stminakirchemuenchen/frontend &&\
 docker build -t stminakirchemuenchen-frontend . && docker rm -f stminakirchemuenchen-frontend &&\
 docker run --name stminakirchemuenchen-frontend -d --restart always -p 5000:5000 stminakirchemuenchen-frontend"