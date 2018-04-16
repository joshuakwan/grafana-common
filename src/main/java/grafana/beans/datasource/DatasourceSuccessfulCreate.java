package grafana.beans.datasource;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DatasourceSuccessfulCreate {
    String message;
    Integer id;
    String name;
}
