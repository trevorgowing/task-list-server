#!/usr/bin/env bash

set -e

sudo docker-compose up -d postgres
sudo docker-compose up --build task-list-server
