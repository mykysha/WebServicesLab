package org.example.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.component.Column;
import org.example.component.column.*;

import java.io.IOException;

public class ColumnDeserializer extends JsonDeserializer<Column> {

    @Override
    public Column deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String type = node.get("Type").asText();
        String name = node.get("Name").asText();

        switch (type) {
            case "INT":
                return new IntegerColumn(name);
            case "REAL":
                return new RealColumn(name);
            case "STRING":
                return new StringColumn(name);
            case "CHAR":
                return new CharColumn(name);
            case "COMPLEXINT":
                return new ComplexIntegerColumn(name);
            case "COMPLEXREAL":
                return new ComplexRealColumn(name);
            default:
                throw new IllegalArgumentException("Unknown column type: " + type);
        }
    }

}
