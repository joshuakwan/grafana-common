import grafana.beans.dashboard.DashboardSuccessfulPost;
import grafana.beans.GrafanaDashboard;
import grafana.exceptions.GrafanaDashboardCouldNotDeleteException;
import grafana.exceptions.GrafanaDashboardDoesNotExistException;
import grafana.exceptions.GrafanaException;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;
import grafana.Grafana;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DashboardTests {

    Grafana grafana;
    String newDashboardUid;

    @BeforeScenario
    public void Setup() {
        String baseUrl = System.getenv("BASE_URL");
        String auth = System.getenv("AUTH");
        this.grafana = new Grafana(baseUrl, auth);
    }

    @Step("Create dashboard from YAML file <filename>")
    public void testCreateDashboard(String filename)
            throws IOException, GrafanaException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(filename);
        DashboardSuccessfulPost resp = this.grafana.createDashboard(is);
        assertNotNull(resp);
        this.newDashboardUid = resp.uid();
        System.out.print(resp);
    }

    @Step("Get the uid of dashboard <dashboardTitle>")
    public void testSearchDashboard(String dashboardTitle) {
        String uid = null;
        try {
            uid = this.grafana.getDashboardUid(dashboardTitle);
        } catch (GrafanaDashboardDoesNotExistException e) {
            e.printStackTrace();
        }
        assertNotNull(uid);
        System.out.println(uid);
    }

    @Step("Get the content of dashboard <dashboardTitle>")
    public void testGetDashboard(String dashboardTitle) {
        try {
            GrafanaDashboard dashboard = this.grafana.getDashboard(dashboardTitle);
            assertNotNull(dashboard);
            System.out.println(dashboard);
        } catch (GrafanaDashboardDoesNotExistException e) {
            e.printStackTrace();
            fail();
        } catch (GrafanaException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Step("Update dashboard <dashboardTitle> from YAML file <filename>")
    public void testUpdateDashboard(String dashboardTitle, String filename)
            throws IOException, GrafanaException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(filename);
        DashboardSuccessfulPost resp = this.grafana.updateDashboard(dashboardTitle,is);
        assertNotNull(resp);
        System.out.print(resp);
    }

    @Step("Delete dashboard <dashboardTitle>")
    public void testDeleteGrafanaDashboard(String dashboardTitle) {
        try {
            String nameDeleted = this.grafana.deleteDashboard(dashboardTitle);
            assertEquals(dashboardTitle, nameDeleted);
            System.out.println(nameDeleted);
        } catch (GrafanaDashboardDoesNotExistException e) {
            fail("Dashboard " + dashboardTitle + " not exist");
        } catch (GrafanaDashboardCouldNotDeleteException e) {
            fail("Shit happens with Grafana");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Shit happens with I/O");
        }
    }
}