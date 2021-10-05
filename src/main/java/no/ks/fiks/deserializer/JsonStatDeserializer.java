package no.ks.fiks.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import no.ks.fiks.JsonStat.JsonStat;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <h3>JsonStatDeserializer</h3>
 * <p>
 * This class deserializes a JSON-Stat query result that is provided. By overriding deserialize method of JsonDeserializer,
 * we have created a custom deserializer for the unique structure of JSON-Stat.
 */

public class JsonStatDeserializer extends JsonDeserializer<JsonStat> {


    /**
     * <h3>deserialize</h3>
     * <p>
     * This method overrides the {@link #JsonDeserializer} deserialize method. A custom deserializer is written to handle
     * JSON-Stat result.
     *
     * @param jsonParser             The object with the JSON-Stat value
     * @param deserializationContext
     * @return Returns new JsonStat object with the deserialized values.
     * @throws IOException
     * @see JsonDeserializer
     */
    @Override
    public JsonStat deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jsonParser);

        final String classSet = jsonNode.get("class").asText();
        final String label = jsonNode.get("label").asText();
        final String source = jsonNode.get("source").asText();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final LocalDateTime updated = LocalDateTime.parse(jsonNode.get("updated").asText(), dtf);
        final List<JsonNode> id = new ArrayList<>();
        for (JsonNode jn : jsonNode.get("id")) {
            id.add(jn);
        }

        final List<Integer> size = new ArrayList<>();
        for (JsonNode jn : jsonNode.get("size")) {
            size.add(jn.intValue());
        }

        final List<JsonNode> dimensions = new ArrayList<>();
        for (JsonNode idName : id) {
            dimensions.add(jsonNode.get("dimension").get(idName.asText()));
        }

        final List<BigDecimal> values = new ArrayList<>();
        for (JsonNode value : jsonNode.get("value")) {
            if (value.isNull()) {
                values.add(null);
            } else {
                values.add(value.decimalValue());
            }
        }

        final Map<String, String> status = new LinkedHashMap<>();
        String[] statusSplit;
        if (jsonNode.get("status") != null) {
            statusSplit = splitJsonNode(jsonNode, "status");
            for (String s : statusSplit) {
                String[] cleanUpStatus = cleanString(s);
                status.put(cleanUpStatus[0], cleanUpStatus[1]);
            }
        }

        final Map<String, String> role = new LinkedHashMap<>();
        String[] roleSplit = splitJsonNode(jsonNode, "role");
        for (String s : roleSplit) {
            String[] cleanUpRole = cleanString(s);
            role.put(cleanUpRole[0], cleanUpRole[1]);
        }

        return new JsonStat(classSet, label, source, updated, id, size, dimensions, values, status, role);
    }

    /**
     * <h3>splitJsonNode</h3>
     * <p>
     * This method splits a jsonNode string.
     *
     * @param jsonNode JsonNode object used in {@link #deserialize(JsonParser, DeserializationContext)}
     * @param nodeName The name of the JSON object.
     * @return Returns the split string as an array.
     */

    private String[] splitJsonNode(JsonNode jsonNode, String nodeName) {
        return jsonNode.get(nodeName).toString().split(",");
    }

    /**
     * <h3>cleanString</h3>
     *
     * Cleans up the string before returning it as an String Array.
     * @param jsonString The string that will have symbols removed.
     * @return Returns the cleaned up string as an String Array.
     */
    private String[] cleanString(String jsonString) {
        String[] cleanUp = jsonString.split(":");
        cleanUp[0] = cleanUp[0].replaceAll("\\{|\\[", "");
        cleanUp[1] = cleanUp[1].replaceAll("\\}|\\[|\\]", "");
        return cleanUp;
    }
}
