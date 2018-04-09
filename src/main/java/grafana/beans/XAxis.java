package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XAxis {
    @JsonProperty
    Boolean show;

    @JsonProperty
    String mode;

    @Override
    public String toString() {
        return "XAxis{" +
                "show=" + show +
                ", mode='" + mode + '\'' +
                '}';
    }

    public Boolean getShow() {
        return show;
    }

    public String getMode() {
        return mode;
    }
}
