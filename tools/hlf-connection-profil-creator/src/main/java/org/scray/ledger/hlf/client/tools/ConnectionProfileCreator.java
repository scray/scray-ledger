package org.scray.ledger.hlf.client.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ConnectionProfileCreator {

	public ObjectNode readTemplate(String path) throws JsonParseException, IOException {

		BufferedReader reader = new BufferedReader(new FileReader(path));

	    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	    mapper.findAndRegisterModules();

	    JsonFactory factory = mapper.getFactory();
	    JsonParser parser = factory.createParser(reader);

	    return mapper.readTree(parser);
	}

}
