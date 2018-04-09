package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Target {
    @JsonProperty
    String refId;

    @JsonProperty
    String expr;

    @JsonProperty
    String legendFormat;

    @JsonProperty
    Integer intervalFactor;

    public String getRefId() {
        return refId;
    }

    public String getExpr() {
        return expr;
    }

    public String getLegendFormat() {
        return legendFormat;
    }

    public Integer getIntervalFactor() {
        return intervalFactor;
    }

    @Override
    public String toString() {
        return "Target{" +
                "refId='" + refId + '\'' +
                ", expr='" + expr + '\'' +

                ", legendFormat='" + legendFormat + '\'' +
                ", intevalFactor='" + intervalFactor + '\'' +
                '}';
    }
}
