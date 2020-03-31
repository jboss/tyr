package org.jboss.tyr.whitelist;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TestBean {

    public String getHello() {
        return "Hello";
    }
}
