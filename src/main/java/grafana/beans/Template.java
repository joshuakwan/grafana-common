package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Template {
    @JsonProperty
    String name;

    @JsonProperty
    String type;

    @JsonProperty
    String datasource;

    @JsonProperty
    String query;

    @JsonProperty
    Integer refresh;

    @JsonProperty
    Boolean multi;

    @JsonProperty
    Boolean includeAll;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDatasource() {
        return datasource;
    }

    public String getQuery() {
        return query;
    }

    public Integer getRefresh() {
        return refresh;
    }

    public Boolean getMulti() {
        return multi;
    }

    public Boolean getIncludeAll() {
        return includeAll;
    }

    @Override
    public String toString() {
        return "Template{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", datasource='" + datasource + '\'' +
                ", query='" + query + '\'' +
                ", refresh=" + refresh +
                ", multi=" + multi +
                ", includeAll=" + includeAll +
                '}';
    }
}
