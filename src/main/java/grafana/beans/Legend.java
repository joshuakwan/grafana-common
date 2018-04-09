package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Legend {
    @JsonProperty
    Boolean show;

    @JsonProperty
    Boolean alignAsTable;

    @JsonProperty
    Boolean rightSide;

    @JsonProperty
    Boolean current;

    public Boolean getShow() {
        return show;
    }

    public Boolean getAlignAsTable() {
        return alignAsTable;
    }

    public Boolean getRightSide() {
        return rightSide;
    }

    public Boolean getCurrent() {
        return current;
    }
}

