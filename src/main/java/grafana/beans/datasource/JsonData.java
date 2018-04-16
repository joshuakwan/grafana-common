package grafana.beans.datasource;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Data
@Accessors(fluent = true)
public class JsonData {
    ArrayList<String> keepCookies;
}
