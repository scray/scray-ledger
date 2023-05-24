#!/bin/bash
BASE_PATH=$PWD
SANS=""

YQ_VERSON2=4.4.1


yq() {
  $BASE_PATH/bin/yq $1 $2 $3 $4 $5
}

# Check if yq exists
checkYqVersion() {
  dowloadYqBin
}

dowloadYqBin() {
  if [[ ! -f "./bin/yq" ]]
  then
    echo "yq does not exists"
    if [ "$OSTYPE" == "linux-gnu" ]
    then
      echo "download linux_amd64 yq binary"
      mkdir bin
      curl -L https://github.com/mikefarah/yq/releases/download/3.4.1/yq_linux_amd64 -o ./bin/yq
      chmod u+x ./bin/yq
    elif [ "$OSTYPE" == "msys" ]
    then
      echo "download yq_windows_amd64  yq binary"
      mkdir bin
      curl -L https://github.com/mikefarah/yq/releases/download/3.4.1/yq_windows_amd64.exe -o ./bin/yq
      chmod u+x ./bin/yq
    fi
  fi
}

dowloadYqBin2() {
  if [[ ! -f "./bin/yq-${YQ_VERSON}" ]]
  then
    echo "yq does not exists"
    echo "download linux_amd64 yq binary"

    mkdir bin
    curl -L https://github.com/mikefarah/yq/releases/download/v${YQ_VERSON2}/yq_linux_amd64 -o ./bin/yq-${YQ_VERSON2}
    chmod u+x ./bin/yq-${YQ_VERSON2}
  fi
}

yq_path() {
  echo $BASE_PATH/bin/yq-${YQ_VERSON2} $1 $2 $3 $4 $5
}

readParameters()
{
  ORG_CRYPTO_CONFIG_FILE=crypto.yaml.org
  CRYPTO_CONFIG_FILE="crypto.yaml"
  BASE_PATH=$PWD


  while [ "$1" != "" ]; do
      case $1 in
           --org_name)	shift
                        NEW_ORG=$1
                        ;;
          --domain)  shift
                      DOMAIN=$1
                      ;;
          --ca_country )  shift
                          COUNTRY=$1
                          ;;
          --ca_province ) shift
                          PROVICE=$1
                          ;;
          --ca_locality)	shift
                  LOCALITY=$1
                            ;;
           -s | --sans )   	shift
                  SANS=$1
                    ;;
          --upload) shift
            CURL_UPLOAD_URL=$1
                          ;;
      esac
      shift
  done
}

readParameters "$@"


# Set default parameters for some parameters

if [ -z "$COUNTRY" ]
then
      COUNTRY="DE"
fi

if [ -z "$PROVICE" ]
then
      PROVICE="Baden"
fi

if [ -z "$LOCALITY" ]
then
      LOCALITY="Bretten"
fi


checkYqVersion
dowloadYqBin2

cp $ORG_CRYPTO_CONFIG_FILE $CRYPTO_CONFIG_FILE
echo $CRYPTO_CONFIG_FILE
echo $ORG_CRYPTO_CONFIG_FILE
echo Country:  $COUNTRY
echo Province: $PROVICE
echo Locality: $LOCALITY

# Update name
yq w -i $CRYPTO_CONFIG_FILE  "PeerOrgs[0].Name" $NEW_ORG
# Update Domain
yq w -i $CRYPTO_CONFIG_FILE  "PeerOrgs[0].Domain" $DOMAIN

yq w -i $CRYPTO_CONFIG_FILE  "PeerOrgs[0].CA.Country"  $COUNTRY
yq w -i $CRYPTO_CONFIG_FILE  "PeerOrgs[0].CA.Province" $PROVICE
yq w -i $CRYPTO_CONFIG_FILE  "PeerOrgs[0].CA.Locality" $LOCALITY

yq w -i $CRYPTO_CONFIG_FILE "PeerOrgs[0].Specs[0].Hostname" $DOMAIN

# Add SANS
echo "source ../yq_lib.sh" > update_SANS.sh
YQ_CHANGE_COMMAND=$(echo \''.PeerOrgs[0].Specs[0].SANS += '\"$DOMAIN\"\')
echo "$(yq_path) -i eval $YQ_CHANGE_COMMAND crypto.yaml " >> update_SANS.sh

if [ "$SANS" != "" ]
then
      IFS=',' read -ra SANS <<< "$SANS"
      for san in "${SANS[@]}"
      do
        YQ_CHANGE_COMMAND=$(echo \''.PeerOrgs[0].Specs[0].SANS += '\"$san\"\')
        echo "$(yq_path) -i eval $YQ_CHANGE_COMMAND crypto.yaml" >> update_SANS.sh
      done
fi

	chmod u+x update_SANS.sh
	./update_SANS.sh