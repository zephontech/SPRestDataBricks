package tech.zephon.databricks.models.modifyuser;

/**
 *
 * @author ForesterF
 */
public class Operations {
    
    private String op;
    //private String path;
    private StatusValue value;
    public void setOp(String op) {
         this.op = op;
     }
     public String getOp() {
         return op;
     }

    public StatusValue getValue() {
        return value;
    }

    public void setValue(StatusValue value) {
        this.value = value;
    }

   

}
