package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tooltip {
    @JsonProperty
    String valueType;

    public String getValueType() {
        return valueType;
    }
}
