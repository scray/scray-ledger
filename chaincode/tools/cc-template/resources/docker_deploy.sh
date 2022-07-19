#!/bin/bash

source .env

REPO_URL=scrayorg

pushDockerHub() {
    echo "$DOCKER_TOKEN" | docker login -u "$DOCKER_USERNAME" --password-stdin
    echo "$DOCKER_TOKEN"
    echo "$DOCKER_USERNAME"
    docker build -t  "$DOCKER_IMAGE_TAG" .
    docker push  "$DOCKER_IMAGE_TAG"
}

pushLocal() {
    docker build -t "$DOCKER_IMAGE_TAG" .
    docker push "$DOCKER_IMAGE_TAG"
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
                                echo Build image  "$DOCKER_IMAGE_TAG"
                                cleanUp
                                pushLocal
                                ;;
        -h | -docker-hub )      shift
                                echo Build image  "$DOCKER_IMAGE_TAG"
                                cleanUp
                                pushDockerHub
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done
