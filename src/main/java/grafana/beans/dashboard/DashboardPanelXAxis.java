package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DashboardPanelXAxis {
    @JsonProperty
    Boolean show = true;

    @JsonProperty
    String mode;
}
