/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
public class PanelAlertConditionEvaluator {
    @JsonProperty
    List<Double> params;

    @JsonProperty
    String type;
}
