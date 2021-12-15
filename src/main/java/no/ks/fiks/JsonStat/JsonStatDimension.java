package no.ks.fiks.JsonStat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <h3>JsonStatDimension</h3>
 * This class deserializes the dimensions object and organizes them in different LinkedHashMap, before adding them to
 * {@link #categoryList}.
 */

public class JsonStatDimension {
    private final String dimensionName;
    private final Map<Integer, Map<String, String>> categoryList;
    private final List<JsonStatUnit> jsonStatUnit;

    /**
     * <h3>JsonStatDimension Constructor</h3>
     * <p>
     * Basic constructor that initializes the global variables and call on {@link #addToCategory(JsonNode)} to populate
     * {@link #categoryList}.
     *
     * @param dimensionName The name of the current dimension.
     * @param category      The current category object.
     */

    public JsonStatDimension(String dimensionName, JsonNode category) {
        this.jsonStatUnit = new ArrayList<>();
        this.dimensionName = dimensionName;
        this.categoryList = addToCategory(category);
    }

    /**
     * <h3>addToCategory</h3>
     * <p>
     * This method splits up the three parts of category object in the JSON-Stat result. Splits it up on index, label
     * and unit (if the category object is ContentsCode). While the index and label objects are added to dimCat
     * LinkedHashMap and is returned, the unit object is added directly to the global jsonStatUnit object list.
     * The unit object contains which variable has decimal values and how many decimals. This isn't used directly by the structuring
     * solution, but used when creating a table.
     *
     * @param categories This is the categories object of the dimension.
     * @return Returns the map dimCat to {@link #categoryList}.
     */
    private Map<Integer, Map<String, String>> addToCategory(JsonNode categories) {
        Map<Integer, Map<String, String>> dimCat = new LinkedHashMap<>();
        Map<String, Object> index = categories("index", categories);
        Map<String, Object> label = categories("label", categories);
        if (dimensionName.equals("ContentsCode")) {
            Map<String, Object> unit = categories("unit", categories);
            for (String dimName : unit.keySet()) {
                String[] splitUnit = unit.get(dimName).toString().split(",");
                String base = splitUnit[0].replaceAll("\\{base=", "").trim();
                String stringDecimals = splitUnit[1].replaceAll("decimals=|\\}", "").trim();
                int decimal = Integer.parseInt(stringDecimals);
                jsonStatUnit.add(new JsonStatUnit(dimName, base, decimal));
            }
        }
        for (String key : index.keySet()) {
            Map<String, String> newLabel = new LinkedHashMap<>();
            newLabel.put(key, (String) label.get(key));
            dimCat.put((Integer) index.get(key), newLabel);
        }
        return dimCat;
    }

    /**
     * <h3>categories</h3>
     * <p>
     * This method reads the categories object and adds them to a String, Object Map and returns it to
     * {@link #addToCategory(JsonNode)}
     *
     * @param categoryName This is the category name we want to read from.
     * @param categories   This is the JsonNode object we are reading from.
     * @return Returns a map of the category name we wanted to read from in categories object.
     */
    private Map<String, Object> categories(String categoryName, JsonNode categories) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(categories.get(categoryName), new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * <h3>getCategoryList</h3>
     *
     * @return Returns the categoryList map.
     */

    public Map<Integer, Map<String, String>> getCategoryList() {
        return categoryList;
    }

    /**
     * <h3>getDimensionName</h3>
     *
     * @return Returns the dimension name String.
     */

    @SuppressWarnings("unused")
    public String getDimensionName() {
        return dimensionName;
    }

    /**
     * <h3>getJsonStatUnit</h3>
     *
     * @return Returns the list of the JsonStatUnit object.
     */

    public List<JsonStatUnit> getJsonStatUnit() {
        return jsonStatUnit;
    }
}
