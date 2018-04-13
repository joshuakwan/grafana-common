package grafana.beans.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Data
@Accessors(fluent = true)
public class Dashboard {
    @JsonProperty
    Integer id;

    @JsonProperty
    String uid;

    @JsonProperty
    Integer version;

    @JsonProperty
    String timezone;

    @JsonProperty
    ArrayList<String> tags;

    @JsonProperty
    String title;

    @JsonProperty
    String datasource;

    @JsonProperty
    String refresh;

    @JsonProperty
    Integer schemaVersion = 1;

    @JsonProperty
    Time time;

    @JsonProperty
    ArrayList<Panel> panels;

    @JsonProperty
    Template templating;
}
