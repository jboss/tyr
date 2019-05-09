package org.jboss.tyr.check.resource;

import org.jboss.tyr.Check;

import javax.json.JsonObject;
import java.util.concurrent.atomic.AtomicInteger;

public class DummyAdditionalCheck implements Check {

    private static final String MESSAGE = "Dummy check failure";
    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public String check(JsonObject payload) {
        counter.incrementAndGet();
        return MESSAGE;
    }

    public static int getCounterValue() {
        return counter.intValue();
    }

    public static String getMessage() {
        return MESSAGE;
    }
}
