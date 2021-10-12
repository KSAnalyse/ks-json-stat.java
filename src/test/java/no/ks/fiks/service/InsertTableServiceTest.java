package no.ks.fiks.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import no.ks.fiks.JsonStat.JsonStat;
import no.ks.fiks.Service.InsertTableService;
import no.ks.fiks.deserializer.JsonStatDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InsertTableServiceTest {

    private static final List<String> jsonList = new ArrayList<>();
    private static final List<JsonStat> jsonStats = new ArrayList<>();
    private static List<Map<String[], BigDecimal>> structuredList = new ArrayList<>();
    private static JsonStat jsonStat = null;
    private static InsertTableService insertTableService = null;

    @BeforeAll
    static void setFasterXml() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(JsonStat.class, new JsonStatDeserializer());
        mapper.registerModule(module);
        String jsonOne = Files.readString(Path.of("src/main/resources/resultatTest.json"));
        jsonList.add(jsonOne);
        jsonStats.add(mapper.readValue(jsonList.get(0), JsonStat.class));

        jsonStat = jsonStats.get(0);
        insertTableService = new InsertTableService();
    }

    @Test
    public void getBiggestDecimalTest() throws IOException {
        structuredList = insertTableService.structureJsonStatTable(jsonList);
        Assertions.assertEquals(0, insertTableService.getBiggestDecimal());
    }

    @Test
    public void getJsonStatTest() {
        Assertions.assertAll("Random tests for getJsonStat()",
                () -> Assertions.assertEquals(jsonStat.getValues().get(50), insertTableService.getJsonStat().get(0).getValues().get(50)),
                () -> Assertions.assertEquals(jsonStat.getIdSize(), insertTableService.getJsonStat().get(0).getIdSize()),
                () -> Assertions.assertEquals(jsonStat.getUpdated(), insertTableService.getJsonStat().get(0).getUpdated()));
    }

    @Test
    public void structureJsonStatTableKeyTest() throws IOException {
        structuredList = insertTableService.structureJsonStatTable(jsonList);
        String[] firstRow = {"EAK", "Landet", "A", "Kommuner/fylkeskommuner", "100", "Politisk styring", "AGD4",
                "Korrigerte brutto driftsutgifter på funksjon/tjenesteområde", "KOSbelop0000", "Beløp (1000 kr)",
                "2015", "2015"};
        String[] sixthRoW = {"EAK", "Landet", "A", "Kommuner/fylkeskommuner", "100", "Politisk styring", "AGD10",
                "Brutto driftsutgifter på funksjon/tjenesteområde", "KOSbelop0000", "Beløp (1000 kr)",
                "2015", "2015"};

        String[] sixHundredAndEightyFifthRow = {"3001", "Halden", "B", "Kommuner/fylkeskommuner eksl. foretak, IKS, og samarbeid.",
                "120", "Administrasjon", "AGD10", "Brutto driftsutgifter på funksjon/tjenesteområde", "KOSbelop0000",
                "Beløp (1000 kr)", "2020", "2020"};
        String[] testOne = (String[]) structuredList.get(0).keySet().toArray()[0];
        String[] testTwo = (String[]) structuredList.get(5).keySet().toArray()[0];
        String[] testThree = (String[]) structuredList.get(684).keySet().toArray()[0];
        Assertions.assertAll("Checking the keys in the Map",
                () -> Assertions.assertArrayEquals(firstRow, testOne),
                () -> Assertions.assertArrayEquals(sixthRoW, testTwo),
                () -> Assertions.assertArrayEquals(sixHundredAndEightyFifthRow, testThree));
    }

    @Test
    public void structuredJsonStatTableValueTest() throws IOException {
        structuredList = insertTableService.structureJsonStatTable(jsonList);
        String[] testOne = (String[]) structuredList.get(0).keySet().toArray()[0];
        String[] testTwo = (String[]) structuredList.get(5).keySet().toArray()[0];
        String[] testThree = (String[]) structuredList.get(684).keySet().toArray()[0];

        Assertions.assertAll("Check the values in the map",
                () -> Assertions.assertEquals(BigDecimal.valueOf(1943974), structuredList.get(0).get(testOne)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(2076058), structuredList.get(5).get(testTwo)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(101357), structuredList.get(684).get(testThree)));
    }
}
