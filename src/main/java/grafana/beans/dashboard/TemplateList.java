package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Data
@Accessors(fluent = true)
public class TemplateList {
    @JsonProperty
    String name;

    @JsonProperty
    String type;

    @JsonProperty
    String datasource;

    @JsonProperty
    String query;

    @JsonProperty
    Integer refresh;

    @JsonProperty
    Boolean multi;

    @JsonProperty
    Boolean includeAll;

    @JsonProperty
    DashboardTemplateListCurrent current;

    @JsonProperty
    ArrayList<DashboardTemplateListOption> options;
}
