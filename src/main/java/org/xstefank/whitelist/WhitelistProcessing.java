package org.xstefank.whitelist;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xstefank.ci.CILoader;
import org.xstefank.ci.ContinuousIntegration;
import org.xstefank.model.PersistentList;
import org.xstefank.model.TyrProperties;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.FormatConfig;

public class WhitelistProcessing {

    public static final boolean IS_WHITELISTING_ENABLED =
            TyrProperties.getBooleanProperty(Utils.WHITELIST_ENABLED);

    private static final List<ContinuousIntegration> continuousIntegrations = new ArrayList<>();

    private final PersistentList userList;
    private final PersistentList adminList;

    private final List<Command> commands;

    public WhitelistProcessing(FormatConfig config) {
        String dirName = System.getProperty(Utils.JBOSS_CONFIG_DIR);
        userList = new PersistentList(dirName, Utils.USERLIST_FILE_NAME);
        adminList = new PersistentList(dirName, Utils.ADMINLIST_FILE_NAME);
        commands = getCommands(config);
        loadCIs(config);
    }

    public void processPRComment(JsonNode issuePayload) {
        if (issuePayload.get(Utils.ISSUE).has(Utils.PULL_REQUEST) &&
                issuePayload.get(Utils.ACTION).asText().matches("created") &&
                !commands.isEmpty()) {
            String message = issuePayload.get(Utils.COMMENT).get(Utils.BODY).asText();
            for (Command command : commands) {
                if (message.matches(command.getCommandRegex())) {
                    command.process(issuePayload, this);
                }
            }
        }
    }

    public void triggerCI(JsonNode prPayload) {
        for (ContinuousIntegration CI : continuousIntegrations) {
            CI.triggerBuild(prPayload);
        }
    }

    public void triggerFailedCI(JsonNode prPayload) {
        for (ContinuousIntegration CI : continuousIntegrations) {
            CI.triggerFailedBuild(prPayload);
        }
    }

    public boolean isUserEligibleToRunCI(String username) {
        return userList.hasUsername(username) || adminList.hasUsername(username);
    }

    String getCommentAuthor(JsonNode issuePayload) {
        return issuePayload.get(Utils.COMMENT).get(Utils.USER).get(Utils.LOGIN).asText();
    }

    String getPRAuthor(JsonNode issuePayload) {
        return issuePayload.get(Utils.ISSUE).get(Utils.USER).get(Utils.LOGIN).asText();
    }

    boolean isUserOnAdminList(String username) {
        return adminList.hasUsername(username);
    }

    boolean isUserOnUserList(String username) {
        return userList.hasUsername(username);
    }

    void addUserToUserList(String username) {
        userList.addUser(username);
    }

    private List<Command> getCommands(FormatConfig config) {
        List<Command> commands = new ArrayList<>();

        Map<String, String> regexMap = config.getFormat().getCommands();
        if (regexMap == null || regexMap.isEmpty()) {
            return commands;
        }

        for (String key : regexMap.keySet()) {
            Command command = CommandsLoader.getCommand(key);
            if (command != null) {
                command.setCommandRegex(regexMap.get(key));
                commands.add(command);
            }
        }

        return commands;
    }

    private void loadCIs(FormatConfig config) {
        List<String> CIConfigList = config.getFormat().getCI();

        if (CIConfigList == null || CIConfigList.isEmpty()) {
            return;
        }

        for (String key : CIConfigList) {
            ContinuousIntegration CI = CILoader.getCI(key);
            if (CI != null) {
                CI.init();
                continuousIntegrations.add(CI);
            }
        }
    }
}