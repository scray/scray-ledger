 # Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0

ARG GO_VER=1.14.4
ARG ALPINE_VER=3.16

FROM golang:${GO_VER}-alpine${ALPINE_VER}

# Install ssh server
RUN apk add --update --no-cache openssh
RUN echo 'PasswordAuthentication yes' >> /etc/ssh/sshd_config

# Add ssh dev user
RUN adduser -h /home/dev -s /bin/sh -D dev

WORKDIR /go/src/github.com/hyperledger/fabric-samples/asset-transfer-basic/chaincode-external
COPY . .

RUN go get -d -v ./...
RUN go install -v ./...

EXPOSE 9999
EXPOSE 22

CMD ["sh", "-c", "chaincode-external; tail -f /dev/null"]
