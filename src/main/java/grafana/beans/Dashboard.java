package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Dashboard {
    @JsonProperty
    private String title;

    @JsonProperty
    private String datasource;

    @JsonProperty
    private String refresh;

    @JsonProperty
    private Integer schemaVersion;

    @JsonProperty
    private Time time;

    @JsonProperty
    private ArrayList<Panel> panels;

    @JsonProperty
    private ArrayList<Template> templating;

    @Override
    public String toString() {
        return "Dashboard{" +
                "title='" + title + '\'' +
                ", datasource='" + datasource + '\'' +
                ", refresh='" + refresh + '\'' +
                ", schemaVersion=" + schemaVersion +
                ", time=" + time +
                ", panels=" + panels +
                ", templating=" + templating +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getDatasource() {
        return datasource;
    }

    public String getRefresh() {
        return refresh;
    }

    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    public Time getTime() {
        return time;
    }

    public ArrayList<Panel> getPanels() {
        return panels;
    }

    public ArrayList<Template> getTemplating() {
        return templating;
    }
}
