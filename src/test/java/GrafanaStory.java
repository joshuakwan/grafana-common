import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.appnexus.grafana.client.GrafanaClient;
import com.appnexus.grafana.client.models.*;
import com.appnexus.grafana.configuration.GrafanaConfiguration;
import com.appnexus.grafana.exceptions.GrafanaDashboardCouldNotDeleteException;
import com.appnexus.grafana.exceptions.GrafanaDashboardDoesNotExistException;
import com.appnexus.grafana.exceptions.GrafanaException;
import com.thoughtworks.gauge.BeforeSpec;
import com.thoughtworks.gauge.Step;

import grafana.Grafana;

public class GrafanaStory {

    Grafana grafana;
    String newDashboardUid;

    @BeforeSpec
    public void Setup() {
        String baseUrl = System.getenv("BASE_URL");
        String auth = System.getenv("AUTH");
        this.grafana = new Grafana(baseUrl, auth);
    }

    @Step("Create dashboard from YAML file <filename>")
    public void testYaml(String filename) throws IOException, GrafanaException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(filename);
        System.out.print(this.grafana.createDashboard(is));
    }


    @Step("Get the uid of dashboard <dashboardTitle>")
    public void testSearchDashboard(String dashboardTitle) {
        String uid = this.grafana.getDashboardUid(dashboardTitle, null);
        assertEquals(this.newDashboardUid, uid);
        System.out.println(uid);
    }

    @Step("Verify dashboard <dashboardTitle> is created")
    public void testGrafanaGetDashboardByName(String dashboardTitle) {
        GrafanaDashboard dashboard;
        try {
            dashboard = this.grafana.getDashboard(dashboardTitle, null);
            assertNotNull(dashboard);
            System.out.println(dashboard.dashboard());
        } catch (GrafanaDashboardDoesNotExistException e) {
            fail("Dashboard " + dashboardTitle + " not exist");
        } catch (GrafanaException e) {
            fail("Shit happens with Grafana");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Shit happens with I/O");
        }
    }

    @Step("Delete dashboard <dashboardName>")
    public void testDeleteGrafanaDashboard(String dashboardName) {
        try {
            String nameDeleted = this.grafana.deleteDashboardByName(dashboardName);
            assertNotNull(nameDeleted);
        } catch (GrafanaDashboardDoesNotExistException e) {
            fail("Dashboard " + dashboardName + " not exist");
        } catch (GrafanaDashboardCouldNotDeleteException e) {
            fail("Shit happens with Grafana");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Shit happens with I/O");
        }
    }
}