package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DashboardPanelGridPosition {
    @JsonProperty
    Integer h;

    @JsonProperty
    Integer w;

    @JsonProperty
    Integer x;

    @JsonProperty
    Integer y;
}
