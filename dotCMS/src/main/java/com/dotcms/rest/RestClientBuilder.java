package com.dotcms.rest;

import com.dotcms.publisher.util.TrustFactory;
import com.dotcms.repackage.javax.ws.rs.client.Client;
import com.dotcms.repackage.javax.ws.rs.client.ClientBuilder;
import com.dotcms.repackage.org.glassfish.jersey.media.multipart.MultiPartFeature;
import com.dotmarketing.util.Config;

/**
 * This class provides an instance of a Jersey REST Client. This client allows
 * the developer to access the different RESTful services provided by dotCMS
 * regarding interaction with contents, workflows, Rule Engine, Push Publishing,
 * Integrity Checker, among many other features.
 * 
 * @author Daniel Silva
 * @version 1.0
 * @since Jun 4, 2015
 *
 */
public class RestClientBuilder {

    private RestClientBuilder() {}

    private static class RestClientLazyHolder {
        static final Client REST_CLIENT = newClient();

        /**
         * Creates a new instance of the REST client used to access the RESTful
         * services available in the dotCMS back-end.
         *
         * @return The REST {@link Client} object.
         */
        private static Client newClient() {
            TrustFactory tFactory = new TrustFactory();

            Client client;
            String truststorePath = Config.getStringProperty("TRUSTSTORE_PATH", "");
            if (truststorePath != null && !truststorePath.trim().equals("")) {
                client = ClientBuilder.newBuilder().sslContext(tFactory.getSSLContext())
                        .hostnameVerifier(tFactory.getHostnameVerifier())
                        .build();
            } else {
                client = ClientBuilder.newClient();
            }
            client.register(MultiPartFeature.class);
            return client;
        }
    }

    public static Client getClient() {
        return RestClientLazyHolder.REST_CLIENT;
    }

}
