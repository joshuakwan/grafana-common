package grafana;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import grafana.beans.dashboard.DashboardSuccessfulDelete;
import grafana.beans.dashboard.DashboardSuccessfulPost;
import grafana.beans.GrafanaSearchResult;
import grafana.beans.dashboard.Template;
import grafana.beans.organization.GrafanaOrganization;
import grafana.configuration.GrafanaConfiguration;
import grafana.exceptions.GrafanaDashboardCouldNotDeleteException;
import grafana.exceptions.GrafanaDashboardDoesNotExistException;
import grafana.exceptions.GrafanaException;
import grafana.models.*;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;


public class Grafana {
    private final GrafanaConfiguration config;
    private final GrafanaService service;

    private static final ObjectMapper mapper =
            new ObjectMapper()
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    /**
     * Allows for the user to provide an OkHttpClient that can be used to connect to the Grafana
     * service. The client provided can be configured with an {@link okhttp3.Interceptor} or other
     * specifics that are then used during communications with Grafana
     *
     * @param configuration the information needed to communicate with Grafana.
     * @param client        An OkHttpClient that is used to connect to Grafana
     */
    public Grafana(GrafanaConfiguration configuration, OkHttpClient client) {
        this.config = configuration;

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(configuration.host())
                        .client(client)
                        .addConverterFactory(JacksonConverterFactory.create(mapper))
                        .build();

        this.service = retrofit.create(GrafanaService.class);
    }

