/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tech.zephon.databricks.models.groups;

import java.util.List;

/**
 *
 * @author ForesterF
 */
public class SPModifyUserGroup {
    
    String userId;
    String databricksId;
    String groupId;
    String userDisplayName;
    private String groups;

    public String getDatabricksId() {
        return databricksId;
    }

    public void setDatabricksId(String databricksId) {
        this.databricksId = databricksId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "SPModifyUserGroup{" + "userId=" + userId + ", databricksId=" + databricksId + ", groupId=" + groupId + ", userDisplayName=" + userDisplayName + ", groups=" + groups + '}';
    }

    
    
    
    
}
