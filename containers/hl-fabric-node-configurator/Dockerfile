FROM hyperledger/fabric-tools:2.2.1

RUN apk add curl zip
RUN mkdir /mnt/peer-conf && mkdir /mnt/scripts/

COPY containers/hl-fabric-node-configurator/conf /tmp/conf
COPY tools /tmp/tools


COPY containers/hl-fabric-node-configurator/check-configuration.sh /tmp/

CMD /tmp/check-configuration.sh 
