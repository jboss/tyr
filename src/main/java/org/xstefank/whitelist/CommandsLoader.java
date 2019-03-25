package org.xstefank.whitelist;

import java.util.HashMap;
import java.util.Map;

public class CommandsLoader {

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put(AddUserCommand.NAME, new AddUserCommand());
        commands.put(RetestCommand.NAME, new RetestCommand());
        commands.put(RetestFailedCommand.NAME, new RetestFailedCommand());
    }

    public static Command getCommand(String key) {
        return commands.get(key);
    }
}
