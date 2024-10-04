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
public class RemoveUserFromGroupRootBean {
    
    private List<String> schema = new ArrayList();
    
    @JsonProperty("Operations")
    private List<RemoveUserFromGroupOperation> operations;
    
    private String patchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
    
    
     public List<String> getSchema() {
        return schema;
    }

    public void setSchema(List<String> schema) {
        this.schema = schema;
    }

    public List<RemoveUserFromGroupOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<RemoveUserFromGroupOperation> operations) {
        this.operations = operations;
    }
    
    
    
    public RemoveUserFromGroupRootBean() {
        
        this.schema.add(patchOp);
    }
    
}
