/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DashboardSuccessfulDelete {
  String title;
}
