package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import grafana.models.DashboardPanelAlert;
import grafana.models.DashboardPanelThreshold;
import lombok.Data;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(fluent = true)
public class Panel {
    @JsonProperty
    String title;

    @JsonProperty
    String datasource;

    @JsonProperty
    String type = "graph";

    @JsonProperty
    String format;

    @JsonProperty
    Integer fill;

    @JsonProperty
    Integer linewidth = 1;

    @JsonProperty
    Boolean lines = true;

    @JsonProperty
    Boolean stack;

    @JsonProperty
    Integer decimals;

    @JsonProperty
    String valueName;

    @JsonProperty
    String nullPointMode;

    @JsonProperty
    String repeat;

    @JsonProperty
    String repeatDirection;

    @JsonProperty
    Integer minSpan;

    @JsonProperty
    DashboardPanelGridPosition gridPos;

    @JsonProperty
    DashboardPanelLegend legend;

    @JsonProperty
    DashboardPanelTooltip tooltip;

    @JsonProperty
    ArrayList<Target> targets;

    @JsonProperty
    DashboardPanelXAxis xaxis;

    @JsonProperty
    ArrayList<DashboardPanelYAxis> yaxes;

    @JsonProperty
    Boolean editable;

    @JsonProperty
    Boolean error;

    @JsonProperty
    Integer id;

    @JsonProperty
    DashboardPanelAlert alert;

    @JsonProperty
    List<DashboardPanelThreshold> thresholds;
}
