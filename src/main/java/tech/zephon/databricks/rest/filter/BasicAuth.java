/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.zephon.databricks.rest.filter;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Owner
 */
public class BasicAuth {
    
    public static String[] decode(String auth) {
        //Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
        auth = auth.replaceFirst("[B|b]asic ", "");
  
        //Decode the Base64 into byte[]
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);
  
        //If the decode fails in any case
        if(decodedBytes == null || decodedBytes.length == 0){
            return null;
        }
  
        //Now we can convert the byte[] into a splitted array :
        //  - the first one is login,
        //  - the second one password
        return new String(decodedBytes).split(":", 2);
    }
    
}
