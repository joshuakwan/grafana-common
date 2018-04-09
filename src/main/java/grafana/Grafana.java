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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Grafana {
    private GrafanaClient client;

    public Grafana(String baseUrl, String auth) {
        GrafanaConfiguration config = new GrafanaConfiguration().host(baseUrl).apiKey(auth);
        this.client = new GrafanaClient(config);
    }

    public DashboardSuccessfulPost createDashboard(InputStream yamlStream)
            throws IOException, GrafanaException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        grafana.beans.Dashboard data = mapper.readValue(yamlStream, grafana.beans.Dashboard.class);
        System.out.println(data);

        Dashboard dashboard = new Dashboard();
        dashboard.title(data.getTitle());
        dashboard.refresh(data.getRefresh());
        dashboard.schemaVersion(data.getSchemaVersion());
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
            panel.fill(panelData.getFill());
            panel.linewidth(panelData.getLinewidth());
            panel.lines(panelData.getLines());
            panel.stack(panelData.getStack());
            panel.decimals(panelData.getDecimals());
            panel.valueName(panelData.getValueName());

            switch (panelData.getType()) {
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
                    if (targetData.getLegendFormat() != null) {
                        target.legendFormat(targetData.getLegendFormat());
                    }
                    if (targetData.getIntervalFactor() != null) {
                        target.intervalFactor(targetData.getIntervalFactor());
                    }

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

            DashboardPanelXAxis xAxis = new DashboardPanelXAxis();
            xAxis.show(panelData.getXaxis().getShow());
            switch (panelData.getXaxis().getMode()) {
                case "time":
                    xAxis.mode(DashboardPanelXAxis.Mode.TIME);
                    break;
                default:
                    xAxis.mode(DashboardPanelXAxis.Mode.TIME);
                    break;
            }

            DashboardPanelYAxis yLeftAxis = new DashboardPanelYAxis();
            yLeftAxis.logBase(panelData.getYaxes().get(0).getLogBase());
            yLeftAxis.show(panelData.getYaxes().get(0).getShow());
            yLeftAxis.min(panelData.getYaxes().get(0).getMin());

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
            yRightAxis.logBase(panelData.getYaxes().get(1).getLogBase());
            yRightAxis.show(panelData.getYaxes().get(1).getShow());
            yRightAxis.min(panelData.getYaxes().get(1).getMin());
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

            panels.add(panel);
        }

        dashboard.panels(panels);
        DashboardMeta dashboardMeta = new DashboardMeta().canSave(true).slug(data.getTitle());
        GrafanaDashboard grafanaDashboard = new GrafanaDashboard().meta(dashboardMeta).dashboard(dashboard);
        return this.client.createDashboard(grafanaDashboard);
    }

    public String deleteDashboardByName(String name)
            throws GrafanaDashboardDoesNotExistException, GrafanaDashboardCouldNotDeleteException, IOException {
        return this.client.deleteDashboard(name);
    }


    public GrafanaDashboard getDashboard(String dashboardTitle, String dashboardFolder)
            throws IOException, GrafanaException {
        String uid = this.getDashboardUid(dashboardTitle, dashboardFolder);
        return this.client.getDashboard(uid);
    }

    public String getDashboardUid(String dashboardTitle, String dashboardFolder) {
        return this.client.searchDashboard(dashboardTitle, dashboardFolder);
    }
}