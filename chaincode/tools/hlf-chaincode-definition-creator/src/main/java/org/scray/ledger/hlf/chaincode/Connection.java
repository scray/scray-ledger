package org.scray.ledger.hlf.chaincode;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Connection {
    private String host;
    private int port;

    @JsonProperty("dial_timeout")
    private String dialTimeout = "10s";

    @JsonProperty("tls_required")
    private boolean tlsRequired = false;

    @JsonProperty("address")
    private String address;

    public Connection(String hostname, int port) {
        this.host = hostname;
        this.port = port;
    }

    public String getAddress() {
        return host + ":" + port;
    }
}