    /**
     * Initialize a Grafana API object with the admin authority
     *
     * @param baseUrl       base url of grafana
     * @param adminUsername the admin username
     * @param adminPassword the admin password
     */
    public Grafana(String baseUrl, String adminUsername, String adminPassword) {
        this.config = new GrafanaConfiguration().host(baseUrl);
        this.config.apiKey(Credentials.basic(adminUsername, adminPassword));
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originReq = chain.request();
                Request.Builder builder = originReq.newBuilder().header("Authorization",
                        Credentials.basic(adminUsername, adminPassword));
                Request newReq = builder.build();
                return chain.proceed(newReq);
            }
        }).build();

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(okHttpClient)
                        .addConverterFactory(JacksonConverterFactory.create(mapper))
                        .build();

        this.service = retrofit.create(GrafanaService.class);
    }

    /**
     * Initialize a Grafana API object
     *
     * @param baseUrl base url of grafana
     * @param auth    grafana auth string
     */
    public Grafana(String baseUrl, String auth) {
        this(new GrafanaConfiguration().host(baseUrl).apiKey(auth), new OkHttpClient());
    }

    // Organizations

    public OrganizationSuccessfulPost createOrganization(String organizationName)
            throws IOException, GrafanaException {
        GrafanaOrganization organization = new GrafanaOrganization().name(organizationName);
        return this.createOrganization(organization);
    }

    public OrganizationSuccessfulPost createOrganization(GrafanaOrganization organization)
            throws IOException, GrafanaException {
        Response<OrganizationSuccessfulPost> response = service.createOrganization(organization).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    public GrafanaOrganization getOrganization(String organizationName)
            throws GrafanaException, IOException {
        Response<GrafanaOrganization> response = service.getOrganization(organizationName).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    // Dashboard

    /**
     * Create a new dashboard from a YAML input stream
     *
     * @param yamlStream the YAML input stream
     * @return the response object that contains essential information
     * @throws IOException
     * @throws GrafanaException
     */
    public DashboardSuccessfulPost createDashboard(InputStream yamlStream)
            throws IOException, GrafanaException {
        grafana.beans.dashboard.Dashboard data = getYamlData(yamlStream);
        GrafanaDashboard grafanaDashboard = buildGrafanaDashboard(data);
        return this.createDashboard(grafanaDashboard);
    }

    /**
     * Creates a Grafana dashboard. If a dashboard already exists with the same name it will be
     * updated.
     *
     * @param grafanaDashboard the dashboard to be created.
     * @return Meta information about the newly created dashboard
     * @throws GrafanaException if Grafana returns an error when trying to create the dashboard.
     * @throws IOException      if a problem occurred talking to the server.
     */
    public DashboardSuccessfulPost createDashboard(GrafanaDashboard grafanaDashboard)
            throws GrafanaException, IOException {
        return this.updateDashboard(grafanaDashboard);
    }

    /**
     * Delete a dashboard by its title
     *
     * @param dashboardTitle the dashboard title
     * @return the title of the deleted dashboard
     * @throws GrafanaDashboardDoesNotExistException
     * @throws GrafanaDashboardCouldNotDeleteException
     * @throws IOException
     */
    public String deleteDashboard(String dashboardTitle)
            throws GrafanaDashboardDoesNotExistException, GrafanaDashboardCouldNotDeleteException, IOException {
        return this.deleteDashboard(dashboardTitle, null);
    }

    /**
     * Delete a dashboard by its title and folder
     *
     * @param dashboardTitle  the dashboard title
     * @param dashboardFolder the folder name which contains the dashboard
     * @return the title of the deleted dashboard
     * @throws GrafanaDashboardDoesNotExistException
     * @throws GrafanaDashboardCouldNotDeleteException
     * @throws IOException
     */
    public String deleteDashboard(String dashboardTitle, String dashboardFolder)
            throws GrafanaDashboardDoesNotExistException, GrafanaDashboardCouldNotDeleteException, IOException {
        String uid = this.getDashboardUid(dashboardTitle, dashboardFolder);
        return this.deleteDashboardByUid(uid);
    }

    public String deleteDashboardByUid(String dashboardUid)
            throws GrafanaDashboardDoesNotExistException, GrafanaDashboardCouldNotDeleteException,
            IOException {

        Response<DashboardSuccessfulDelete> response =
                service.deleteDashboard(config.apiKey(), dashboardUid).execute();

        if (response.isSuccessful()) {
            return response.body().title();
        } else if (response.code() == HTTP_NOT_FOUND) {
            throw new GrafanaDashboardDoesNotExistException(
                    "Dashboard " + dashboardUid + " does not exist");
        } else {
            throw GrafanaDashboardCouldNotDeleteException.withErrorBody(response.errorBody());
        }
    }

    /**
     * Update a dashboard with a YAML input stream
     *
     * @param dashboardTitle the title of the dashboard to be updated
     * @param yamlStream     the YAML stream which contains updated configuration
     * @return the response object that contains essential information
     * @throws IOException
     * @throws GrafanaException
     */
    public DashboardSuccessfulPost updateDashboard(String dashboardTitle, InputStream yamlStream)
            throws IOException, GrafanaException {
        return this.updateDashboard(dashboardTitle, null, yamlStream);
    }

    /**
     * Update a dashboard with a YAML input stream
     *
     * @param dashboardTitle  the title of the dashboard to be updated
     * @param dashboardFolder the name of the folder which contains the dashboard to be updated
     * @param yamlStream      the YAML stream which contains updated configuration
     * @return the response object that contains essential information
     * @throws IOException
     * @throws GrafanaException
     */
    public DashboardSuccessfulPost updateDashboard(String dashboardTitle, String dashboardFolder, InputStream yamlStream)
            throws IOException, GrafanaException {
        // 1. get existing dashboard
        GrafanaDashboard existingDashboard = this.getDashboard(dashboardTitle, dashboardFolder);

        // 2. build new dashboard
        grafana.beans.dashboard.Dashboard data = getYamlData(yamlStream);
        GrafanaDashboard newDashboard = buildGrafanaDashboard(data);

        // 3. make the update
        // 3.1. specify the version
        newDashboard.dashboard().version(existingDashboard.dashboard().version());
        // 3.2. set uid so the dashboard won't be created again
        newDashboard.dashboard().uid(existingDashboard.dashboard().uid());

        return this.updateDashboard(newDashboard);
    }

    /**
     * Updates a Grafana dashboard. If the dashboard did not previously exist it will be created.
     *
     * @param dashboard the dashboard to be updated.
     * @return Meta information about the updated dashboard
     * @throws GrafanaException if Grafana returns an error when trying to create the dashboard.
     * @throws IOException      if a problem occurred talking to the server.
     */
    public DashboardSuccessfulPost updateDashboard(GrafanaDashboard dashboard)
            throws GrafanaException, IOException {

        Response<DashboardSuccessfulPost> response = service.postDashboard(this.config.apiKey(),
                dashboard).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    /**
     * Get a dashboard by its title and folder name
     *
     * @param dashboardTitle  the dashboard title
     * @param dashboardFolder the name of the folder which contains the dashboard
     * @return the dashboard object
     * @throws IOException
     * @throws GrafanaException
     */
    public GrafanaDashboard getDashboard(String dashboardTitle, String dashboardFolder)
            throws IOException, GrafanaException {
        String uid = this.getDashboardUid(dashboardTitle, dashboardFolder);
        return this.getDashboardByUid(uid);
    }

    /**
     * Get a dashboard by its title
     *
     * @param dashboardTitle the dashboard title
     * @return the dashboard object
     * @throws IOException
     * @throws GrafanaException
     */
    public GrafanaDashboard getDashboard(String dashboardTitle)
            throws IOException, GrafanaException {
        return this.getDashboard(dashboardTitle, null);
    }

    /**
     * Get the uid of a dashboard
     *
     * @param dashboardTitle  the dashboard title
     * @param dashboardFolder the name of the folder which contains the dashboard
     * @return the dashboard uid
     */
    public String getDashboardUid(String dashboardTitle, String dashboardFolder)
            throws GrafanaDashboardDoesNotExistException {
        return this.searchDashboard(dashboardTitle, dashboardFolder);
    }

    /**
     * Get the uid of a dashboard
     *
     * @param dashboardTitle the dashboard title
     * @return the dashboard uid
     */
    public String getDashboardUid(String dashboardTitle)
            throws GrafanaDashboardDoesNotExistException {
        return this.getDashboardUid(dashboardTitle, null);
    }

    /**
     * Searches for an existing dashboard by name.
     *
     * @param dashboardUid the uid of the dashboard to search for.
     * @return {@link GrafanaDashboard} with matching name.
     * @throws GrafanaDashboardDoesNotExistException if a dashboard with matching name does not exist.
     * @throws GrafanaException                      if Grafana returns an error when trying to retrieve the dashboard.
     * @throws IOException                           if a problem occurred talking to the server.
     */
    public GrafanaDashboard getDashboardByUid(String dashboardUid)
            throws GrafanaDashboardDoesNotExistException, GrafanaException, IOException {

        Response<GrafanaDashboard> response = service.getDashboard(config.apiKey(), dashboardUid).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else if (response.code() == HTTP_NOT_FOUND) {
            throw new GrafanaDashboardDoesNotExistException(
                    "Dashboard " + dashboardUid + " does not exist");
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    private grafana.beans.dashboard.Dashboard getYamlData(InputStream yamlStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(yamlStream, grafana.beans.dashboard.Dashboard.class);
    }

    private String searchDashboard(String dashboardTitle, String dashboardFolder)
            throws GrafanaDashboardDoesNotExistException {
        List<GrafanaSearchResult> response = null;
        String uid = null;
        try {
            response = this.search(dashboardTitle, null, false);
        } catch (GrafanaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.size() == 0) {
            throw new GrafanaDashboardDoesNotExistException("not found");
        }
        if (response.size() == 1) {
            uid = response.get(0).uid();
        } else {
            for (GrafanaSearchResult result : response) {
                if (dashboardFolder == null) {
                    if (result.folderTitle() == null) {
                        uid = result.uid();
                        break;
                    }
                } else {
                    if (dashboardFolder.equals(result.folderTitle())) {
                        uid = result.uid();
                        break;
                    }
                }
            }

            if (uid == null) {
                uid = response.get(0).uid();
            }

        }

        return uid;
    }

    private List<GrafanaSearchResult> search(
            String query, String tag, Boolean starred)
            throws GrafanaException, IOException {

        Response<List<GrafanaSearchResult>> response =
                service.search(this.config.apiKey(), query, tag, starred).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    private GrafanaDashboard buildGrafanaDashboard(grafana.beans.dashboard.Dashboard data) {
        Dashboard dashboard = new Dashboard();
        dashboard.title(data.title());
        dashboard.refresh(data.refresh());
        dashboard.schemaVersion(data.schemaVersion());

        dashboard.time(data.time());

        ArrayList<DashboardTemplateList> templateLists = new ArrayList<>();
        for (Template templateData : data.templating()) {
            DashboardTemplateList templateList = new DashboardTemplateList();
            templateList.name(templateData.getName());
            templateList.type(templateData.getType());
            templateList.datasource(templateData.getDatasource());
            templateList.query(templateData.getQuery());
            templateList.refresh(templateData.getRefresh());
            templateList.multi(templateData.getMulti());
            templateList.includeAll(templateData.getIncludeAll());
            templateLists.add(templateList);
        }

        DashboardTemplate templating = new DashboardTemplate().list(templateLists);
        dashboard.templating(templating);


        dashboard.panels(data.panels());
        DashboardMeta dashboardMeta = new DashboardMeta().canSave(true).slug(data.title());
        return new GrafanaDashboard().meta(dashboardMeta).dashboard(dashboard);
    }

    /**
     * Searches for a Grafana notification with a given id.
     *
     * @param id the id of the notification to search for.
     * @return the notification with the given id.
     * @throws GrafanaException if a notification with the given id does not exist.
     * @throws IOException      if a problem occurred talking to the server.
     */
    public AlertNotification getNotification(Integer id) throws GrafanaException, IOException {

        Response<AlertNotification> response = service.getNotification(config.apiKey(), id).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else if (response.code() == HTTP_NOT_FOUND) {
            throw new GrafanaException("Notification" + id + " does not exist");
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    /**
     * Returns a list of all Grafana notifications.
     *
     * @return a list of notifications.
     * @throws GrafanaException if Grafana returns an unexpected error.
     * @throws IOException      if a problem occurred talking to the server.
     */
    public List<AlertNotification> getNotifications() throws GrafanaException, IOException {

        Response<List<AlertNotification>> response = service.getNotifications(config.apiKey()).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    /**
     * Creates a Grafana alert notification.
     *
     * @param alertNotification the notification to be created.
     * @return the newly created notification
     * @throws GrafanaException if Grafana returns an error when trying to create the notification.
     * @throws IOException      if a problem occurred talking to the server.
     */
    public AlertNotification createNotification(AlertNotification alertNotification)
            throws GrafanaException, IOException {

        Response<AlertNotification> response =
                service.postNotification(config.apiKey(), alertNotification).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    /**
     * Updates a Grafana alert notification by id.
     *
     * @param id                the id of the notification to be updated.
     * @param alertNotification the notification values to be updated.
     * @return the newly updated notification
     * @throws GrafanaException if a notification does not exist for the given id.
     * @throws IOException      if a problem occurred talking to the server.
     */
    public AlertNotification updateNotification(Integer id, AlertNotification alertNotification)
            throws GrafanaException, IOException {

        Response<AlertNotification> response =
                service.putNotification(config.apiKey(), id, alertNotification).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else if (response.code() == HTTP_NOT_FOUND) {
            throw new GrafanaException("Notification id: " + id + " does not exist");
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    /**
     * Deletes Grafana notification by id.
     *
     * @param id the id of the notification to delete.
     * @return confirmation of the deleted notification.
     * @throws GrafanaException if Grafana returns an error when trying to delete the notification.
     * @throws IOException      if a problem occurred talking to the server.
     */
    public String deleteNotification(Integer id) throws GrafanaException, IOException {

        Response<GrafanaMessage> response = service.deleteNotification(config.apiKey(), id).execute();

        if (response.isSuccessful()) {
            return response.body().message();
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }

    /**
     * Searches for a Grafana alert with a given id.
     *
     * @param id the id of the alert to search for.
     * @return the alert with the given id.
     * @throws GrafanaException if Grafana returns an error when trying to find the alert.
     * @throws IOException      if a problem occurred talking to the server.
     */
    public DashboardPanelAlert getAlert(Integer id) throws GrafanaException, IOException {

        Response<DashboardPanelAlert> response = service.getAlert(config.apiKey(), id).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw GrafanaException.withErrorBody(response.errorBody());
        }
    }
}