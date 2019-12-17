package com.dotcms.security.secret;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ServiceSecrets implements Serializable {

    private final String serviceKey;

    private final Map<String,Secret> secrets;

    @JsonCreator
    private ServiceSecrets(@JsonProperty("serviceKey") final String serviceKey,
            @JsonProperty("secrets") final Map<String, Secret> secrets) {
        this.serviceKey = serviceKey;
        this.secrets = secrets;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public Map<String, Secret> getSecrets() {
        return secrets;
    }

    public static class Builder {

        private Map<String,Secret> secretMap = new HashMap<>();
        private String serviceKey;

        ServiceSecrets build(){
            return new ServiceSecrets(serviceKey, ImmutableMap.copyOf(secretMap));
        }

        Builder withServiceKey(final String serviceKey){
            this.serviceKey = serviceKey;
            return this;
        }

        Builder withHiddenSecret(final String name, final String value) {
            secretMap.put(
                    name, Secret.newSecret(value.toCharArray(), SecretType.STRING, true)
            );
            return this;
        }

        Builder withHiddenSecret(final String name, final boolean value){
            secretMap.put(
                    name, Secret.newSecret(String.valueOf(value).toCharArray(), SecretType.BOOL, true)
            );
            return this;
        }

        Builder withSecret(final String name, final String value){
            secretMap.put(
                    name, Secret.newSecret(value.toCharArray(), SecretType.STRING, false)
            );
            return this;
        }

        Builder withSecret(final String name, final boolean value){
            secretMap.put(
                    name, Secret.newSecret(String.valueOf(value).toCharArray(), SecretType.STRING, false)
            );
            return this;
        }

        Builder withSecret(final String name, final Secret secret){
            secretMap.put(name, secret);
            return this;
        }

    }

}
