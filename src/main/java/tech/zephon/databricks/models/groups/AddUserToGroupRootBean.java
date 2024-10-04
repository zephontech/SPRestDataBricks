/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tech.zephon.databricks.models.groups;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ForesterF
 */
public class AddUserToGroupRootBean {
    
    private List<String> schema = new ArrayList();
    
    @JsonProperty("Operations")
    private List<AddUserToGroupOperation> operations;
    
    private String patchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
    
    
     public List<String> getSchema() {
        return schema;
    }

    public void setSchema(List<String> schema) {
        this.schema = schema;
    }

    public List<AddUserToGroupOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<AddUserToGroupOperation> operations) {
        this.operations = operations;
    }
    
    
    
    public AddUserToGroupRootBean() {
        
        this.schema.add(patchOp);
    }
}
