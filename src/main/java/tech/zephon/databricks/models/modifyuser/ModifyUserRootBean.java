package tech.zephon.databricks.models.modifyuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author ForesterF
 */
public class ModifyUserRootBean {
    
    @JsonProperty("Operations")
    private List<Operations> operations;
    public void setOperations(List<Operations> operations) {
         this.operations = operations;
     }
     public List<Operations> getOperations() {
         return operations;
     }
}
