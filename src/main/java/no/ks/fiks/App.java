package no.ks.fiks;


import com.fasterxml.jackson.core.JsonProcessingException;
import no.ks.fiks.Service.InsertTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;

//@SpringBootApplication
public class App /*implements CommandLineRunner */{

    private static ConfigurableApplicationContext ctx;
    /*@Autowired
    private JdbcTemplate jdbcTemplate;*/

    public static void main(String[] args) throws IOException {
//        ctx = new SpringApplicationBuilder(App.class).web(WebApplicationType.NONE).run();
        new InsertTableService().test();
    }

    /*@Override
    public void run(String... args) {
        new InsertTableService().test();

        /*String sqlSelectStatement = "SELECT * FROM Tabellinformasjon";
        List<String> allTables = jdbcTemplate.query(sqlSelectStatement, (rs, rowNum) -> rs.getString(2));
        ResultSetExtractor<HashMap<String, String>> rse = rs -> {
            HashMap<String, String> retVal = new HashMap<>();
            while (rs.next()) {
                retVal.put(rs.getString(2), rs.getString(4));
            }
            return retVal;
        };

        HashMap<String, String> createTableFilter = jdbcTemplate.query(sqlSelectStatement + " WHERE [Lag Tabell Filter] NOT LIKE 'NULL'", rse);


        for (String table :
                allTables) {
            String sqlCreateTableStatement = "EXEC [dbo].[SSB_Lag_Tabell_v2]@SSBTableCode = '" + table + "'";
            for (String key :
                    createTableFilter.keySet()) {
                String tableFilter = createTableFilter.get(key);

                if (key.equals(table)) {
                    sqlCreateTableStatement += ",@Filters= '" + tableFilter + "'";
                }
            }
            Thread.sleep(5000);
            jdbcTemplate.execute("DROP TABLE IF EXISTS SSB_" + table);
            jdbcTemplate.execute(sqlCreateTableStatement);
            System.out.println(sqlCreateTableStatement);
        }
        int exitCode = SpringApplication.exit(ctx, new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                return 0;
            }
        });*/
    //}
}
