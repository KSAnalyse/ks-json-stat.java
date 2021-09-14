package no.ks.fiks.JsonStat;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonStat {
    private String classSet;
    private String label;
    private String source;
    private LocalDateTime updated;
    private List<String> id;
    private List<Integer> size;
    private Map<String, Integer> idSize;
    private List<JsonStatDimension> dimensions;
    private List<BigDecimal> values;
    private Map<String, String> status;
    private Map<String, String> role;

    public JsonStat(String classSet, String label, String source, LocalDateTime updated, List<JsonNode> id,
                    List<Integer> size, List<JsonNode> dimensions, List<BigDecimal> values, Map<String, String> status,
                    Map<String, String> role) {
        this.classSet = classSet;
        this.label = label;
        this.source = source;
        this.updated = updated;
        this.id = convertJsonNodeToString(id);
        this.size = size;
        this.idSize = populateIdSize();
        this.dimensions = addToClass(dimensions);
        this.values = values;
        this.status = status;
        this.role = role;
    }

    private Map<String, Integer> populateIdSize() {
        Map<String, Integer> idSize = new LinkedHashMap<>();
        for (int i = 0; i < this.id.size(); i++)
            idSize.put(this.id.get(i), this.size.get(i));
        return idSize;
    }

    private List<JsonStatDimension> addToClass(List<JsonNode> dimensionValues) {
        List<JsonStatDimension> dimensionMap = new ArrayList<>();
        for (int i = 0; i < this.id.size(); i++) {
            JsonStatDimension jsonStatDimension = new JsonStatDimension(this.id.get(i), dimensionValues.get(i).get("category"));
            dimensionMap.add(jsonStatDimension);
        }
        return dimensionMap;
    }

    private List<String> convertJsonNodeToString(List<JsonNode> listOfStrings) {
        List<String> convertedStrings = new ArrayList<>();
        for (JsonNode jn : listOfStrings) {
            convertedStrings.add(jn.asText());
        }
        return convertedStrings;
    }

    public String getClassSet() {
        return classSet;
    }

    public String getLabel() {
        return label;
    }

    public String getSource() {
        return source;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public List<String> getId() {
        return id;
    }

    public List<Integer> getSize() {
        return size;
    }

    public Map<String, Integer> getIdSize() {
        return idSize;
    }

    public List<JsonStatDimension> getDimensions() {
        return dimensions;
    }

    public List<BigDecimal> getValues() {
        return values;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public Map<String, String> getRole() {
        return role;
    }
}