/* Licensed under Apache-2.0 */
package grafana;

import grafana.beans.GrafanaDashboard;
import grafana.beans.GrafanaMessage;
import grafana.beans.alert.AlertNotification;
import grafana.beans.dashboard.PanelAlert;
import grafana.beans.dashboard.DashboardSuccessfulDelete;
import grafana.beans.dashboard.DashboardSuccessfulPost;
import grafana.beans.organization.GrafanaOrganization;
import grafana.beans.GrafanaSearchResult;
import grafana.beans.organization.OrganizationSuccessfulPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GrafanaService {
    String GRAFANA_DASHBOARDS = "api/dashboards/db/";
    String GRAFANA_DASHBOARDS_UID = "api/dashboards/uid/";
    String GRAFANA_NOTIFICATIONS = "api/alert-notifications/";
    String GRAFANA_ALERTS = "api/alerts/";
    String GRAFANA_SEARCH = "api/search/";

    String AUTHORIZATION = "Authorization";

    // Organizations
    @POST("api/orgs/")
    Call<OrganizationSuccessfulPost> createOrganization(
            @Body GrafanaOrganization organization);

    @GET("api/orgs/name/{organizationName}")
    Call<GrafanaOrganization> getOrganization(
            @Path("organizationName") String organizationName);

    //Dashboards
    @GET(GRAFANA_DASHBOARDS_UID + "{dashboardUid}")
    Call<GrafanaDashboard> getDashboard(
            @Header(AUTHORIZATION) String authorization, @Path("dashboardUid") String dashboardUid);

    @POST(GRAFANA_DASHBOARDS)
    Call<DashboardSuccessfulPost> postDashboard(
            @Header(AUTHORIZATION) String authorization, @Body GrafanaDashboard dashboard);

    @DELETE(GRAFANA_DASHBOARDS_UID + "{dashboardUid}")
    Call<DashboardSuccessfulDelete> deleteDashboard(
            @Header(AUTHORIZATION) String authorization, @Path("dashboardUid") String dashboardUid);

    //Notifications
    @GET(GRAFANA_NOTIFICATIONS + "{id}")
    Call<AlertNotification> getNotification(
            @Header(AUTHORIZATION) String authorization, @Path("id") Integer id);

    @GET(GRAFANA_NOTIFICATIONS)
    Call<List<AlertNotification>> getNotifications(@Header(AUTHORIZATION) String authorization);

    @POST(GRAFANA_NOTIFICATIONS)
    Call<AlertNotification> postNotification(
            @Header(AUTHORIZATION) String authorization, @Body AlertNotification notification);

    @PUT(GRAFANA_NOTIFICATIONS + "{id}")
    Call<AlertNotification> putNotification(
            @Header(AUTHORIZATION) String authorization,
            @Path("id") Integer id,
            @Body AlertNotification notification);

    @DELETE(GRAFANA_NOTIFICATIONS + "{id}")
    Call<GrafanaMessage> deleteNotification(
            @Header(AUTHORIZATION) String authorization, @Path("id") Integer id);

    //Alerts
    @GET(GRAFANA_ALERTS + "{id}")
    Call<PanelAlert> getAlert(
            @Header(AUTHORIZATION) String authorization, @Path("id") Integer id);

    // Search
    @GET(GRAFANA_SEARCH)
    Call<List<GrafanaSearchResult>> search(
            @Header(AUTHORIZATION) String authorization,
            @Query("query") String query,
            @Query("tag") String tag,
            @Query("starred") Boolean starred);
}
