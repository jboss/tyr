package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.Format;
import org.xstefank.model.yaml.Row;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.regex.Matcher;

public class RequiredRowsCheck implements Check {

    private static final Logger log = Logger.getLogger(RequiredRowsCheck.class);

    private List<Row> rows;

    public RequiredRowsCheck(List<Row> rows) {
        this.rows = rows;
    }

    @Override
    public String check(JsonNode payload) {
        log.info("checking required rows");
        List<Row> requiredRows = new ArrayList<>(rows);
        String description = payload.get(Utils.PULL_REQUEST).get(Utils.BODY).asText();
        Scanner scanner = new Scanner(description);

        while (scanner.hasNextLine() && !requiredRows.isEmpty()) {
            String line = scanner.nextLine();
            for (Iterator<Row> it = requiredRows.iterator(); it.hasNext(); ) {
                Row row = it.next();
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
            for (Row row : requiredRows) {
                joiner.add(row.getMessage());
            }

            return joiner.toString();
        }
        return null;
    }

}
