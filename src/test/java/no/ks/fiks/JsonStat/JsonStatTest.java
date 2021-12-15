package no.ks.fiks.JsonStat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import no.ks.fiks.deserializer.JsonStatDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class JsonStatTest {
    private static ObjectMapper mapper;
    private static SimpleModule module;
    private static String jsonOne;
    private static List<String> jsonList = new ArrayList<>();
    private static List<JsonStat> jsonStats = new ArrayList<>();
    private static JsonStat jsonStat = null;
    private static List<String> dimensionsTest = new ArrayList<>();
    Map<Integer, Map<String, String>> categoryMapTest = new LinkedHashMap<>();

    @BeforeAll
    static void setFasterXml() throws IOException {
        mapper = new ObjectMapper();
        module = new SimpleModule();
        module.addDeserializer(JsonStat.class, new JsonStatDeserializer());
        mapper.registerModule(module);
        jsonOne = Files.readString(Path.of("src/main/resources/resultatTest.json"));
        jsonList.add(jsonOne);
        jsonStats.add(mapper.readValue(jsonList.get(0), JsonStat.class));

        jsonStat = jsonStats.get(0);
        dimensionsTest.add("{\"label\":\"region\",\"category\":{\"index\":{\"EAK\":0,\"EAKUO\":1,\"3001\":2,\"EKG16\":3,\"EKG17\":4},\"label\":{\"EAK\":\"Landet\",\"EAKUO\":\"Landet uten Oslo\",\"3001\":\"Halden\",\"EKG16\":\"KOSTRA-gruppe 16\",\"EKG17\":\"KOSTRA-gruppe 17\"}},\"link\":{\"describedby\":[{\"extension\":{\"KOKkommuneregion0000\":\"urn:ssb:classification:klass:231\"}}]}}");
        dimensionsTest.add("{\"label\":\"regnskapsomfang\",\"category\":{\"index\":{\"A\":0,\"B\":1},\"label\":{\"A\":\"Kommuner/fylkeskommuner\",\"B\":\"Kommuner/fylkeskommuner eksl. foretak, IKS, og samarbeid.\"}},\"link\":{\"describedby\":[{\"extension\":{\"KOKregnskapsomfa0000\":\"urn:ssb:classification:klass:292\"}}]}}");
        dimensionsTest.add("{\"label\":\"funksjon\",\"category\":{\"index\":{\"100\":0,\"110\":1,\"120\":2,\"FGK8b\":3,\"FGK9\":4},\"label\":{\"100\":\"Politisk styring\",\"110\":\"Kontroll og revisjon\",\"120\":\"Administrasjon\",\"FGK8b\":\"Grunnskole\",\"FGK9\":\"Helse- og omsorg\"}},\"link\":{\"describedby\":[{\"extension\":{\"KOKfunksjon0000\":\"urn:ssb:classification:klass:277\"}}]}}");
        dimensionsTest.add("{\"label\":\"art\",\"category\":{\"index\":{\"AGD4\":0,\"AGD10\":1,\"AGD2\":2,\"AGD56\":3,\"AGD28\":4},\"label\":{\"AGD4\":\"Korrigerte brutto driftsutgifter på funksjon/tjenesteområde\",\"AGD10\":\"Brutto driftsutgifter på funksjon/tjenesteområde\",\"AGD2\":\"Netto driftsutgifter på funksjon/tjenesteområde\",\"AGD56\":\"Overføringer fra  egne foretak og IKS der (fylkes)kommunen selv er deltaker\",\"AGD28\":\"Finansinntekter og finanstransaksjoner ekskl. motpost avskrivninger\"}},\"link\":{\"describedby\":[{\"extension\":{\"KOKart0000\":\"urn:ssb:classification:klass:259\"}}]}}");
        dimensionsTest.add("{\"label\":\"statistikkvariabel\",\"category\":{\"index\":{\"KOSbelop0000\":0},\"label\":{\"KOSbelop0000\":\"Beløp (1000 kr)\"},\"unit\":{\"KOSbelop0000\":{\"base\":\"1000 kr\",\"decimals\":0}}},\"link\":{\"describedby\":[{\"extension\":{\"KOSbelop0000\":\"urn:ssb:contextvariable:common:116b3024-0ab0-4c7c-bf0a-973f61912516:127362:KOSbelop0000\"}}]}}");
        dimensionsTest.add("{\"label\":\"år\",\"category\":{\"index\":{\"2015\":0,\"2016\":1,\"2017\":2,\"2019\":3,\"2020\":4},\"label\":{\"2015\":\"2015\",\"2016\":\"2016\",\"2017\":\"2017\",\"2019\":\"2019\",\"2020\":\"2020\"}}}");

    }

    @Test
    public void getClassSetTest() {
        Assertions.assertEquals(jsonStat.getClassSet(), "dataset");
    }

    @Test
    public void getLabelTest() {
        Assertions.assertEquals(jsonStat.getLabel(), "12367: Detaljerte regnskapstall driftsregnskapet, etter region, regnskapsomfang, funksjon, art, statistikkvariabel og år");
    }

    @Test
    public void getSourceTest() {
        Assertions.assertEquals(jsonStat.getSource(), "Statistisk sentralbyrå");
    }

    @Test
    public void getUpdatedTest() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final LocalDateTime updated = LocalDateTime.parse("2021-06-13T22:00:00Z", dtf);
        Assertions.assertEquals(jsonStat.getUpdated(), updated);
    }

    @Test
    public void getIdTest() {
        List<String> id =
                Arrays.asList(
                        "KOKkommuneregion0000",
                        "KOKregnskapsomfa0000",
                        "KOKfunksjon0000",
                        "KOKart0000",
                        "ContentsCode",
                        "Tid"
                );
        Assertions.assertEquals(jsonStat.getId(), id);
    }

    @Test
    public void getSizeTest() {
        List<Integer> size = Arrays.asList(5, 2, 5, 5, 1, 5);
        Assertions.assertEquals(jsonStat.getSize(), size);
    }

    @Test
    public void getIdSizeTest() {
        List<String> id =
                Arrays.asList(
                        "KOKkommuneregion0000",
                        "KOKregnskapsomfa0000",
                        "KOKfunksjon0000",
                        "KOKart0000",
                        "ContentsCode",
                        "Tid"
                );
        List<Integer> size = Arrays.asList(5, 2, 5, 5, 1, 5);
        Map<String, Integer> idSize = new LinkedHashMap<>();
        for (int i = 0; i < size.size(); i++)
            idSize.put(id.get(i), size.get(i));

        Assertions.assertEquals(jsonStat.getIdSize(), idSize);
    }

    @Test
    public void getDimensionsNamesTest() {
        List<String> dimensionsNameTest = new ArrayList<>();
        List<String> id =
                Arrays.asList(
                        "KOKkommuneregion0000",
                        "KOKregnskapsomfa0000",
                        "KOKfunksjon0000",
                        "KOKart0000",
                        "ContentsCode",
                        "Tid"
                );
        dimensionsNameTest = jsonStat.getDimensions().stream().map(JsonStatDimension::getDimensionName).collect(Collectors.toList());
        Assertions.assertEquals(id, dimensionsNameTest);
    }

    private void setCategoryMapTest() {
        Map<String, String> index = new LinkedHashMap<>();
        index.put("EAK", "Landet");
        index.put("EAKUO", "Landet uten Oslo");
        index.put("3001", "Halden");
        index.put("EKG16", "KOSTRA-gruppe 16");
        index.put("EKG17", "KOSTRA-gruppe 17");
        int count = 0;
        for (String key : index.keySet()) {
            Map<String, String> newLabel = new LinkedHashMap<>();
            newLabel.put(key, index.get(key));
            categoryMapTest.put(count++, newLabel);
        }
    }

    @Test
    public void getDimensionsCategoryTest() {
        setCategoryMapTest();
        Assertions.assertEquals(categoryMapTest, jsonStat.getDimensions().get(0).getCategoryList());
    }

    @Test
    public void getJsonStatUnit() {
        JsonStatUnit jsonStatUnitTest = new JsonStatUnit("KOSbelop0000", "1000 kr", 0);
        JsonStatUnit jsonStatUnitActual = null;
        for (JsonStatDimension jsonStatDimension : jsonStat.getDimensions()) {
            if (jsonStatDimension.getDimensionName().equals("ContentsCode")) {
                jsonStatUnitActual = jsonStatDimension.getJsonStatUnit().get(0);
            }
        }
        assert jsonStatUnitActual != null;
        String dimNameActual = jsonStatUnitActual.getDimensionName();
        String dimNameTest = jsonStatUnitTest.getDimensionName();
        String baseActual = jsonStatUnitActual.getBase();
        String baseTest = jsonStatUnitTest.getBase();
        int decimalActual = jsonStatUnitActual.getDecimals();
        int decimalTest = jsonStatUnitTest.getDecimals();
        Assertions.assertAll("Should return JsonStatUnit variables",
                () -> Assertions.assertEquals(dimNameTest, dimNameActual),
                () -> Assertions.assertEquals(baseTest, baseActual),
                () -> Assertions.assertEquals(decimalTest, decimalActual));
    }

    @Test
    public void getValuesTest() {
        Assertions.assertAll("Should return the values of value array in every 10th position and first for the first 100.",
                () -> Assertions.assertEquals(BigDecimal.valueOf(1943974), jsonStat.getValues().get(0)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(1944078), jsonStat.getValues().get(9)),
                () -> Assertions.assertNull(jsonStat.getValues().get(19)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(429745), jsonStat.getValues().get(29)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(516230), jsonStat.getValues().get(39)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(8333), jsonStat.getValues().get(49)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(22380510), jsonStat.getValues().get(59)),
                () -> Assertions.assertNull(jsonStat.getValues().get(69)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(84317072), jsonStat.getValues().get(79)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(78044855), jsonStat.getValues().get(89)),
                () -> Assertions.assertEquals(BigDecimal.valueOf(553097), jsonStat.getValues().get(99)));
    }

    @Test
    public void getStatusTest() {
        Assertions.assertAll("Should return status of value positions",
                () -> Assertions.assertEquals(":", jsonStat.getStatus().get("15")),
                () -> Assertions.assertEquals(":", jsonStat.getStatus().get("44")),
                () -> Assertions.assertEquals(":", jsonStat.getStatus().get("94")),
                () -> Assertions.assertEquals(":", jsonStat.getStatus().get("269")),
                () -> Assertions.assertEquals(":", jsonStat.getStatus().get("319")),
                () -> Assertions.assertEquals(":", jsonStat.getStatus().get("369")),
                () -> Assertions.assertEquals(".", jsonStat.getStatus().get("511")),
                () -> Assertions.assertEquals(".", jsonStat.getStatus().get("522")),
                () -> Assertions.assertEquals(".", jsonStat.getStatus().get("535")),
                () -> Assertions.assertEquals(".", jsonStat.getStatus().get("575")));
    }

    @Test
    public void getRoleTest() {
        Assertions.assertAll("Should return time variable and metric variable.",
                () -> Assertions.assertEquals("Tid", jsonStat.getRole().get("time")),
                () -> Assertions.assertEquals("ContentsCode", jsonStat.getRole().get("metric")));
    }
}