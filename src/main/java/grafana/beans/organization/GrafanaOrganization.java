package grafana.beans.organization;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class GrafanaOrganization {
    Integer id;
    String name;
    OrganizationAddress address;
}
