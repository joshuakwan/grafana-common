/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

import grafana.beans.alert.AlertNotification;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class PanelAlert {
    @JsonProperty
    List<PanelAlertCondition> conditions;

    @JsonProperty
    String executionErrorState;

    @JsonProperty
    String frequency;

    @JsonProperty
    Integer handler;

    @JsonProperty
    String message;

    @JsonProperty
    String name;

    @JsonProperty
    String noDataState;

    @JsonProperty
    List<AlertNotification> notifications;
}
