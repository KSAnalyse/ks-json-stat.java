package no.ks.fiks.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import no.ks.fiks.JsonStat.JsonStat;
import no.ks.fiks.deserializer.JsonStatDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InsertTableService {

    private JsonStat jsonStat;
    private List<String> valuesStrings;
    private boolean shouldIterate = false;

    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(JsonStat.class, new JsonStatDeserializer());
        mapper.registerModule(module);
        jsonStat = mapper.readValue(new File("src/main/resources/resultatTest.json"), JsonStat.class);
        String[][] table = new String[jsonStat.getValues().size()][(jsonStat.getId().size() * 2)];


        Map<Integer, Integer> dimSizeIterator = new LinkedHashMap<>();
        int count = 0;
        for (int dimSize : jsonStat.getSize())
            dimSizeIterator.put(count++, 0);

        int dimension = 0;

        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < table[0].length; j++) {
                //System.out.println("dimension start: " + dimension + ", itertate start: " + shouldIterate);
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
                //System.out.println("dimension end: " + dimension + ", itertate end: " + shouldIterate);
                //System.out.println(split[0] + " " + split[1]);
                table[i][j++] = split[0];
                table[i][j] = split[1];
                if (dimension == dimSizeIterator.size()) {
                    dimension = 0;
                    shouldIterate = true;
                }
            }
        }

        for (int i = 0; i < 26; i++) {
            System.out.print(i + ":  \t");
            for (int j = 0; j < table[0].length; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
    }

    private Object[] keyAndValue(Map<String, String> test) {
        System.out.println(test);
        return test.entrySet().toArray();
    }

    private int increaseCategorySize(int dimPosition, int categorySize, Map<Integer, Integer> dimSizeIterator) {
        System.out.println("category size outside if " + categorySize + ", iterate: " + shouldIterate);
        int returnVal = dimSizeIterator.get(dimPosition);
        if (dimSizeIterator.get(categorySize) == jsonStat.getSize().get(categorySize)) {
            System.out.println("yes");
            shouldIterate = true;
            dimSizeIterator.put(categorySize, 0);
            return increaseCategorySize(dimPosition, categorySize - 1, dimSizeIterator);
        } else if ((dimSizeIterator.get(categorySize) < jsonStat.getSize().get(categorySize)) && shouldIterate) {
            //System.out.println("Category size in if: " + categorySize);
            dimSizeIterator.forEach((key, value) -> System.out.println(key + ": " + value));
            dimSizeIterator.put(categorySize, dimSizeIterator.get(categorySize) + 1);
        }
        shouldIterate = false;

        //System.out.println("\nReturn value: " + returnVal + ", dimension: " + jsonStat.getDimensions().get(dimPosition).getDimensionName() + "\n");
        return returnVal;
    }
}
