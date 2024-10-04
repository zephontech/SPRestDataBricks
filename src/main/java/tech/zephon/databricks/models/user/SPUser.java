/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tech.zephon.databricks.models.user;

import java.util.List;

/**
 *
 * @author ForesterF
 */
public class SPUser {
    
    private String databricksId;
    private String userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    private List<String> groups;
    private String status;
    

    public String getDatabricksId() {
        return databricksId;
    }

    public void setDatabricksId(String databricksId) {
        this.databricksId = databricksId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "SPUser{" + "databricksId=" + databricksId + ", userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", displayName=" + displayName + ", email=" + email + ", groups=" + groups + ", status=" + status + '}';
    }

    
}
