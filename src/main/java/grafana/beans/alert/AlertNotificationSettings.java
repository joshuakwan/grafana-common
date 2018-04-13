/* Licensed under Apache-2.0 */
package grafana.beans.alert;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class AlertNotificationSettings {
    private String httpMethod;
    private String url;
}
