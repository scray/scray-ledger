#!/bin/bash

source .env

REPO_URL=scrayorg
VERISON=0.1

pushDockerHub() {
    echo "$DOCKER_TOKEN" | docker login -u "$DOCKER_USERNAME" --password-stdin
    echo "$DOCKER_TOKEN"
    echo "$DOCKER_USERNAME"
    docker build -t  "$REPO_URL/$DOCKER_IMAGE_NAME:$VERISON" .
    docker push  "$REPO_URL/$DOCKER_IMAGE_NAME:$VERISON"
}

pushLocal() {
    docker build -t "$REPO_URL/$DOCKER_IMAGE_NAME:$VERISON" .
    docker push "$REPO_URL/$DOCKER_IMAGE_NAME:$VERISON"
}

# Remove runtime data
cleanUp() {
    echo
}

usage() {
    echo "usage: push container to docker registry [[-h push to docker hub ] [-l push to local registry]]"
}

if [[ -z "$1" ]]
then
    usage
fi
                                echo Build image  "$DOCKER_IMAGE_NAME"

while [ "$1" != "" ]; do
    case $1 in
        -l | --local )   shift
                                REPO_URL=$1
                                echo Build image  "$DOCKER_IMAGE_NAME"
                                cleanUp
                                pushLocal
                                ;;
        -h | -docker-hub )      shift
                                echo Build image  "$DOCKER_IMAGE_NAME"
                                cleanUp
                                pushDockerHub
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done
