package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YAxis {
    @JsonProperty
    String format;

    @JsonProperty
    Integer logBase;

    @JsonProperty
    Boolean show;

    @JsonProperty
    Integer min;

    @JsonProperty
    Integer max;

    @Override
    public String toString() {
        return "YAxis{" +
                "format='" + format + '\'' +
                ", logBase=" + logBase +
                ", show=" + show +
                '}';
    }

    public String getFormat() {
        return format;
    }

    public Integer getLogBase() {
        return logBase;
    }

    public Boolean getShow() {
        return show;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }
}
