package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DashboardPanelLegend {
    @JsonProperty
    Boolean avg;

    @JsonProperty
    Boolean current;

    @JsonProperty
    Boolean max;

    @JsonProperty
    Boolean min;

    @JsonProperty
    Boolean show;

    @JsonProperty
    Boolean totals;

    @JsonProperty
    Boolean values;

    @JsonProperty
    Boolean hideEmpty;

    @JsonProperty
    Boolean hideZero;

    @JsonProperty
    Boolean alignAsTable;

    @JsonProperty
    Boolean rightSide;

}

