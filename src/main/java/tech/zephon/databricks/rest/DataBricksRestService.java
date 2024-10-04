package tech.zephon.databricks.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tech.zephon.databricks.models.groups.AddUserToGroupOperation;
import tech.zephon.databricks.models.groups.AddUserToGroupRootBean;
import tech.zephon.databricks.models.groups.AddUserToGroupValue;
import tech.zephon.databricks.models.groups.GroupRootBean;
import tech.zephon.databricks.models.groups.RemoveUserFromGroupOperation;
import tech.zephon.databricks.models.groups.RemoveUserFromGroupRootBean;
import tech.zephon.databricks.models.groups.SPGroup;
import tech.zephon.databricks.models.groups.SPGroupList;
import tech.zephon.databricks.models.groups.SPModifyUserGroup;
import tech.zephon.databricks.models.modifyuser.ModifyUserRootBean;
import tech.zephon.databricks.models.modifyuser.Operations;
import tech.zephon.databricks.models.modifyuser.StatusValue;
import tech.zephon.databricks.models.user.Emails;
import tech.zephon.databricks.models.user.Groups;
import tech.zephon.databricks.models.user.Name;
import tech.zephon.databricks.models.user.SPUser;
import tech.zephon.databricks.models.user.SPUserList;
import tech.zephon.databricks.models.user.UserRootBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Owner
 */
@Path("/databricks")
public class DataBricksRestService {

    @Context
    private ServletContext servletContext;

    @Context
    private HttpHeaders httpHeaders;

    private static final Logger logger = LogManager.getLogger(DataBricksRestService.class);

