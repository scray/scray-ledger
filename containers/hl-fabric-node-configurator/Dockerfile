FROM hyperledger/fabric-tools:2.2.1

RUN apk add curl zip
RUN mkdir /mnt/peer-conf && mkdir /mnt/scripts/

COPY containers/hl-fabric-node-configurator/conf /tmp/conf
COPY tools /tmp/tools

CMD cp -r /tmp/conf /mnt/peer-conf/ &&\
    cp -r /tmp/tools /mnt/peer-conf/ &&\
    cd /mnt/peer-conf/conf/ &&\
    ./run.sh -d $HOSTNAME -o $ORG_NAME -t $NODE_TYPE -s "$DATA_SHARE" --ca_country "$CA_COUNTRY" --ca_province "$CA_PROVINCE" --ca_locality "$CA_LOCALITY"
