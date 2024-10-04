/* Copyright 2023 freecodeformat.com */
package tech.zephon.databricks.models.user;

/* Time: 2023-06-29 9:16:52 @author freecodeformat.com @website http://www.freecodeformat.com/json2javabean.php */
public class Emails {

    private String type;
    private String value;
    private boolean primary;
    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setValue(String value) {
         this.value = value;
     }
     public String getValue() {
         return value;
     }

    public void setPrimary(boolean primary) {
         this.primary = primary;
     }
     public boolean getPrimary() {
         return primary;
     }

}