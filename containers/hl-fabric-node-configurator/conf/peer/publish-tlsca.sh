#!/bin/bash

CA_CERT_PATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/tlsca/tlsca.$HOSTNAME-cert.pem
SHARED_FS_HOST=fs.example.com
SHARED_FS_USER=scray
SHARED_FS_PW=scray

function publish() {
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -X MKCOL --silent http://$SHARED_FS_HOST/peer-tlsca-certs/
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -X MKCOL --silent http://$SHARED_FS_HOST/peer-tlsca-certs//"$HOSTNAME"/
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -X DELETE http://$SHARED_FS_HOST/peer-tlsca-certs/"$HOSTNAME"/tlsca.pem
  curl --user $SHARED_FS_USER:$SHARED_FS_PW -T "$CA_CERT_PATH" http://$SHARED_FS_HOST/peer-tlsca-certs/"$HOSTNAME"/tlsca.pem
}

{
  while [ "$1" != "" ]; do
      case $1 in
        --shared-fs-host) shift
          SHARED_FS_HOST=$1
          publish
          ;;
      * )
        echo "Mandatory parameter --shared-fs-host is missing. --shared-fs-host http://share.scray.org"
        exit 1
      esac
      shift
  done
}