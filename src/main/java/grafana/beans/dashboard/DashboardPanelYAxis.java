package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DashboardPanelYAxis {
    @JsonProperty
    String format;

    @JsonProperty
    Integer logBase = 1;

    @JsonProperty
    Boolean show = true;

    @JsonProperty
    Integer min;

    @JsonProperty
    Integer max;

    @JsonProperty
    String label;
}
