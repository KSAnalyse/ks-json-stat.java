package no.ks.fiks.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import no.ks.fiks.JsonStat.JsonStat;
import no.ks.fiks.deserializer.JsonStatDeserializer;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class InsertTableService {

    private JsonStat jsonStat;
    private boolean shouldIterate = false;
    private List<Map<String[], BigDecimal>> sortedJsonStat = new ArrayList<>();

    public void structureJsonStatTable(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(JsonStat.class, new JsonStatDeserializer());
        mapper.registerModule(module);
        jsonStat = mapper.readValue(json, JsonStat.class);
        String[][] table = new String[jsonStat.getValues().size()][(jsonStat.getId().size() * 2)];


        Map<Integer, Integer> dimSizeIterator = new LinkedHashMap<>();
        int count = 0;
        for (int dimSize : jsonStat.getSize())
            dimSizeIterator.put(count++, 0);

        int dimension = 0;

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                String[] split = Arrays.toString(
                                keyAndValue(
                                        jsonStat.getDimensions()
                                                .get(dimension)
                                                .getCategoryList()
                                                .get(increaseCategorySize(dimension++, jsonStat.getSize().size() - 1, dimSizeIterator))))
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
        sortedJsonStat = combineTableWithValues(table);
        for (Map<String[], BigDecimal> bigDecimalMap :
                sortedJsonStat) {
            bigDecimalMap.forEach((key, value) -> System.out.println(Arrays.toString(key) + " " + value));
        }
    }

    private List<Map<String[], BigDecimal>> combineTableWithValues(String[][] table) {
        List<Map<String[], BigDecimal>> result = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            Map<String[], BigDecimal> combineCategoryWithValues = new LinkedHashMap<>();
            combineCategoryWithValues.put(table[i], jsonStat.getValues().get(i));
            result.add(combineCategoryWithValues);
        }
        return result;
    }

    private Object[] keyAndValue(Map<String, String> stringMap) {
        return stringMap.entrySet().toArray();
    }

    private int increaseCategorySize(int dimPosition, int categorySize, Map<Integer, Integer> dimSizeIterator) {
        int jsSize = jsonStat.getSize().get(categorySize);

        if ((dimSizeIterator.get(categorySize) < jsSize) && shouldIterate) {
            shouldIterate = false;
            dimSizeIterator.put(categorySize, dimSizeIterator.get(categorySize) + 1);
        }
        if (dimSizeIterator.get(categorySize) == jsSize) {
            shouldIterate = true;
            dimSizeIterator.put(categorySize, 0);
            return increaseCategorySize(dimPosition, categorySize - 1, dimSizeIterator);
        }
        return dimSizeIterator.get(dimPosition);
    }

    public List<Map<String[], BigDecimal>> getSortedJsonStat() {
        return sortedJsonStat;
    }
}
