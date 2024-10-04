/* Copyright 2023 freecodeformat.com */
package tech.zephon.databricks.models.user;
import java.util.Date;

/* Time: 2023-06-29 9:16:52 @author freecodeformat.com @website http://www.freecodeformat.com/json2javabean.php */
public class Groups {

    private String display;
    private String type;
    private String value;
    private String $ref;
    public void setDisplay(String display) {
         this.display = display;
     }
     public String getDisplay() {
         return display;
     }

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

    public void set$ref(String $ref) {
         this.$ref = $ref;
     }
     public String get$ref() {
         return $ref;
     }

}