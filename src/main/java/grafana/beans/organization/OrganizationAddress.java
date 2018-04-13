package grafana.beans.organization;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class OrganizationAddress {
    String address1;
    String address2;
    String city;
    String zipCode;
    String state;
    String country;
}
