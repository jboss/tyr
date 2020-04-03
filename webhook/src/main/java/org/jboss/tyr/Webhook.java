package org.jboss.tyr;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/")
@ApplicationScoped
public class Webhook {

    private Client client = ClientBuilder.newClient();

    @ConfigProperty(name = "tyr.webhook.urls")
    Optional<List<String>> URLs;

    @POST
    @Path("/pull-request")
    @Consumes(MediaType.APPLICATION_JSON)
    public void processRequest(JsonObject payload) {
        URLs.ifPresent(urls ->
            urls.stream()
                .parallel()
                .map(URI::create)
                .forEach(uri -> sendPayload(uri, payload)));
    }

    private void sendPayload(URI uri, JsonObject payload) {
        client.target(uri)
            .request()
            .post(Entity.json(payload));
    }
}
