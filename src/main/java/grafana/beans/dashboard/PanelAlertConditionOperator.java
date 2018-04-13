/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
public class PanelAlertConditionOperator {
    @JsonProperty
    String type;
}
