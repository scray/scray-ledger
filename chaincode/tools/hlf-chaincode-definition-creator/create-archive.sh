#!/bin/bash

SCRIPT_DIR="$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd $SCRIPT_DIR

mkdir -p target

HOSTNAME=$1
PORT=$2
CC_LABEL=$3
SHARED_FS_HOST=$4



create_description_file() {
  java -jar ./target/hlf-chaincode-definition-creator-1.0-jar-with-dependencies.jar $HOSTNAME $PORT $CC_LABEL
  cd target/${HOSTNAME}_$CC_LABEL/
  tar  -czvf  code.tar.gz connection.json
  tar -cvzf chainecode_description.tgz metadata.json code.tar.gz
  DESCRIPTION_HASH=$(sha256sum chainecode_description.tgz  | head -c 64)
  CC_ID=$CC_LABEL:$DESCRIPTION_HASH

  echo "{ \"description-hash\": \"$CC_ID\" }" > description-hash.json
}

upload_ccdescription() {
  SHARED_FS_USER=scray
  SHARED_FS_PW=scray
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -X MKCOL http://$SHARED_FS_HOST/cc_descriptions
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -X DELETE http://$SHARED_FS_HOST/cc_descriptions/${HOSTNAME}_$CC_LABEL
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -X MKCOL  http://$SHARED_FS_HOST/cc_descriptions/${HOSTNAME}_$CC_LABEL
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -T chainecode_description.tgz http://$SHARED_FS_HOST/cc_descriptions/${HOSTNAME}_$CC_LABEL/chainecode_description.tgz
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -T chainecode_description.tgz http://$SHARED_FS_HOST/cc_descriptions/${HOSTNAME}_$CC_LABEL/
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -T description-hash.json      http://$SHARED_FS_HOST/cc_descriptions/${HOSTNAME}_$CC_LABEL/
}

create_description_file
upload_ccdescription
