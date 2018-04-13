/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
public class PanelAlertCondition {

    PanelAlertConditionEvaluator evaluator;
    PanelAlertConditionQuery query;
    PanelAlertConditionReducer reducer;
    PanelAlertConditionOperator operator;
    Type type;

    public enum Type {
        QUERY("query");
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
