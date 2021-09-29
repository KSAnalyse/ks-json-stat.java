package no.ks.fiks.JsonStat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonStatDimension {
    private List<JsonStatUnit> jsonStatUnit;
    private final String dimensionName;
    private Map<Integer, Map<String, String>> categoryList;

    public JsonStatDimension(String dimensionName, JsonNode categoryList) {
        this.jsonStatUnit = new ArrayList<>();
        this.dimensionName = dimensionName;
        this.categoryList = addToCategory(categoryList);
    }

    private Map<Integer, Map<String, String>> addToCategory(JsonNode categories) {
        Map<Integer, Map<String, String>> dimCat = new LinkedHashMap<>();
        Map<String, Object> index = categories("index", categories);
        Map<String, Object> label = categories("label", categories);
        if (dimensionName.equals("ContentsCode")) {
            Map<String, Object> unit = categories("unit", categories);
            for (String dimName : unit.keySet()) {
                String[] splitTest = unit.get(dimName).toString().split(",");
                String base = splitTest[0].replaceAll("\\{base=", "").trim();
                String stringDecimals = splitTest[1].replaceAll("decimals=|\\}", "").trim();
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

    private Map<String, Object> categories(String categoryName, JsonNode categories) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(categories.get(categoryName), new TypeReference<Map<String, Object>>() {
        });
    }

    public Map<Integer, Map<String, String>> getCategoryList() {
        return categoryList;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public List<JsonStatUnit> getJsonStatUnit() {
        return jsonStatUnit;
    }
}
