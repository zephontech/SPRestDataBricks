package tech.zephon.databricks.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.zephon.databricks.models.groups.SPGroup;
import tech.zephon.databricks.models.groups.SPModifyUserGroup;
import tech.zephon.databricks.models.user.SPUser;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.junit.Test;

/**
 *
 * @author ForesterF
 */
public class TestUsers {
    
    
    private String proxyServer = "https://yourserver.databricks.com/api/2.0/preview/scim/v2";
    private String proxyclientId = "your id";
    
    @Test
    public void mainTest()
    {
        //this.deleteUser("100168");
        //this.createUser("MrJoe202@joe.com");
        //this.deleteUser("100155");
        //this.getUsers();
      // this.getGroup("100215");
       //this.getUser("100155");
        //this.enableUser("100155");
        //this.getUser("100155");
        //this.disableUser("100155");
        //this.getUser("100155");
        //this.addUserToGroup("100172", "100159","55TSB Frederick, Frederick (Contractor)");
        //this.getUser("100155");
        this.removeUserFromGroup("100172", "BMF","BMF");
        //this.getUser("100155");
    }    
    
    
    public void getUsers()
    {
        
        SPDatabricksClient client = this.getClient(null);
        Response resp = client.getUsers();
        System.out.println("Status:" + resp.getStatus());
        
        Object obj = resp.readEntity(Object.class);
        if (obj != null)
        {
            System.out.println("Type:" + obj.getClass().getName());
            LinkedHashMap arr = (LinkedHashMap)obj;
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList users = (ArrayList)arr.get("users");
            System.out.println("Type:" + users.getClass().getName());
            for(int i=0;i<users.size();i++)
            {
                Object obj2 = users.get(i);
                System.out.println("Type:" + obj2.getClass().getName());
                LinkedHashMap lhm = (LinkedHashMap)obj2;
                //System.out.println("Type:" + lhm.keySet());
                //System.out.println("Type:" + lhm.values());
                SPUser spUsder = objectMapper.convertValue(lhm, SPUser.class);
                System.out.println("Type:" + spUsder);
                
            }
        }
    }
    
    
    public void getGroups()
    {
        
        SPDatabricksClient client = this.getClient(null);
        Response resp = client.getGroupss();
        System.out.println("Status:" + resp.getStatus());
        
        Object obj = resp.readEntity(Object.class);
        if (obj != null)
        {
            System.out.println("Type:" + obj.toString());
            System.out.println("Type:" + obj.getClass().getName());
            LinkedHashMap arr = (LinkedHashMap)obj;
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList users = (ArrayList)arr.get("groups");
            System.out.println("Type:" + users.getClass().getName());
            for(int i=0;i<users.size();i++)
            {
                Object obj2 = users.get(i);
                System.out.println("Type:" + obj2.getClass().getName());
                LinkedHashMap lhm = (LinkedHashMap)obj2;
                //System.out.println("Type:" + lhm.keySet());
                //System.out.println("Type:" + lhm.values());
                SPGroup spGroup = objectMapper.convertValue(lhm, SPGroup.class);
                System.out.println("Type:" + spGroup);
                
            }
        }
    }
    
    public void createUser(String name)
    {
        SPDatabricksClient client = this.getClient(null);
        
        SPUser user = new SPUser();
        //user.setId("9999999999");
        user.setStatus("A");
        user.setDisplayName("Mr Joe99");
        user.setUserId(name);
        user.setFirstName("Joe");
        user.setLastName("Joe99");
        user.setEmail(name);
      
        Response resp = client.createUser(user);
        System.out.println("Status:" + resp.getStatus());
        if (resp.getStatus() != 200)
        {
            String err = resp.readEntity(String.class);
            System.out.println("ERROR:" + err);
            return;
        }
        SPUser respData = resp.readEntity(SPUser.class);
        System.out.println("New ID:" + respData.getDatabricksId());
    }
    
