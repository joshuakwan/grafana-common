/* Licensed under Apache-2.0 */
package grafana.beans.dashboard;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Meta {
  String type;
  Boolean canSave;
  Boolean canEdit;
  Boolean canStar;
  String slug;
  String expires;
  String created;
  String updated;
  String updatedBy;
  String createdBy;
  Integer version;
}
