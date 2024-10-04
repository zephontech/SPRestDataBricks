/* Copyright 2023 freecodeformat.com */
package tech.zephon.databricks.models.groups;

import com.fasterxml.jackson.annotation.JsonProperty;

/* Time: 2023-06-29 9:42:1 @author freecodeformat.com @website http://www.freecodeformat.com/json2javabean.php */
public class Meta {

    @JsonProperty("resourceType")
    private String resourcetype;
    public void setResourcetype(String resourcetype) {
         this.resourcetype = resourcetype;
     }
     public String getResourcetype() {
         return resourcetype;
     }

}