/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

import grafana.beans.alert.AlertNotification;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class PanelAlert {

    List<PanelAlertCondition> conditions;
    AlertState executionErrorState;
    String frequency;
    Integer handler;
    String message;
    String name;
    AlertState noDataState;
    List<AlertNotification> notifications;

    public enum AlertState {
        ALERTING("alerting"),
        NO_DATA("no_data"),
        KEEP_LAST_STATE("keep_state"),
        OK("ok");
        private final String value;

        AlertState(String s) {
            value = s;
        }

        @JsonValue
        public String value() {
            return value;
        }
    }
}