    @Path("/aggregate/{userId}")
    @GET
    @Produces({"application/json"})
    public Response getUsers(@PathParam("userId") String nativeIdentity) throws Exception {
        logger.debug("getUser Service Data Received: " + nativeIdentity);
        NativeDatabricksClient client = null;
        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();
            Response resp = client.getUser(nativeIdentity);
            logger.debug("Status:" + resp.getStatus());
            if (resp.getStatus() != 200)
            {
                return resp;
            }
            String str = resp.readEntity(String.class);
            logger.debug("Entity:" + str);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode jroot = mapper.readTree(str);
            SPUser spUser = new SPUser();
            if (jroot != null && jroot.isObject()) {
                ObjectNode node = (ObjectNode) jroot;
                UserRootBean user = mapper.convertValue(node, UserRootBean.class);
                spUser.setDatabricksId(user.getId());
                spUser.setUserId(user.getUsername());
                if (user.getName() != null) {
                    spUser.setFirstName(user.getName().getGivenname());
                    spUser.setLastName(user.getName().getFamilyName());
                }
                spUser.setDisplayName(user.getDisplayname());
                boolean active = user.getActive();
                if (active) {
                    spUser.setStatus("A");
                } else {
                    spUser.setStatus("D");
                }
                List<Groups> groups = user.getGroups();
                if (groups != null && !groups.isEmpty()) {
                    List<String> spGroups = new ArrayList();
                    for (Groups group : groups) {
                        spGroups.add(group.getValue());
                    }
                    spUser.setGroups(spGroups);
                }
                List<Emails> emails = user.getEmails();
                String spEmail = "NONE";
                if (emails != null && !emails.isEmpty()) {
                    for (Emails email : emails) {
                        if (email.getPrimary() && email.getValue() != null) {
                            spEmail = email.getValue();
                        }
                    }
                    spUser.setEmail(spEmail);
                }
            }
            logger.debug("Return user:" + spUser);
            return Response.status(200).entity(spUser).build();

        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Path("/aggregate")
    @GET
    @Produces({"application/json"})
    public Response getUsers() throws Exception {
        SPUserList allUsers = new SPUserList();
        NativeDatabricksClient client = null;
        logger.debug("Headers:" + this.httpHeaders.getRequestHeaders());
        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();
            Response resp = client.getUsers();
            logger.debug("Status:" + resp.getStatus());
            if (resp.getStatus() != 200)
            {
                return resp;
            }
            String str = resp.readEntity(String.class);
            logger.debug("Entity:" + str);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode jroot = mapper.readTree(str);
            JsonNode resources = jroot.path("Resources");
            List<SPUser> users = new ArrayList();

            if (!resources.isMissingNode() && resources.isArray()) {
                ArrayNode userArr = (ArrayNode) resources;
                for (int i = 0; i < userArr.size(); i++) {
                    JsonNode juser = userArr.get(i);
                    logger.debug("User:" + juser);
                    UserRootBean user = mapper.convertValue(juser, UserRootBean.class);
                    SPUser spUser = new SPUser();
                    spUser.setDatabricksId(user.getId());
                    spUser.setUserId(user.getUsername());
                    if (user.getName() != null) {
                        spUser.setFirstName(user.getName().getGivenname());
                        spUser.setLastName(user.getName().getFamilyName());
                    }
                    spUser.setDisplayName(user.getDisplayname());
                    boolean active = user.getActive();
                    if (active) {
                        spUser.setStatus("A");
                    } else {
                        spUser.setStatus("D");
                    }
                    List<Groups> groups = user.getGroups();
                    if (groups != null && !groups.isEmpty()) {
                        List<String> spGroups = new ArrayList();
                        for (Groups group : groups) {
                            spGroups.add(group.getValue());
                        }
                        spUser.setGroups(spGroups);
                    }
                    List<Emails> emails = user.getEmails();
                    String spEmail = "NONE";
                    if (emails != null && !emails.isEmpty()) {
                        for (Emails email : emails) {
                            if (email.getPrimary() && email.getValue() != null) {
                                spEmail = email.getValue();
                            }
                        }
                        spUser.setEmail(spEmail);
                    }
                    users.add(spUser);
                    //logger.debug("User:" + user.getDisplayname());

                }
            }
            allUsers.setUsers(users);
            return Response.status(200).entity(allUsers).build();

        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    @Path("/aggregateGroups")
    @GET
    @Produces({"application/json"})
    public Response getGroups() throws Exception {
        logger.debug("getGroups Service Data Received: ");
        SPGroupList groups = new SPGroupList();
        NativeDatabricksClient client = null;
        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();
            Response resp = client.getGroups();
            logger.debug("Status:" + resp.getStatus());
            if (resp.getStatus() != 200)
            {
                return resp;
            }
            String str = resp.readEntity(String.class);
            logger.debug("Entity:" + str);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode jroot = mapper.readTree(str);
            JsonNode resources = jroot.path("Resources");
            List<SPGroup> spGroups = new ArrayList();

            if (!resources.isMissingNode() && resources.isArray()) {
                ArrayNode groupArr = (ArrayNode) resources;
                for (int i = 0; i < groupArr.size(); i++) {
                    JsonNode jgroup = groupArr.get(i);
                    GroupRootBean group = mapper.convertValue(jgroup, GroupRootBean.class);
                    SPGroup spGroup = new SPGroup();
                    spGroup.setName(group.getDisplayname());
                    spGroup.setId(group.getId());
                    spGroups.add(spGroup);
                }
            }
            groups.setGroups(spGroups);
            return Response.status(200).entity(groups).build();

        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }
    
    @Path("/aggregateGroups/{groupId}")
    @GET
    @Produces({"application/json"})
    public Response getGroup(@PathParam("groupId") String groupId) throws Exception {
        logger.debug("getGroups Service Data Received: ");
        NativeDatabricksClient client = null;
        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();
            Response resp = client.getGroup(groupId);
            logger.debug("Status:" + resp.getStatus());
            if (resp.getStatus() != 200)
            {
                return resp;
            }
            String str = resp.readEntity(String.class);
            logger.debug("Group resp Entity:" + str);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode jroot = mapper.readTree(str);
            SPGroup spGroup = new SPGroup();
            if (jroot != null && jroot.isObject()) {
                ObjectNode node = (ObjectNode) jroot;
                GroupRootBean group = mapper.convertValue(node, GroupRootBean.class);
                spGroup.setName(group.getDisplayname());
                spGroup.setId(group.getId());
            }
            return Response.status(200).entity(spGroup).build();

        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    @POST
    @Path("/createService")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response createUser(SPUser user) throws Exception {

        logger.debug("Create Service Data Received: " + user);
        NativeDatabricksClient client = null;

        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();

            UserRootBean dbUser = new UserRootBean();
            if ("A".equalsIgnoreCase(user.getStatus())) {
                dbUser.setActive(true);
            } else {
                dbUser.setActive(false);
            }
            dbUser.setDisplayname(user.getDisplayName());
            dbUser.setUsername(user.getUserId());

            Name userName = new Name();
            userName.setGivenname(user.getFirstName());
            userName.setFamilyName(user.getLastName());
            dbUser.setName(userName);

            Emails email = new Emails();
            email.setType("work");
            email.setPrimary(true);
            email.setValue(user.getEmail());
            List<Emails> allEmails = new ArrayList();
            allEmails.add(email);
            dbUser.setEmails(allEmails);

            Response resp = client.createUser(dbUser);
            if (resp.getStatus() != 201) {
                String respData = resp.readEntity(String.class);
                logger.error("Databricks Error:" + resp.getStatus() + ":" + respData);
                throw new Exception("Invalid response from databricks:");

            }
            String respData = resp.readEntity(String.class);
            logger.debug("Databricks:" + respData);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserRootBean newUser = mapper.readValue(respData, UserRootBean.class);
            SPUser spUser = ConvertUtils.convertUser(newUser);
            logger.debug("new user ID:" + spUser);
            return Response.status(200).entity(spUser).build();

        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    @POST
    @Path("/enableUserService")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response enableUser(SPUser user) throws Exception {
        NativeDatabricksClient client = null;
        String id = user.getDatabricksId();
        Operations operation = new Operations();
        operation.setOp("replace");
        StatusValue value = new StatusValue();
        value.setActive(true);

        operation.setValue(value);
        List<Operations> operations = new ArrayList();
        operations.add(operation);
        ModifyUserRootBean rootBean = new ModifyUserRootBean();
        rootBean.setOperations(operations);

        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();
            Response resp = client.modifyUser(id, rootBean);
            if (resp.getStatus() != 200) {
                String respData = resp.readEntity(String.class);
                logger.error("Databricks Error:" + resp.getStatus() + ":" + respData);
                throw new Exception("Invalid response from databricks:");

            }
            return Response.status(200).entity("OK").build();
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @POST
    @Path("/disableUserService")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response disableUser(SPUser user) throws Exception {
        NativeDatabricksClient client = null;
        String id = user.getDatabricksId();
        Operations operation = new Operations();
        operation.setOp("replace");
        StatusValue value = new StatusValue();
        value.setActive(false);

        operation.setValue(value);
        List<Operations> operations = new ArrayList();
        operations.add(operation);
        ModifyUserRootBean rootBean = new ModifyUserRootBean();
        rootBean.setOperations(operations);

        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();
            Response resp = client.modifyUser(id, rootBean);
            if (resp.getStatus() != 200) {
                String respData = resp.readEntity(String.class);
                logger.error("Databricks Error:" + resp.getStatus() + ":" + respData);
                throw new Exception("Invalid response from databricks:");

            }
            return Response.ok().build();
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @POST
    @Path("/addEntitlementService")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response addEntitlement(SPModifyUserGroup userGroup) throws Exception {

        logger.debug("addEntitlement:" + userGroup);
        
        try
        {
            SPGroup tmpGroup = InternalCalls.getGroup(httpHeaders, userGroup.getGroupId());
            if (tmpGroup.getId() == null)
            {
                logger.error("Invalid Group Id");
                throw new Exception("Invalid Group Id:" + userGroup);
            }
            userGroup.setUserDisplayName(tmpGroup.getName());
        }
        catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        }
        
        NativeDatabricksClient client = null;
        AddUserToGroupRootBean groupRoot = new AddUserToGroupRootBean();
        List<tech.zephon.databricks.models.groups.Groups> groups = new ArrayList();
        tech.zephon.databricks.models.groups.Groups group = new tech.zephon.databricks.models.groups.Groups();
        group.setType("direct");
        group.setValue(userGroup.getDatabricksId());
        group.setDisplay(userGroup.getUserDisplayName());
        groups.add(group);
        AddUserToGroupValue value = new AddUserToGroupValue();
        value.setMembers(groups);
        AddUserToGroupOperation operation = new AddUserToGroupOperation();
        operation.setOp("add");
        operation.setValue(value);
        List<AddUserToGroupOperation> operations = new ArrayList();
        operations.add(operation);
        groupRoot.setOperations(operations);
        operation.setOp("add");
        
        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();
            Response resp = client.addUserToGroup(userGroup.getGroupId(), groupRoot);
            if (resp.getStatus() != 200) {
                String respData = resp.readEntity(String.class);
                logger.error("Databricks Error:" + resp.getStatus() + ":" + respData);
                throw new Exception("Invalid response from databricks:");

            }
            return Response.ok().build();
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @POST
    @Path("/removeEntitlementService")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response removeEntitlement(SPModifyUserGroup userGroup) throws Exception {

        logger.debug("removeEntitlement:" + userGroup);
        
        String groupName = userGroup.getUserDisplayName();
        if (groupName == null)
        {
            groupName = userGroup.getGroupId();
        }
        try
        {
            SPGroup tmpGroup = InternalCalls.findGroup(httpHeaders, groupName);
            if (tmpGroup.getId() == null)
            {
                logger.error("Ignoring Invalid removeEntitlement Group Id:" + userGroup);
                return Response.ok().build();
            }
            userGroup.setGroupId(tmpGroup.getId());
        }
        catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        }
        
        NativeDatabricksClient client = null;
        RemoveUserFromGroupRootBean groupRoot = new RemoveUserFromGroupRootBean();
        RemoveUserFromGroupOperation operation = new RemoveUserFromGroupOperation();
        operation.setOp("remove");
        operation.setPath("members[value eq " + userGroup.getDatabricksId()+ "]");
        List<RemoveUserFromGroupOperation> operations = new ArrayList();
        operations.add(operation);
        groupRoot.setOperations(operations);

        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();
            Response resp = client.removeUserFromGroup(userGroup.getGroupId(), groupRoot);
            if (resp.getStatus() == 200 || resp.getStatus() == 404)
            {
                return Response.ok().build();
            }
            if (resp.getStatus() != 200) {
                String respData = resp.readEntity(String.class);
                logger.error("Databricks Error:" + resp.getStatus() + ":" + respData);
                throw new Exception("Invalid response from databricks:");

            }
            return Response.ok().build();
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @DELETE
    @Path("/remove/{userId}")
    @Produces({"application/json"})
    public Response remove(@PathParam("userId") String nativeIdentity) throws Exception {
        NativeDatabricksClient client = null;
        try {
            client = new NativeDatabricksClient(this.httpHeaders);
            client.connect();
            Response resp = client.deleteUser(nativeIdentity);
            if (resp.getStatus() != 204) {
                String respData = resp.readEntity(String.class);
                logger.error("Databricks Error:" + resp.getStatus() + ":" + respData);
                throw new Exception("Invalid response from databricks:");

            }
            return Response.ok().build();
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Path("/test")
    @GET
    @Produces({"text/html"})
    public Response getStartingPage() {
        String output = "<h1>Hello there! Testing success!<h1><p>Simple RESTful Get Service is running ... <br>Ping @ " + new Date().toString() + "</p<br>";
        logger.error("error test me completed:" + this.httpHeaders.getRequestHeaders());
        logger.debug("debug test me completed:" + this.httpHeaders);
        logger.info("info test me completed:" + this.httpHeaders);
        logger.warn("warn test me completed:" + this.httpHeaders);
        return Response.status(200).entity(output).build();
    }

}
