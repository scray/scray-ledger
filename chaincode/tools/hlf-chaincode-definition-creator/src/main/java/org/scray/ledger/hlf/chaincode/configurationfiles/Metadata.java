package org.scray.ledger.hlf.chaincode.configurationfiles;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {

    @JsonProperty("type")
    private final String type = "external";

    @JsonProperty("label")
    private final String label;

    public Metadata(String label) {
        this.label = label;
    }


}
