/* Copyright 2023 freecodeformat.com */
package tech.zephon.databricks.models.user;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
/* Time: 2023-06-29 9:16:52 @author freecodeformat.com @website http://www.freecodeformat.com/json2javabean.php */
public class UserRootBean {

    private List<Emails> emails;
    private List<Entitlements> entitlements;
    @JsonProperty("displayName")
    private String displayname;
    private List<Roles> roles;
    private Name name;
    private boolean active;
    private List<Groups> groups;
    private String id;
    @JsonProperty("userName")
    private String username;
    public void setEmails(List<Emails> emails) {
         this.emails = emails;
     }
     public List<Emails> getEmails() {
         return emails;
     }

    public void setEntitlements(List<Entitlements> entitlements) {
         this.entitlements = entitlements;
     }
     public List<Entitlements> getEntitlements() {
         return entitlements;
     }

    public void setDisplayname(String displayname) {
         this.displayname = displayname;
     }
     public String getDisplayname() {
         return displayname;
     }

    public void setRoles(List<Roles> roles) {
         this.roles = roles;
     }
     public List<Roles> getRoles() {
         return roles;
     }

    public void setName(Name name) {
         this.name = name;
     }
     public Name getName() {
         return name;
     }

    public void setActive(boolean active) {
         this.active = active;
     }
     public boolean getActive() {
         return active;
     }

    public void setGroups(List<Groups> groups) {
         this.groups = groups;
     }
     public List<Groups> getGroups() {
         return groups;
     }

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setUsername(String username) {
         this.username = username;
     }
     public String getUsername() {
         return username;
     }

}