package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.RegexDefinition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class RequiredRowsCheck implements Check {

    private List<RegexDefinition> rows;

    public RequiredRowsCheck(List<RegexDefinition> rows) {
        this.rows = rows;
    }

    @Override
    public String check(JsonNode payload) {
        List<RegexDefinition> requiredRows = new ArrayList<>(rows);
        String description = payload.get(Utils.PULL_REQUEST).get(Utils.BODY).asText();
        Scanner scanner = new Scanner(description);

        while (scanner.hasNextLine() && !requiredRows.isEmpty()) {
            String line = scanner.nextLine();
            for (Iterator<RegexDefinition> it = requiredRows.iterator(); it.hasNext(); ) {
                RegexDefinition row = it.next();
                Matcher matcher = row.getPattern().matcher(line);
                if (matcher.matches()) {
                    requiredRows.remove(row);
                    break;
                }
            }
        }

        if (!requiredRows.isEmpty()) {
            return requiredRows.get(0).getMessage();
        }

        return null;
    }

}
