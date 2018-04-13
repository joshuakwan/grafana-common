package grafana.beans.dashboard;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DashboardSuccessfulPost {
    Integer id;
    String uid;
    String url;
    String status;
    Integer version;
    String slug;
}
