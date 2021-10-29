package org.scray.ledger.hlf.chaincode.configurationfiles;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Connection {
    private final String host;
    private final int port;

    @JsonProperty("dial_timeout")
    private final String dialTimeout = "10s";

    @JsonProperty("tls_required")
    private final boolean tlsRequired = false;

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
