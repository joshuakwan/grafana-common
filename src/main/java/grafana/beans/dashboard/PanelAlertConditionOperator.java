/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
public class PanelAlertConditionOperator {

    Type type;

    public enum Type {
        AND("and"),
        OR("or");
        private final String value;

        Type(String s) {
            value = s;
        }

        @JsonValue
        public String value() {
            return value;
        }
    }
}
