/* Copyright 2023 freecodeformat.com */
package tech.zephon.databricks.models.groups;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
/* Time: 2023-06-29 9:42:1 @author freecodeformat.com @website http://www.freecodeformat.com/json2javabean.php */
public class GroupRootBean {

    private Meta meta;
    @JsonProperty("displayName")
    private String displayname;
    private List<Roles> roles;
    private List<Groups> groups;
    private String id;
    public void setMeta(Meta meta) {
         this.meta = meta;
     }
     public Meta getMeta() {
         return meta;
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

}