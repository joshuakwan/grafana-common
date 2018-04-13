import grafana.beans.organization.GrafanaOrganization;
import grafana.beans.organization.OrganizationSuccessfulPost;
import grafana.exceptions.GrafanaException;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;
import grafana.Grafana;

import java.io.IOException;

import static org.junit.Assert.fail;

public class OrganizationTests {
    Grafana grafana;

    @BeforeScenario
    public void Setup() {
        String baseUrl = System.getenv("BASE_URL");
        String auth = System.getenv("AUTH");
        // this.grafana = new Grafana(baseUrl, auth);
        this.grafana = new Grafana(baseUrl, "admin", "admin");
    }

    @Step("Create organization <organizationName>")
    public void testCreateOrganization(String organizationName){
        try {
            OrganizationSuccessfulPost resp = this.grafana.createOrganization(organizationName);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (GrafanaException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Step("Get organization <organizationName>")
    public void testGetOrganization(String organizationName){
        try {
            GrafanaOrganization resp = this.grafana.getOrganization(organizationName);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (GrafanaException e) {
            e.printStackTrace();
            fail();
        }

    }
}
