/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class PanelThreshold {
    @JsonProperty
    String colorMode = "critical";

    @JsonProperty
    Boolean fill = true;

    @JsonProperty
    Boolean line = true;

    @JsonProperty
    String op;

    @JsonProperty
    Long value;
}
