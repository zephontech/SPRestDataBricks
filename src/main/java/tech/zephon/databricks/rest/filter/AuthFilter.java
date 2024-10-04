/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.zephon.databricks.rest.filter;

import java.io.IOException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Owner
 */
@Provider
public class AuthFilter implements ContainerRequestFilter {
    
    private static final Logger logger = LogManager.getLogger(AuthFilter.class.getName());
    
    
    
    @Override
    public void filter(ContainerRequestContext containerRequest) throws IOException {
        
        logger.debug("*********************** AuthFilter ****************");
        
        //Get the authentification passed in HTTP headers parameters
        String auth = containerRequest.getHeaderString("Authorization");
        String env = containerRequest.getHeaderString("ENVIRONMENT");
        
        logger.debug("Env:" + env + "  :  " + auth);
        //If the user does not have the right (does not provide any HTTP Basic Auth)
        
        if (env == null)
        {
            logger.error("Missing ENVIRONMENT header");
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
        if(auth == null) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
        
        AuthenticationService authSvc = new AuthenticationService();
        try
        {
            boolean status = authSvc.authenticate(auth,env);
            if (!status)
            {
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
           
        }
        catch(Exception e)
        {
            logger.error("Authentication failed!:" + e.getMessage(),e);
            throw new WebApplicationException(Status.UNAUTHORIZED);
            
        }
    }
}

    
    

