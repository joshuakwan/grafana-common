package grafana.beans.dashboard;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Template {
    @JsonProperty
    ArrayList<TemplateList> list;
}
