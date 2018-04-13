package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

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
    ArrayList<PanelThreshold> thresholds;

    // What a CRAP!
    @JsonSetter("thresholds")
    public void setThresholdsInternal(JsonNode thresholdsInternal){
        if(thresholdsInternal !=null){
            if(!thresholdsInternal.isArray()){
                thresholds = new ArrayList<>();
            } else{
                thresholds = new ArrayList<>();
                Iterator<JsonNode> nodeIterator = thresholdsInternal.iterator();
                while(nodeIterator.hasNext()) {
                    JsonNode entry = nodeIterator.next();
                    PanelThreshold threshold = new PanelThreshold();
                    threshold.colorMode(entry.get("colorMode").textValue());
                    threshold.fill(entry.get("fill").booleanValue());
                    threshold.line(entry.get("line").booleanValue());
                    threshold.op(entry.get("op").textValue());
                    threshold.value(entry.get("value").longValue());
                    thresholds.add(threshold);
                }
            }
        }
    }

    @JsonGetter("thresholds")
    public ArrayList<PanelThreshold> getThresholds(){
        return thresholds;
    }
}
