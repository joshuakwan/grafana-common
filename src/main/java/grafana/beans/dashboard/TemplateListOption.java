/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class TemplateListOption {
    @JsonProperty
    Boolean selected;

    @JsonProperty
    String text;

    @JsonProperty
    String value;
}
