package org.jboss.tyr.cli;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.tyr.check.TemplateChecker;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

@QuarkusMain
public class TyrMain implements QuarkusApplication {

    @Inject
    TemplateChecker templateChecker;

    @Override
    public int run(String... args) throws Exception {
        JsonReader reader = Json.createReader(new StringReader(args[0]));
        JsonObject prJson = reader.readObject();
        String result = templateChecker.processPullRequest(prJson);
        System.out.println(">>> " + result);
        return result.isEmpty() ? 0 : 1;
    }
}
