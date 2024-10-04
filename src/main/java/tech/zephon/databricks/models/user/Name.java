/* Copyright 2023 freecodeformat.com */
package tech.zephon.databricks.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;

/* Time: 2023-06-29 9:16:52 @author freecodeformat.com @website http://www.freecodeformat.com/json2javabean.php */
public class Name {

    @JsonProperty("givenName")
    private String givenname;
    
    @JsonProperty("familyName")
    private String familyName;
    
    public void setGivenname(String givenname) {
         this.givenname = givenname;
     }
     public String getGivenname() {
         return givenname;
     }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
     
     

}