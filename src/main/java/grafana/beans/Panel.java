package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Panel {
    public String getValueName() {
        return valueName;
    }

    @JsonProperty
    String title;

    @JsonProperty
    String datasource;

    @JsonProperty
    String type;

    @JsonProperty
    String format;

    @JsonProperty
    Integer fill;

    @JsonProperty
    Integer linewidth;

    @JsonProperty
    Boolean lines;

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
    GridPosition gridPos;

    @JsonProperty
    Legend legend;

    @JsonProperty
    Tooltip tooltip;

    @JsonProperty
    ArrayList<Target> targets;

    @JsonProperty
    XAxis xaxis;

    @JsonProperty
    ArrayList<YAxis> yaxes;

    public String getTitle() {
        return title;
    }

    public String getDatasource() {
        return datasource;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public Integer getFill() {
        return fill;
    }

    public Integer getLinewidth() {
        return linewidth;
    }

    public Boolean getLines() {
        return lines;
    }

    public Boolean getStack() {
        return stack;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public GridPosition getGridPos() {
        return gridPos;
    }

    public ArrayList<Target> getTargets() {
        return targets;
    }

    public XAxis getXaxis() {
        return xaxis;
    }

    public ArrayList<YAxis> getYaxes() {
        return yaxes;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public String getNullPointMode() {
        return nullPointMode;
    }

    public Legend getLegend() {
        return legend;
    }

    public String getRepeat() {
        return repeat;
    }

    public String getRepeatDirection() {
        return repeatDirection;
    }

    public Integer getMinSpan() {
        return minSpan;
    }

    @Override
    public String toString() {
        return "Panel{" +
                "title='" + title + '\'' +
                ", datasource='" + datasource + '\'' +
                ", type='" + type + '\'' +
                ", format='" + format + '\'' +
                ", fill=" + fill +
                ", linewidth=" + linewidth +
                ", lines=" + lines +
                ", stack=" + stack +
                ", decimals=" + decimals +
                ", valueName='" + valueName + '\'' +
                ", nullPointMode='" + nullPointMode + '\'' +
                ", repeat='" + repeat + '\'' +
                ", repeatDirection='" + repeatDirection + '\'' +
                ", minSpan=" + minSpan +
                ", gridPos=" + gridPos +
                ", legend=" + legend +
                ", tooltip=" + tooltip +
                ", targets=" + targets +
                ", xaxis=" + xaxis +
                ", yaxes=" + yaxes +
                '}';
    }
}
