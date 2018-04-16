/* Licensed under Apache-2.0 */
package grafana.beans.messages;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class GrafanaMessage {
    String message;
}
