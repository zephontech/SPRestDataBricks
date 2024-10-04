package tech.zephon.databricks.rest;

import tech.zephon.databricks.models.groups.AddUserToGroupRootBean;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import tech.zephon.databricks.models.groups.GroupRootBean;
import tech.zephon.databricks.models.groups.RemoveUserFromGroupRootBean;
import tech.zephon.databricks.models.modifyuser.ModifyUserRootBean;
import tech.zephon.databricks.models.user.UserRootBean;
import javax.servlet.ServletContext;
import javax.ws.rs.core.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Jersey REST client generated for REST resource:RestRulesService [/rules]<br>
 * USAGE:
 * <pre>
 *        NewJerseyClient client = new NewJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Owner
 */
public class NativeDatabricksClient {

    private WebTarget webTarget;
    private String scimTarget = "https://yourserver.databricks.com/api/2.0/preview/scim/v2";
    private String clientid = "yourid";
    private Client client;
    
    private static final Logger logger = LogManager.getLogger(DataBricksRestService.class);

    public NativeDatabricksClient(ServletContext servletContext) {
        
        this.scimTarget = servletContext.getInitParameter("proxy-server");
        this.clientid = servletContext.getInitParameter("proxy-server-clientid");
        logger.debug("webxml target:" + this.scimTarget);
        logger.debug("webxml clientid:" + this.clientid);
        
    }
    
    
    public NativeDatabricksClient(HttpHeaders httpHeaders) {
        
        this.scimTarget = httpHeaders.getHeaderString("proxy-server");
        this.clientid = httpHeaders.getHeaderString("proxy-server-clientid");
        logger.debug("header target:" + this.scimTarget);
        logger.debug("header clientid:" + this.clientid);
        
    }
    
    
    
    
    public void connect() {
        
        //Client client = ClientBuilder.newClient();
        client = ClientBuilder
                        .newClient()
                        .register(OAuth2ClientSupport.feature(this.clientid));
        //HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(userName,password);
        //client.register(feature);
        // allow the 
        client.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
        webTarget = client.target(this.scimTarget);
        //webTarget.register(new JacksonJsonProvider(JsonUtils.createObjectMapper()));
        
    }

    public void setClientId(String clientid) {
        this.clientid = clientid;
    }

    public void setScimTarget(String scimTarget) {
        this.scimTarget = scimTarget;
    }
    
    
   
    public Response getSchemas() throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget.path("Schemas").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        return response;
    }
    
    public Response getResourceTypes() throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget.path("ResourceTypes").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        return response;
    }
    
    public Response getUsers() throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget.path("Users").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        return response;
    }
    
    public Response getGroups() throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget
                .path("Groups")
                .request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        return response;
    }
    
    public Response findGroup(String groupName) throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget
                .path("Groups")
                .queryParam("filter", "displayName eq " + groupName)
                .request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        return response;
    }
    
    public Response createUser(UserRootBean user) throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget
                .path("Users")
                .request(MediaType.APPLICATION_JSON);
                
        Response response = invocationBuilder.post(Entity.json(user));
        return response;
    }
    
    public Response createGroup(GroupRootBean group) throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget
                .path("Groups")
                .request(MediaType.APPLICATION_JSON);
                
        Response response = invocationBuilder.post(Entity.json(group));
        return response;
    }
    
    public Response deleteUser(String id) throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget
                .path("Users/" + id)
                .request(MediaType.APPLICATION_JSON);
                
        Response response = invocationBuilder.delete();
        return response;
    }
    
    public Response deleteGroup(String id) throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget
                .path("Groups/" + id)
                .request(MediaType.APPLICATION_JSON);
                
        Response response = invocationBuilder.delete();
        return response;
    }
    
    public Response getUser(String id) throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget
                .path("Users/" + id)
                .request(MediaType.APPLICATION_JSON);
                
        Response response = invocationBuilder.get();
        return response;
    }
    
    public Response getGroup(String id) throws ClientErrorException {
        Invocation.Builder invocationBuilder = this.webTarget
                .path("Groups/" + id)
                .request(MediaType.APPLICATION_JSON);
                
        Response response = invocationBuilder.get();
        return response;
    }
    
    
    public Response modifyUser(String id,ModifyUserRootBean mods)
    {
        
        Response response = this.webTarget
                .path("Users/" + id)
                .request().method("PATCH",Entity.json(mods));
                
        return response;
        
    }
    
    public Response addUserToGroup(String id,AddUserToGroupRootBean mods)
    {
        
        Response response = this.webTarget
                .path("Groups/" + id)
                .request().method("PATCH",Entity.json(mods));
                
        return response;
        
    }
    
    public Response removeUserFromGroup(String id,RemoveUserFromGroupRootBean mods)
    {
        
        Response response = this.webTarget
                .path("Groups/" + id)
                .request().method("PATCH",Entity.json(mods));
                
        return response;
        
    }
    
    public void setWebTarget(WebTarget webTarget) {
        this.webTarget = webTarget;
    }
    
    public void close() {
        client.close();
    }
    
}
