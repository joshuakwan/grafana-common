package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Target {
    @JsonProperty
    String refId;

    @JsonProperty
    String expr;

    @JsonProperty
    String legendFormat;

    @JsonProperty
    Integer intervalFactor;
}
