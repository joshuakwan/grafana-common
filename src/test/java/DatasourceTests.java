import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;
import grafana.Grafana;
import grafana.beans.datasource.Datasource;
import grafana.beans.datasource.DatasourceSuccessfulCreate;
import grafana.beans.messages.GrafanaMessage;
import grafana.exceptions.GrafanaException;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DatasourceTests {
    Grafana grafana;

    @BeforeScenario
    public void Setup() {
        String baseUrl = System.getenv("BASE_URL");
        String auth = System.getenv("AUTH");
        this.grafana = new Grafana(baseUrl, auth);
    }

    @Step("Get all datasources")
    public void testGetAllDatasources() {
        try {
            List<Datasource> datasources = this.grafana.getAllDatasources();
            assertNotNull(datasources);
            System.out.println(datasources);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (GrafanaException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Step("Create a new <type> datasource <name> from <url> with <access> mode")
    public void testCreateDatasource(String type, String name, String url, String access) {
        try {
            DatasourceSuccessfulCreate message = this.grafana.createDatasource(name, type, url, access);
            assertNotNull(message);
            assertEquals(name, message.name());
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (GrafanaException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Step("Delete datasource <name>")
    public void testDeleteDatasource(String name){
        GrafanaMessage message = null;
        try {
            message = this.grafana.deleteDatasource(name);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (GrafanaException e) {
            e.printStackTrace();
            fail();
        }
        System.out.println(message);
    }
}
