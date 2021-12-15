package no.ks.fiks.deserializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import no.ks.fiks.JsonStat.JsonStat;
import no.ks.fiks.JsonStat.JsonStatTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonStatTestDeserializerTest {
    private static ObjectMapper mapper;
    private static SimpleModule module;
    private static String json;
    private static List<String> jsonList = new ArrayList<>();
    private static List<JsonStat> jsonStats = new ArrayList<>();
    private static JsonStat jsonStat = null;
    @BeforeAll
    static void setFasterXml() throws IOException {
        mapper = new ObjectMapper();
        module = new SimpleModule();
        module.addDeserializer(JsonStat.class, new JsonStatDeserializer());
        mapper.registerModule(module);
        json = Files.readString(Path.of("src/main/resources/resultatTest3.json"));
        jsonList.add(json);
        jsonStats.add(mapper.readValue(jsonList.get(0), JsonStat.class));
        jsonStat = jsonStats.get(0);
    }

    @Test
    public void deserializeTest()  {
        Assertions.assertNotNull(jsonStat);
    }
}