    public void enableUser(String id)
    {
        SPUser user = new SPUser();
        user.setDatabricksId(id);
        SPDatabricksClient client = this.getClient(null);
        Response resp = client.enableUser(user);
        System.out.println("Status:" + resp.getStatus());
        if (resp.getStatus() != 200)
        {
            String err = resp.readEntity(String.class);
            System.out.println("ERROR:" + err);
        }
        
    }
    
    public void disableUser(String id)
    {
        SPUser user = new SPUser();
        user.setDatabricksId(id);
        SPDatabricksClient client = this.getClient(null);
        Response resp = client.disableUser(user);
        System.out.println("Status:" + resp.getStatus());
        if (resp.getStatus() != 200)
        {
            String err = resp.readEntity(String.class);
            System.out.println("ERROR:" + err);
        }
        
    }
    
    public void addUserToGroup(String userId,String groupId,String userDisplayName) 
    {
        SPDatabricksClient client = this.getClient(null);
        SPModifyUserGroup userGroup = new SPModifyUserGroup();
        userGroup.setDatabricksId(userId);
        userGroup.setUserDisplayName(userDisplayName);
        userGroup.setGroupId(groupId);
        Response resp = client.addUserToGroup(userGroup);
        System.out.println("Status:" + resp.getStatus());
        if (resp.getStatus() != 200)
        {
            String err = resp.readEntity(String.class);
            System.out.println("ERROR:" + err);
        }
    }
    
    public void removeUserFromGroup(String userId,String groupId,String userDisplayName) 
    {
        SPDatabricksClient client = this.getClient(null);
        SPModifyUserGroup userGroup = new SPModifyUserGroup();
        userGroup.setDatabricksId(userId);
        userGroup.setUserDisplayName(userDisplayName);
        userGroup.setGroupId(groupId);
        Response resp = client.removeUserFromGroup(userGroup);
        System.out.println("Status:" + resp.getStatus());
        if (resp.getStatus() != 200)
        {
            String err = resp.readEntity(String.class);
            System.out.println("ERROR:" + err);
        }
    }
    
    public void deleteUser(String id)
    {
        SPDatabricksClient client = this.getClient(null);
        
        Response resp = client.deleteUser(id);
        
        System.out.println("Status:" + resp.getStatus());
        if (resp.getStatus() != 200)
        {
            String err = resp.readEntity(String.class);
            System.out.println("ERROR:" + err);
            return;
        }
        String respData = resp.readEntity(String.class);
        
        System.out.println("Status:" + respData);
        
    }
    
    
    public void getUser(String id)
    {
        SPDatabricksClient client = this.getClient(null);
        
        System.out.println("Running test");
        
        
        Response resp = client.getUser(id);
        
        System.out.println("Status:" + resp.getStatus());
        if (resp.getStatus() != 200)
        {
            String err = resp.readEntity(String.class);
            System.out.println("ERROR:" + err);
            return;
        }
        SPUser respData = resp.readEntity(SPUser.class);
        
        System.out.println("Status:" + respData);
        
    }
    
    public void getGroup(String id)
    {
        SPDatabricksClient client = this.getClient(null);
        
        System.out.println("Running test");
        
        
        Response resp = client.getGroup(id);
        
        System.out.println("Status:" + resp.getStatus());
        if (resp.getStatus() != 200)
        {
            String err = resp.readEntity(String.class);
            System.out.println("ERROR:" + err);
            return;
        }
        SPGroup respData = resp.readEntity(SPGroup.class);
        
        System.out.println("Status:" + respData);
        
    }
    
    public SPDatabricksClient getClient(String path)
    {
        MultivaluedMap headers = new MultivaluedHashMap();
        SPDatabricksClient client = null;
        String creds = "soapuser:tempSOAPpwd";
        String encoded = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
        System.out.println("Running test:" + encoded);
        headers.putSingle("Authorization", encoded);
        headers.putSingle("ENVIRONMENT","DEV");
        headers.putSingle("proxy-server",this.proxyServer);
        headers.putSingle("proxy-server-token",this.proxyclientId);
        if (path == null)
        {
            client = new SPDatabricksClient();
        }
        else
        {
            client = new SPDatabricksClient(path);
        }
        
        client.setHeaders(headers);
        return client;
    }
}
