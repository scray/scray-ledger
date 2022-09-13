package org.scray.ledger.hlf.connectionprofile.nodes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.List;

public class Organisation {

    private String orgName;
    private String mspid;
    private List<String> peers;
    private List<String> certificateAuthorities;

    public Organisation(String orgName, String mspid, List<String> peers, List<String> certificateAuthorities) {
        this.orgName = orgName;
        this.mspid = mspid;
        this.peers = peers;
        this.certificateAuthorities = certificateAuthorities;
    }

    public ObjectNode getJsonNode() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode organization = mapper.createObjectNode();

        organization.put("mspid", this.mspid);

        // List of peers
        ArrayNode peerList = mapper.createArrayNode();
        for(String peer: this.peers) {
            peerList.add(peer);
        }

        organization.put("peers", peerList);

        // Set certificate certificateAuthoritiesorities
        ArrayNode caList = mapper.createArrayNode();
        for(String ca: this.certificateAuthorities) {
            caList.add(ca);
        }

        ObjectNode cas = mapper.createObjectNode();
        organization.put("certificateAuthorities", caList);

        ObjectNode orgNode = mapper.createObjectNode();
        orgNode.set(this.orgName, organization);

        return orgNode;
    }
}
