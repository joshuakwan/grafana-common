import grafana.beans.GrafanaMessage;
import grafana.beans.organization.GrafanaOrganization;
import grafana.beans.organization.OrganizationSuccessfulPost;
import grafana.beans.user.User;
import grafana.exceptions.GrafanaException;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;
import grafana.Grafana;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.fail;

public class OrganizationTests {
    Grafana adminGrafanaClient;
    Grafana grafanaClient;

    @BeforeScenario
    public void Setup() {
        String baseUrl = System.getenv("BASE_URL");
        String auth = System.getenv("AUTH");
        this.grafanaClient = new Grafana(baseUrl, auth);
        this.adminGrafanaClient = new Grafana(baseUrl, "admin", "admin");
    }

    @Step("Create organization <organizationName>")
    public void testCreateOrganization(String organizationName) {
        try {
            OrganizationSuccessfulPost resp = this.adminGrafanaClient.createOrganization(organizationName);
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
    public void testGetOrganization(String organizationName) {
        try {
            GrafanaOrganization resp = this.adminGrafanaClient.getOrganization(organizationName);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (GrafanaException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Step("Get current organization")
    public void testGetCurrentOrganization() {
        try {
            GrafanaOrganization resp = this.grafanaClient.getCurrentOrganization();
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (GrafanaException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Step("Get current organization users")
    public void testGetCurrentOrganizationUsers() {
        try {
            ArrayList<User> resp = this.grafanaClient.getCurrentOrganizationUsers();
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (GrafanaException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Step("Add user <loginOrEmail> to current organization as <role>")
    public void testAddUserToCurrentOrganization(String loginOrEmail, String role) {
        try {
            GrafanaMessage resp = this.grafanaClient.addUserToCurrentOrganization(loginOrEmail, role);
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
