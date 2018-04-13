/* Licensed under Apache-2.0 */
package grafana.beans;

import grafana.beans.dashboard.Meta;
import grafana.beans.dashboard.Dashboard;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class GrafanaDashboard {
    Meta meta;
    Dashboard dashboard;
}
