package no.ks.fiks.JsonStat;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <h3>JsonStat</h3>
 * <p>
 * A Basic POJO class for JsonStat, with three methods to populate {@link #id}, {@link #idSize} and {@link #dimensions}.
 */

public class JsonStat {
    private final String classSet;
    private final String label;
    private final String source;
    private final LocalDateTime updated;
    private final List<String> id;
    private final List<Integer> size;
    private final Map<String, Integer> idSize;
    private final List<JsonStatDimension> dimensions;
    private final List<BigDecimal> values;
    private final Map<String, String> status;
    private final Map<String, String> role;

    /**
     * <h3>JsonStat constructor</h3>
     * <p>
     * Basic constructor that initializes the global variables in this class.
     *
     * @param classSet   The classSet String in the table query result.
     * @param label      The label String in the table query result.
     * @param source     The source String in the table query result.
     * @param updated    The updated String in the table query result, this is when the table was last updated.
     * @param id         This is a list of the id names of the table query result.
     * @param size       This is a list of the number of dimensions per id.
     * @param dimensions This is a list of objects for the dimension object in the table query result.
     * @param values     This is a list of the values from the table query result
     * @param status     This is a list that says which index in the values list is 'Not Reported'
     * @param role
     */

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
        this.dimensions = populateDimensionsList(dimensions);
        this.values = values;
        this.status = status;
        this.role = role;
    }

    /**
     * <h3>populateIdSize</h3>
     * Combines the id and size lists to a Map.
     *
     * @return Returns the combined Map of {@link #id} and {@link #size}
     */

    private Map<String, Integer> populateIdSize() {
        Map<String, Integer> idSize = new LinkedHashMap<>();
        for (int i = 0; i < this.id.size(); i++)
            idSize.put(this.id.get(i), this.size.get(i));
        return idSize;
    }

    /**
     * <h3>populateDimensionsList</h3>
     * <p>
     * Populates the {@link #dimensions} list with the category object from dimension JsonNode.
     *
     * @param dimensionValues List of the dimension objects
     * @return Returns a list of JsonStatDimension objects.
     */
    private List<JsonStatDimension> populateDimensionsList(List<JsonNode> dimensionValues) {
        List<JsonStatDimension> dimensionMap = new ArrayList<>();
        for (int i = 0; i < this.id.size(); i++) {
            JsonStatDimension jsonStatDimension = new JsonStatDimension(this.id.get(i), dimensionValues.get(i).get("category"));
            dimensionMap.add(jsonStatDimension);
        }
        return dimensionMap;
    }

    /**
     * <h3>convertJsonNodeToString</h3>
     * <p>
     * Converts the JsonNode object of id's to String and returns it as a list.
     *
     * @param listOfStrings List of JsonNode objects for id.
     * @return Returns a list of strings.
     */
    private List<String> convertJsonNodeToString(List<JsonNode> listOfStrings) {
        List<String> convertedStrings = new ArrayList<>();
        for (JsonNode jn : listOfStrings) {
            convertedStrings.add(jn.asText());
        }
        return convertedStrings;
    }

    /**
     * <h3>getClassSet</h3>
     *
     * @return Returns the classSet string.
     */

    public String getClassSet() {
        return classSet;
    }

    /**
     * <h3>getLabel</h3>
     *
     * @return Returns the label string.
     */

    public String getLabel() {
        return label;
    }

    /**
     * <h3>getSource</h3>
     *
     * @return Returns the source string.
     */

    public String getSource() {
        return source;
    }

    /**
     * <h3>getUpdated</h3>
     *
     * @return Returns the updated LocalDateTime variable.
     */

    public LocalDateTime getUpdated() {
        return updated;
    }

    /**
     * <h3>getId</h3>
     *
     * @return Returns the list of id's.
     */

    public List<String> getId() {
        return id;
    }

    /**
     * <h3>getSize</h3>
     *
     * @return Returns the list of size.
     */

    public List<Integer> getSize() {
        return size;
    }

    /**
     * <h3>getIdSize</h3>
     *
     * @return Returns the Map of the combined {@link #id} and {@link #size}
     */

    public Map<String, Integer> getIdSize() {
        return idSize;
    }

    /**
     * <h3>getDimensions</h3>
     *
     * @return Returns the list of JsonStatDimension objects
     */

    public List<JsonStatDimension> getDimensions() {
        return dimensions;
    }

    /**
     * <h3>getValues</h3>
     *
     * @return Returns the list of values.
     */

    public List<BigDecimal> getValues() {
        return values;
    }

    /**
     * <h3>getStatus</h3>
     *
     * @return Returns the Map of status.
     */

    public Map<String, String> getStatus() {
        return status;
    }

    /**
     * <h3>getRole</h3>
     *
     * @return Returns the map of role.
     */

    public Map<String, String> getRole() {
        return role;
    }
}