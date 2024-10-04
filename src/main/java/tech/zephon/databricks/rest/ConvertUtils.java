package tech.zephon.databricks.rest;

import tech.zephon.databricks.models.user.Emails;
import tech.zephon.databricks.models.user.Groups;
import tech.zephon.databricks.models.user.SPUser;
import tech.zephon.databricks.models.user.UserRootBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ForesterF
 */
public class ConvertUtils {

    public static SPUser convertUser(UserRootBean user) {
        SPUser spUser = new SPUser();
        spUser.setDatabricksId(user.getId());
        spUser.setUserId(user.getUsername());
        if (user.getName() != null) {
            spUser.setFirstName(user.getName().getGivenname());
            spUser.setLastName(user.getName().getFamilyName());
        }
        spUser.setDisplayName(user.getDisplayname());
        boolean active = user.getActive();
        if (active) {
            spUser.setStatus("A");
        } else {
            spUser.setStatus("D");
        }
        List<Groups> groups = user.getGroups();
        if (groups != null && !groups.isEmpty()) {
            List<String> spGroups = new ArrayList();
            for (Groups group : groups) {
                spGroups.add(group.getDisplay());
            }
            spUser.setGroups(spGroups);
        }
        List<Emails> emails = user.getEmails();
        String spEmail = "NONE";
        if (emails != null && !emails.isEmpty()) {
            for (Emails email : emails) {
                if (email.getPrimary() && email.getValue() != null) {
                    spEmail = email.getValue();
                }
            }
            spUser.setEmail(spEmail);
        }
        return spUser;
    }

}
