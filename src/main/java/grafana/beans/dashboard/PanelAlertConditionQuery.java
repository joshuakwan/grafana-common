/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import java.util.List;

import grafana.beans.dashboard.Target;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class PanelAlertConditionQuery {
    Integer datasourceId;
    Target model;
    List<String> params;
}
