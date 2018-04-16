package grafana.beans.datasource;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Datasource {
    Integer id;
    Integer orgId;
    String name;
    String type;
    String typeLogoUrl;
    String access;
    String url;
    String password;
    String user;
    String database;
    Boolean basicAuth;
    Boolean isDefault;
    JsonData jsonData;
    Boolean readOnly;
}
