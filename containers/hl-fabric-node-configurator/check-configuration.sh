
CONF_LOCATION=/mnt/peer-conf/conf/

if [ -d "$CONF_LOCATION" ]; then
    echo "WARNING: Configuration folder $CONF_LOCATION  is not empty. Will not create a new configuration" 
else 
  echo "Create new configuration."
  cp -r /tmp/conf /mnt/peer-conf/ 
  cp -r /tmp/tools /mnt/peer-conf/ 
  cd /mnt/peer-conf/conf/ &&\
  ./run.sh -d $HOSTNAME -o $ORG_NAME -t $NODE_TYPE -s "$DATA_SHARE" --ca_country "$CA_COUNTRY" --ca_province "$CA_PROVINCE" --ca_locality "$CA_LOCALITY" --sans "$SANS"  
fi

