/* Licensed under Apache-2.0 */
package grafana.beans.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class AlertNotificationSettings {
    @JsonProperty
    private String httpMethod;

    @JsonProperty
    private String url;
}
