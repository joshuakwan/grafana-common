package grafana.beans.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class User {
    Integer id;
    String name;
    String login;
    String email;
    Boolean isAdmin;

    // From the organization APIs
    Integer ordId;
    Integer userId;
    String role;

    // Used for POST
    String loginOrEmail;
}
