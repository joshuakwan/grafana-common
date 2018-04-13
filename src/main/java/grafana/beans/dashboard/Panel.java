package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import grafana.GrafanaDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(fluent = true)
//@JsonDeserialize(using = GrafanaDeserializer.class)
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
    Integer fill = 1;

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
    PanelGridPosition gridPos;

    @JsonProperty
    PanelLegend legend;

    @JsonProperty
    PanelTooltip tooltip;

    @JsonProperty
    ArrayList<Target> targets;

    @JsonProperty
    PanelXAxis xaxis;

    @JsonProperty
    ArrayList<PanelYAxis> yaxes;

    @JsonProperty
    Boolean editable;

    @JsonProperty
    Boolean error;

    @JsonProperty
    Integer id;

    @JsonProperty
    PanelAlert alert;

    @JsonProperty
    List<PanelThreshold> thresholds;
}
