package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.FormatYAML;
import org.xstefank.model.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.regex.Matcher;

public class RequiredRowsCheck implements Check {

    private static final Logger log = Logger.getLogger(RequiredRowsCheck.class);

    private List<FormatYAML.Row> rows;

    public RequiredRowsCheck(List<FormatYAML.Row> rows) {
        this.rows = rows;
    }

    @Override
    public String check(JsonNode payload) {
        log.info("checking required rows");
        List<FormatYAML.Row> requiredRows = new ArrayList<>(rows);
        String description = payload.get(Utils.PULL_REQUEST).get(Utils.BODY).asText();
        Scanner scanner = new Scanner(description);

        while (scanner.hasNextLine() && !requiredRows.isEmpty()) {
            String line = scanner.nextLine();
            for (Iterator<FormatYAML.Row> it = requiredRows.iterator(); it.hasNext(); ) {
                FormatYAML.Row row = it.next();
                Matcher matcher = row.getPattern().matcher(line);
                if (matcher.matches()) {
                    System.out.println("matched line " + line);
                    requiredRows.remove(row);
                    break;
                }
            }
        }

        if (!requiredRows.isEmpty()) {
            StringJoiner joiner = new StringJoiner(", ");
            for (FormatYAML.Row row : requiredRows) {
                joiner.add(row.getMessage());
            }

            return joiner.toString();
        }
        return null;
    }

}
