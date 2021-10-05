package no.ks.fiks.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import no.ks.fiks.JsonStat.JsonStat;
import no.ks.fiks.deserializer.JsonStatDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * <h3>InsertTableService</h3>
 * <p>
 * This class handles the creation of jsonStat objects, sorting and structuring of the JSON-Stat query results.
 *
 * @see #structureJsonStatTable(List)
 * @see #increaseCategorySize(int, int, Map, JsonStat)
 * @see #combineTableWithValues(String[][], JsonStat)
 * @see #keyAndValue(Map)
 */
public class InsertTableService {

    private final List<JsonStat> jsonStat = new ArrayList<>();
    private final List<Map<String[], BigDecimal>> structuredJsonStat = new ArrayList<>();
    private boolean shouldIterate = false;
    private int biggestDecimal = 0;

    /**
     * <h3>structureJsonStatTable</h3>
     * <p>
     * This is the main method if this class that handles reading from the JSON-Stat String, adding it to a object list.
     * Then reading from those objects, structuring it so that each row matches the correct index og values.
     * <p>
     * Quick explanation of how JSON-Stat is structured:
     * The first index of each dimension, is the first index of the values array. The first index each dimension and the
     * second index of the last dimension is the second value. So if dimension sizes are [5, 5, 5] it will go like this
     * 0-0-0 = values[0], 0-0-1 = values[1] ... 0-0-4 = values[4], 0-1-0 = values[5], 0-1-1 = values[6] and so on and on.
     * <p>
     * So to solve this, it first uses the Integer dimension variable to iterate through all the metadata dimensions.
     * Then it uses dimSizeIterator to track which dimension index we are on. By using {@link #increaseCategorySize}
     * we get the correct sequence of dimensions.
     *
     * @param jsonList The list of table query results.
     * @return Returns a List of the Map of combined dimension rows and values.
     * @see #combineTableWithValues(String[][], JsonStat) This method combines all the structured data in a global List.
     */
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
                    String[] split = Arrays.toString(keyAndValue(stat.getDimensions().get(dimension)
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
        return structuredJsonStat;
    }

    /**
     * <h3>combineTableWithValues</h3>
     * <p>
     * This method combines the 2D array of the metadata structure with the values in a map and adds that map to a list.
     *
     * @param table This array is the query result sorted and structured.
     * @param stat  This is the JSON-Stat object
     */
    private void combineTableWithValues(String[][] table, JsonStat stat) {
        for (int i = 0; i < table.length; i++) {
            Map<String[], BigDecimal> combineCategoryWithValues = new LinkedHashMap<>();
            combineCategoryWithValues.put(table[i], stat.getValues().get(i));
            structuredJsonStat.add(combineCategoryWithValues);
        }
    }

    /**
     * <h3>keyAndValue</h3>
     * <p>
     * This method splits up the map and returns it as an array.
     *
     * @param stringMap This map is of the category {@link #structureJsonStatTable(List)} is in when called.
     * @return Returns the key and value of the current key of the map as an Object[].
     */
    private Object[] keyAndValue(Map<String, String> stringMap) {
        return stringMap.entrySet().toArray();
    }

    /**
     * <h3>increaseCategorySize</h3>
     * <p>
     * This method tracks when dimSizeIterator should increase it's value for given dimension we are in.
     * <p>
     * When {@link #shouldIterate} is true it increases the value of the last key in the Map by 1. If by doing that the
     * last value is now equal to the size of the last dimension, it sets it to zero and recursively calls itself again,
     * but with categorySize - 1 and sets {@link #shouldIterate} to true again so that it can increase the dimension
     * by 1. Why like this? Read description for JSON-Stat in {@link #structureJsonStatTable(List)}
     *
     * @param dimPosition     The position of the dimension we are in.
     * @param categorySize    The position of the last category as default, subtract one for each time this method is
     *                        called recursively.
     * @param dimSizeIterator The map which tracks how far we have come in the structuring of the dimensions.
     * @param stat            The jsonStat object
     * @return Returns the dimension index.
     */
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

    /**
     * <h3>getJsonStat</h3>
     *
     * @return Returns the JSON-Stat list of objects.
     */
    public List<JsonStat> getJsonStat() {
        return jsonStat;
    }

    /**
     * <h3>getBiggestDecimal</h3>
     *
     * @return Returns the biggest decimal in the query results.
     */

    public int getBiggestDecimal() {
        return biggestDecimal;
    }

    /**
     * <h3>getHighestDecimal</h3>
     * <p>
     * This method goes through the jsonStatUnit object to find the largest decimal value and sets it to {@link #biggestDecimal}.
     */
    private void getHighestDecimal() {
        getJsonStat().stream()
                .flatMap(stat -> stat.getDimensions().stream())
                .flatMap(jsonStatDimension -> jsonStatDimension.getJsonStatUnit().stream())
                .filter(jsonStatUnit -> !jsonStatUnit.getDimensionName().isEmpty())
                .forEach(jsonStatUnit -> biggestDecimal = Math.max(jsonStatUnit.getDecimals(), biggestDecimal));
    }
}
