package tech.zephon.databricks.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tech.zephon.databricks.models.groups.GroupRootBean;
import tech.zephon.databricks.models.groups.SPGroup;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ForesterF
 */
public class InternalCalls {
    
    private static final Logger logger = LogManager.getLogger(InternalCalls.class);
    
    public static SPGroup getGroup(HttpHeaders httpHeaders,String groupId) throws Exception {
        logger.debug("getGroups Service Data Received: ");
        NativeDatabricksClient client = null;
        try {
            client = new NativeDatabricksClient(httpHeaders);
            client.connect();
            Response resp = client.getGroup(groupId);
            logger.debug("Status:" + resp.getStatus());
            if (resp.getStatus() != 200)
            {
                logger.error("getGroup error:" + resp);
                return new SPGroup();
            }
            String str = resp.readEntity(String.class);
            logger.debug("Group resp Entity:" + str);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode jroot = mapper.readTree(str);
            SPGroup spGroup = new SPGroup();
            if (jroot != null && jroot.isObject()) {
                ObjectNode node = (ObjectNode) jroot;
                GroupRootBean group = mapper.convertValue(node, GroupRootBean.class);
                spGroup.setName(group.getDisplayname());
                spGroup.setId(group.getId());
            }
            return spGroup;

        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }
    
    
    public static SPGroup findGroup(HttpHeaders httpHeaders,String groupName) throws Exception {
        logger.debug("findGroup Service Data Received: ");
        NativeDatabricksClient client = null;
        try {
            client = new NativeDatabricksClient(httpHeaders);
            client.connect();
            Response resp = client.findGroup(groupName);
            logger.debug("Status:" + resp.getStatus());
            if (resp.getStatus() != 200)
            {
                logger.error("findGroup error:" + resp);
                return new SPGroup();
            }
            String str = resp.readEntity(String.class);
            logger.debug("Group resp Entity:" + str);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode jroot = mapper.readTree(str);
            JsonNode resources = jroot.path("Resources");
            SPGroup spGroup = new SPGroup();
            if (!resources.isMissingNode() && resources.isArray()) {
                ArrayNode groupArr = (ArrayNode) resources;
                for (int i = 0; i < groupArr.size(); i++) {
                    JsonNode jgroup = groupArr.get(i);
                    GroupRootBean group = mapper.convertValue(jgroup, GroupRootBean.class);
                    spGroup.setName(group.getDisplayname());
                    spGroup.setId(group.getId());
                }
            }
            logger.debug("Group resp bean:" + spGroup);
            return spGroup;

        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }
    
}
