package tech.zephon.databricks.test;

import tech.zephon.databricks.models.groups.SPModifyUserGroup;
import tech.zephon.databricks.models.user.SPUser;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author ForesterF
 */
public class SPDatabricksClient {
    
    
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "https://0aaee1e5:8443/SPRestDataBricks/rest/databricks";
    private MultivaluedMap headers;
    
    public SPDatabricksClient() {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI);
                //.path("target");
    }
    
    public SPDatabricksClient(String path) {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path(path);
                //.path("target");
    }

    public Response createUser(SPUser requestEntity) throws ClientErrorException {
        return webTarget.path("createService")
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), Response.class);
    }
    
    
    public Response deleteUser(String id) throws ClientErrorException {
        return webTarget.path("remove/" + id)
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .delete();
    }
    
    public Response getUsers() throws ClientErrorException {
        return webTarget
                .path("aggregate")
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .get();
    }
    
    public Response getUser(String id) throws ClientErrorException {
        return webTarget
                .path("aggregate").path(id)
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .get();
    }
    
    public Response getGroupss() throws ClientErrorException {
        return webTarget
                .path("aggregateGroups")
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .get();
    }
    
    public Response getGroup(String id) throws ClientErrorException {
        return webTarget
                .path("aggregateGroups").path(id)
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .get();
    }
    
    public Response enableUser(SPUser requestEntity)
    {
        return webTarget.path("enableUserService")
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), Response.class);
    }
    
    public Response disableUser(SPUser requestEntity)
    {
        return webTarget.path("disableUserService")
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), Response.class);
    }
    
    
    public Response addUserToGroup(SPModifyUserGroup requestEntity)
    {
        return webTarget.path("addEntitlementService")
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), Response.class);
    }
    
    public Response removeUserFromGroup(SPModifyUserGroup requestEntity)
    {
        return webTarget.path("removeEntitlementService")
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), Response.class);
    }

    public void close() {
        client.close();
    }

    public void setHeaders(MultivaluedMap headers) {
        this.headers = headers;
    }
    
    
    
}
