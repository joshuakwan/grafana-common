/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import grafana.beans.dashboard.Target;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class PanelAlertConditionQuery {
    @JsonProperty
    Integer datasourceId;

    @JsonProperty
    Target model;

    @JsonProperty
    List<String> params;
}
