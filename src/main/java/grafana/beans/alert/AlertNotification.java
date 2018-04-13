/* Licensed under Apache-2.0 */
package grafana.beans.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class AlertNotification {
    @JsonProperty
    Integer id;

    @JsonProperty
    String name;

    @JsonProperty
    String type;

    @JsonProperty
    Boolean isDefault;

    @JsonProperty
    AlertNotificationSettings settings;

    @JsonProperty
    String created;

    @JsonProperty
    String updated;
}
