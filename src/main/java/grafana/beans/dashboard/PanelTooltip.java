package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class PanelTooltip {
    @JsonProperty
    Boolean msResolution;

    @JsonProperty
    Boolean shared;

    @JsonProperty
    Integer sort;

    @JsonProperty
    String valueType;
}
