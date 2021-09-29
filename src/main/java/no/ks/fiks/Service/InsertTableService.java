package no.ks.fiks.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import no.ks.fiks.JsonStat.JsonStat;
import no.ks.fiks.JsonStat.JsonStatDimension;
import no.ks.fiks.JsonStat.JsonStatUnit;
import no.ks.fiks.deserializer.JsonStatDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class InsertTableService {

    private final List<JsonStat> jsonStat = new ArrayList<>();
    private boolean shouldIterate = false;
    private final List<Map<String[], BigDecimal>> sortedJsonStat = new ArrayList<>();
    private int biggestDecimal = 0;

    public List<Map<String[], BigDecimal>> structureJsonStatTable(List<String> jsonList) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(JsonStat.class, new JsonStatDeserializer());
        mapper.registerModule(module);
        for (String json : jsonList) {
            JsonStat mapperValue = mapper.readValue(json, JsonStat.class);
            jsonStat.add(mapperValue);
        }

        getHighestDecimal();

        for (JsonStat stat : jsonStat) {
            String[][] table = new String[stat.getValues().size()][(stat.getId().size() * 2)];
            shouldIterate = false;
            Map<Integer, Integer> dimSizeIterator = new LinkedHashMap<>();
            int count = 0;
            for (int dimSize : stat.getSize())
                dimSizeIterator.put(count++, 0);

            int dimension = 0;

            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[0].length; j++) {
                    String[] split = Arrays.toString(
                                    keyAndValue(
                                            stat.getDimensions()
                                                    .get(dimension)
                                                    .getCategoryList()
                                                    .get(increaseCategorySize(dimension++, stat.getSize().size() - 1, dimSizeIterator, stat))))
                            .replaceAll("\\[|\\]", "")
                            .split("=");

                    if (dimension == 1)
                        shouldIterate = false;

                    table[i][j++] = split[0];
                    table[i][j] = split[1];

                    if (dimension == dimSizeIterator.size()) {
                        dimension = 0;
                        shouldIterate = true;
                    }
                }
            }
            combineTableWithValues(table, stat);
        }
        return sortedJsonStat;
    }

    private void combineTableWithValues(String[][] table, JsonStat stat) {
        for (int i = 0; i < table.length; i++) {
            Map<String[], BigDecimal> combineCategoryWithValues = new LinkedHashMap<>();
            combineCategoryWithValues.put(table[i], stat.getValues().get(i));
            sortedJsonStat.add(combineCategoryWithValues);
        }
    }

    private Object[] keyAndValue(Map<String, String> stringMap) {
        return stringMap.entrySet().toArray();
    }

    private int increaseCategorySize(int dimPosition, int categorySize, Map<Integer, Integer> dimSizeIterator, JsonStat stat) {
        int jsSize = stat.getSize().get(categorySize);

        if ((dimSizeIterator.get(categorySize) < jsSize) && shouldIterate) {
            shouldIterate = false;
            dimSizeIterator.put(categorySize, dimSizeIterator.get(categorySize) + 1);
        }
        if (dimSizeIterator.get(categorySize) == jsSize) {
            shouldIterate = true;
            dimSizeIterator.put(categorySize, 0);
            return increaseCategorySize(dimPosition, categorySize - 1, dimSizeIterator, stat);
        }
        return dimSizeIterator.get(dimPosition);
    }

    public List<JsonStat> getJsonStat() {
        return jsonStat;
    }

    public int getBiggestDecimal() {
        return biggestDecimal;
    }

    private void getHighestDecimal() {
        getJsonStat().stream()
                .flatMap(stat -> stat.getDimensions().stream())
                .flatMap(jsonStatDimension -> jsonStatDimension.getJsonStatUnit().stream())
                .filter(jsonStatUnit -> !jsonStatUnit.getDimensionName().isEmpty())
                .forEach(jsonStatUnit -> biggestDecimal = Math.max(jsonStatUnit.getDecimals(), biggestDecimal));
    }
}
