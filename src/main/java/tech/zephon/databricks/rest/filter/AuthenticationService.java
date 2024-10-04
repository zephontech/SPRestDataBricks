/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tech.zephon.databricks.rest.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.Cache;
import org.ehcache.CacheManager;


/**
 *
 * @author ForesterF
 */
public class AuthenticationService {
    
    private static final String REGION = "us-gov-west-1";
    private static final Logger logger = LogManager.getLogger(AuthenticationService.class.getName());
    
    enum Environment 
    {
        PROD("BEARSSOAP/Prod"),
        TEST("BEARSSOAP/Test"),
        DEV("BEARSSOAP/Dev");
        
        private String name;
        
        Environment(String env)
        {
            this.name = env;
        }
        
        public String getName()
        {
            return name;
        }
        
    }
    

    public boolean authenticate(String authCredentials,String environment) throws Exception {
        if (null == authCredentials) {
            return false;
        }
        // header value format will be "Basic encodedstring" for Basic
        // authentication. Example "Basic YWRtaW46YWRtaW4="
        final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            throw e;
        }
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        environment = environment.toUpperCase();
        Environment envSecretName = Environment.valueOf(environment);
        if (envSecretName == null)
        {
            logger.error("Invalid Environment:" + environment);
            return false;
        }
        logger.debug("SecretName:" + envSecretName.getName());
        Map awsCreds = this.getSecret(envSecretName.getName());
        if (awsCreds == null || awsCreds.isEmpty())
        {
            logger.error("Invalid awsCreds:" + environment);
            return false;
        }
        logger.debug("headeruser:" + username);
        logger.debug("headerpw:" + password);
        
        logger.debug("awscreds:" + awsCreds);
        String awsUser = (String)awsCreds.get("username");
        String awspw = (String)awsCreds.get("password");
        
        if (awsUser == null || awspw == null)
        {
            logger.error("Invalid awsCreds:" + environment);
            return false;
        }
        if (awsUser.equals(username) && awspw.equals(password))
        {
            logger.debug("Valid Credentials");
            return true;
        }
        // we have fixed the userid and password as admin
        // call some UserService/LDAP here
        boolean authenticationStatus = true;
        return authenticationStatus;
    }
    
    private Map getSecret(String name) throws Exception {
      String secret = null;
      
      Map cachedCreds = this.getCacheCredentials();
      if (cachedCreds != null)
      {
          return cachedCreds;
      }
      
      AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().withRegion(REGION).build();
      GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(name);
      GetSecretValueResult getSecretValueResult = null;
      try {
        getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        if (getSecretValueResult != null && getSecretValueResult.getSecretString() != null) {
          secret = getSecretValueResult.getSecretString();
          logger.debug("Secret:" + secret);
          //taskResult.addMessage(new Message(Message.Type.Info,"secret in main: "+secret ,null));
          ObjectMapper mapper = new ObjectMapper();
          JsonNode root = mapper.readTree(secret);
          Map creds = new HashMap();
          if (root.isObject())
          {
              ObjectNode node = (ObjectNode)root;
              if (node.has("soap.pwd"))
              {
                  creds.put("password",node.get("soap.pwd").textValue());
              }
              if (node.has("soap.user"))
              {
                  creds.put("username",node.get("soap.user").textValue());
              }
              if (creds.size() > 1)
              {
                  this.setCacheCredentials(creds);
                  logger.debug("Setting cached credentials");
                  return creds;
              }
          }
          String userName = root.path("soap.user").asText();
          String pw = root.path("soap.pwd").asText();
          creds.put("password",pw);
          creds.put("username",userName);
          return creds;
        }
      } catch (Exception e) {
        throw e;
        
      }
      return new HashMap();
    }


    public Map getCacheCredentials()
    {
        Map creds = new HashMap();
        CacheManager cacheManager = CacheInitializer.getCacheManager();
        if (cacheManager == null)
        {
            logger.error("No CacheManager Found!");
            return null;
        }
        Cache appCache = cacheManager.getCache(CacheInitializer.getCacheName(), String.class, String.class);
        String uid = (String)appCache.get("username");
        String upw = (String)appCache.get("password");
        if (uid == null || uid.trim().isEmpty())
        {
            return null;
        }
        if (upw == null || upw.trim().isEmpty())
        {
            return null;
        }
        logger.debug("using cached credentials");
        creds.put("username",uid);
        creds.put("password",upw);
        return creds;
    }
    
    public void setCacheCredentials(Map creds)
    {
        CacheManager cacheManager = CacheInitializer.getCacheManager();
        if (cacheManager == null)
        {
            logger.error("No CacheManager Found!");
            return;
        }
        String uid = (String)creds.get("username");
        String upw = (String)creds.get("password");
        Cache appCache = cacheManager.getCache(CacheInitializer.getCacheName(), String.class, String.class);
        appCache.put("username", uid);
        appCache.put("password", upw);
    }
}
