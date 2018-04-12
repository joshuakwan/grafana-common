package grafana;

import com.appnexus.grafana.client.GrafanaClient;
import com.appnexus.grafana.client.models.*;
import com.appnexus.grafana.configuration.GrafanaConfiguration;
import com.appnexus.grafana.exceptions.GrafanaDashboardCouldNotDeleteException;
import com.appnexus.grafana.exceptions.GrafanaDashboardDoesNotExistException;
import com.appnexus.grafana.exceptions.GrafanaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import grafana.beans.Panel;
import grafana.beans.Target;
import grafana.beans.Template;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Grafana {
    private GrafanaClient client;

    /**
     * Initialize a Grafana API object
     *
     * @param baseUrl base url of grafana
     * @param auth    grafana auth string
     */
    public Grafana(String baseUrl, String auth) {
        GrafanaConfiguration config = new GrafanaConfiguration().host(baseUrl).apiKey(auth);
        this.client = new GrafanaClient(config);
    }

    /**
     * Initialize a Grafana API object with the admin authority
     *
     * @param baseUrl       base url of grafana
     * @param adminUsername the admin username
     * @param adminPassword the admin password
     */
    public Grafana(String baseUrl, String adminUsername, String adminPassword) {
        GrafanaConfiguration config = new GrafanaConfiguration().host(baseUrl);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originReq = chain.request();
                Request.Builder builder = originReq.newBuilder().header("Authorization",
                        Credentials.basic(adminUsername, adminPassword));
                Request newReq = builder.build();
                return chain.proceed(newReq);
            }
        }).build();
        this.client = new GrafanaClient(config, okHttpClient);
    }

    public OrganizationSuccessfulPost createOrganization(String organizationName)
            throws IOException, GrafanaException {
        GrafanaOrganization organization = new GrafanaOrganization().name(organizationName);
        return this.client.createOrganization(organization);
    }

    public GrafanaOrganization getOrganization(String organizationName)
            throws IOException, GrafanaException {
        return this.client.getOrganization(organizationName);
    }

    /**
     * Create a new dashboard from a YAML input stream
     *
     * @param yamlStream the YAML input stream
     * @return the response object that contains essential information
     * @throws IOException
     * @throws GrafanaException
     */
    public DashboardSuccessfulPost createDashboard(InputStream yamlStream)
            throws IOException, GrafanaException {
        grafana.beans.Dashboard data = getYamlData(yamlStream);
        GrafanaDashboard grafanaDashboard = buildGrafanaDashboard(data);
        return this.client.createDashboard(grafanaDashboard);
    }

    /**
     * Delete a dashboard by its title and folder
     *
     * @param dashboardTitle  the dashboard title
     * @param dashboardFolder the folder name which contains the dashboard
     * @return the title of the deleted dashboard
     * @throws GrafanaDashboardDoesNotExistException
     * @throws GrafanaDashboardCouldNotDeleteException
     * @throws IOException
     */
    public String deleteDashboard(String dashboardTitle, String dashboardFolder)
            throws GrafanaDashboardDoesNotExistException, GrafanaDashboardCouldNotDeleteException, IOException {
        String uid = this.getDashboardUid(dashboardTitle, dashboardFolder);
        return this.client.deleteDashboard(uid);
    }

    /**
     * Delete a dashboard by its title
     *
     * @param dashboardTitle the dashboard title
     * @return the title of the deleted dashboard
     * @throws GrafanaDashboardDoesNotExistException
     * @throws GrafanaDashboardCouldNotDeleteException
     * @throws IOException
     */
    public String deleteDashboard(String dashboardTitle)
            throws GrafanaDashboardDoesNotExistException, GrafanaDashboardCouldNotDeleteException, IOException {
        String uid = this.getDashboardUid(dashboardTitle);
        return this.client.deleteDashboard(uid);
    }

    /**
     * Update a dashboard with a YAML input stream
     *
     * @param dashboardTitle the title of the dashboard to be updated
     * @param yamlStream     the YAML stream which contains updated configuration
     * @return the response object that contains essential information
     * @throws IOException
     * @throws GrafanaException
     */
    public DashboardSuccessfulPost updateDashboard(String dashboardTitle, InputStream yamlStream)
            throws IOException, GrafanaException {
        return this.updateDashboard(dashboardTitle, null, yamlStream);
    }

    /**
     * Update a dashboard with a YAML input stream
     *
     * @param dashboardTitle  the title of the dashboard to be updated
     * @param dashboardFolder the name of the folder which contains the dashboard to be updated
     * @param yamlStream      the YAML stream which contains updated configuration
     * @return the response object that contains essential information
     * @throws IOException
     * @throws GrafanaException
     */
    public DashboardSuccessfulPost updateDashboard(String dashboardTitle, String dashboardFolder, InputStream yamlStream)
            throws IOException, GrafanaException {
        // 1. get existing dashboard
        GrafanaDashboard existingDashboard = this.getDashboard(dashboardTitle, dashboardFolder);

        // 2. build new dashboard
        grafana.beans.Dashboard data = getYamlData(yamlStream);
        GrafanaDashboard newDashboard = buildGrafanaDashboard(data);

        // 3. make the update
        // 3.1. specify the version
        newDashboard.dashboard().version(existingDashboard.dashboard().version());
        // 3.2. set uid so the dashboard won't be created again
        newDashboard.dashboard().uid(existingDashboard.dashboard().uid());

        return this.client.updateDashboard(newDashboard);
    }

    /**
     * Get a dashboard by its title and folder name
     *
     * @param dashboardTitle  the dashboard title
     * @param dashboardFolder the name of the folder which contains the dashboard
     * @return the dashboard object
     * @throws IOException
     * @throws GrafanaException
     */
    public GrafanaDashboard getDashboard(String dashboardTitle, String dashboardFolder)
            throws IOException, GrafanaException {
        String uid = this.getDashboardUid(dashboardTitle, dashboardFolder);
        return this.client.getDashboard(uid);
    }

    /**
     * Get a dashboard by its title
     *
     * @param dashboardTitle the dashboard title
     * @return the dashboard object
     * @throws IOException
     * @throws GrafanaException
     */
    public GrafanaDashboard getDashboard(String dashboardTitle)
            throws IOException, GrafanaException {
        return this.getDashboard(dashboardTitle, null);
    }

    /**
     * Get the uid of a dashboard
     *
     * @param dashboardTitle  the dashboard title
     * @param dashboardFolder the name of the folder which contains the dashboard
     * @return the dashboard uid
     */
    public String getDashboardUid(String dashboardTitle, String dashboardFolder) {
        return this.client.searchDashboard(dashboardTitle, dashboardFolder);
    }

    /**
     * Get the uid of a dashboard
     *
     * @param dashboardTitle the dashboard title
     * @return the dashboard uid
     */
    public String getDashboardUid(String dashboardTitle) {
        return this.client.searchDashboard(dashboardTitle, null);
    }

    private grafana.beans.Dashboard getYamlData(InputStream yamlStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(yamlStream, grafana.beans.Dashboard.class);
    }

    private static <T> T getValue(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    private GrafanaDashboard buildGrafanaDashboard(grafana.beans.Dashboard data) {
        Dashboard dashboard = new Dashboard();
        dashboard.title(data.getTitle());
        dashboard.refresh(data.getRefresh());
        dashboard.schemaVersion(getValue(data.getSchemaVersion(), 1));
        DashboardTime time = new DashboardTime().from(data.getTime().getFrom()).to(data.getTime().getTo());
        dashboard.time(time);

        ArrayList<DashboardTemplateList> templateLists = new ArrayList<>();
        for (Template templateData : data.getTemplating()) {
            DashboardTemplateList templateList = new DashboardTemplateList();
            templateList.name(templateData.getName());
            templateList.type(templateData.getType());
            templateList.datasource(templateData.getDatasource());
            templateList.query(templateData.getQuery());
            templateList.refresh(templateData.getRefresh());
            templateList.multi(templateData.getMulti());
            templateList.includeAll(templateData.getIncludeAll());

            templateLists.add(templateList);
        }

        DashboardTemplate templating = new DashboardTemplate().list(templateLists);
        dashboard.templating(templating);

        ArrayList<DashboardPanel> panels = new ArrayList<>();
        for (Panel panelData : data.getPanels()) {
            DashboardPanel panel = new DashboardPanel();
            panel.title(panelData.getTitle());
            panel.datasource(panelData.getDatasource());

            panel.format(panelData.getFormat());
            panel.fill(getValue(panelData.getFill(), 1));
            panel.linewidth(getValue(panelData.getLinewidth(), 1));
            panel.lines(getValue(panelData.getLines(), true));
            panel.stack(panelData.getStack());
            panel.decimals(panelData.getDecimals());
            panel.valueName(panelData.getValueName());
            panel.repeat(panelData.getRepeat());
            panel.repeatDirection(panelData.getRepeatDirection());
            panel.minSpan(panelData.getMinSpan());

            switch (getValue(panelData.getType(), "graph")) {
                case "singlestat":
                    panel.type(DashboardPanel.Type.SINGLESTAT);
                    break;
                case "graph":
                    panel.type(DashboardPanel.Type.GRAPH);
                    break;
                default:
                    panel.type(DashboardPanel.Type.GRAPH);
                    break;
            }

            ArrayList<DashboardPanelTarget> targets = new ArrayList<>();
            if (panelData.getTargets() != null) {
                for (Target targetData : panelData.getTargets()) {
                    DashboardPanelTarget target = new DashboardPanelTarget();
                    target.refId(targetData.getRefId());
                    target.expr(targetData.getExpr());
                    target.legendFormat(targetData.getLegendFormat());
                    target.intervalFactor(targetData.getIntervalFactor());

                    targets.add(target);
                }

                panel.targets(targets);
            }

            if (panelData.getLegend() != null) {
                DashboardPanelLegend legend = new DashboardPanelLegend();
                legend.show(panelData.getLegend().getShow());
                legend.alignAsTable(panelData.getLegend().getAlignAsTable());
                legend.rightSide(panelData.getLegend().getRightSide());
                legend.current(panelData.getLegend().getCurrent());
                panel.legend(legend);
            }

            if (panelData.getTooltip() != null) {
                DashboardPanelTooltip tooltip = new DashboardPanelTooltip();
                tooltip.valueType(panelData.getTooltip().getValueType());
                panel.tooltip(tooltip);
            }

            DashboardPanelGridPosition gridPos = new DashboardPanelGridPosition();
            gridPos.h(panelData.getGridPos().getH());
            gridPos.w(panelData.getGridPos().getW());
            gridPos.x(panelData.getGridPos().getX());
            gridPos.y(panelData.getGridPos().getY());
            panel.gridPos(gridPos);

            if (panelData.getXaxis() != null && panelData.getYaxes() != null) {
                DashboardPanelXAxis xAxis = new DashboardPanelXAxis();

                xAxis.show(getValue(panelData.getXaxis().getShow(), true));
                switch (panelData.getXaxis().getMode()) {
                    case "time":
                        xAxis.mode(DashboardPanelXAxis.Mode.TIME);
                        break;
                    default:
                        xAxis.mode(DashboardPanelXAxis.Mode.TIME);
                        break;
                }

                DashboardPanelYAxis yLeftAxis = new DashboardPanelYAxis();

                yLeftAxis.logBase(getValue(panelData.getYaxes().get(0).getLogBase(), 1));
                yLeftAxis.show(getValue(panelData.getYaxes().get(0).getShow(), true));
                yLeftAxis.min(panelData.getYaxes().get(0).getMin());
                yLeftAxis.max(panelData.getYaxes().get(0).getMax());

                switch (panelData.getYaxes().get(0).getFormat()) {
                    case "short":
                        yLeftAxis.format(DashboardPanelYAxis.Format.SHORT);
                        break;
                    case "bytes":
                        yLeftAxis.format(DashboardPanelYAxis.Format.BYTES);
                        break;
                    default:
                        yLeftAxis.format(DashboardPanelYAxis.Format.NONE);
                        break;
                }

                DashboardPanelYAxis yRightAxis = new DashboardPanelYAxis();
                yRightAxis.logBase(getValue(panelData.getYaxes().get(1).getLogBase(), 1));
                yRightAxis.show(getValue(panelData.getYaxes().get(1).getShow(), true));
                yRightAxis.min(panelData.getYaxes().get(1).getMin());
                yRightAxis.max(panelData.getYaxes().get(1).getMax());

                switch (panelData.getYaxes().get(1).getFormat()) {
                    case "short":
                        yRightAxis.format(DashboardPanelYAxis.Format.SHORT);
                        break;
                    case "bytes":
                        yRightAxis.format(DashboardPanelYAxis.Format.BYTES);
                        break;
                    default:
                        yRightAxis.format(DashboardPanelYAxis.Format.NONE);
                        break;
                }

                panel.xaxis(xAxis);
                panel.yaxes(new ArrayList<>(Arrays.asList(yLeftAxis, yRightAxis)));
            }

            panels.add(panel);
        }

        dashboard.panels(panels);
        DashboardMeta dashboardMeta = new DashboardMeta().canSave(true).slug(data.getTitle());
        return new GrafanaDashboard().meta(dashboardMeta).dashboard(dashboard);
    }
}