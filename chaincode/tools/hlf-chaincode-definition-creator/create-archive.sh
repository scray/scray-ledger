#!/bin/sh
mkdir -p target

HOSTNAME=$1
PORT=$2
CC_LABEL=$3
SHARED_FS_HOST=$4

create_description_file() {
  java -jar ./target/hlf-chaincode-definition-creator-1.0-jar-with-dependencies.jar $HOSTNAME $PORT $CC_LABEL
  cd target/${HOSTNAME}_$CC_LABEL/
  tar  -czvf  codetar.tar.gz connection.json
  tar -cvzf chainecode_description.tgz metadata.json codetar.tar.gz
  DESCRIPTION_HASH=$(sha256sum codetar.tar.gz)
  CC_ID=$CC_LABEL:$DESCRIPTION_HASH
}

upload_ccdescription() {
  SHARED_FS_HOST=kubernetes.research.dev.seeburger.de:30080
  SHARED_FS_USER=scray
  SHARED_FS_PW=scray
  apk add curl
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -X MKCOL http://$SHARED_FS_HOST/cc_descriptions
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -X MKCOL http://$SHARED_FS_HOST/cc_descriptions/${HOSTNAME}_$CC_LABEL
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -T chainecode_description.tgz http://$SHARED_FS_HOST/cc_descriptions/${HOSTNAME}_$CC_LABEL/
}

create_description_file
upload_ccdescription