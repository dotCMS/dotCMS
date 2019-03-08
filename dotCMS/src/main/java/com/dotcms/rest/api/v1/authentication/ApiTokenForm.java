package com.dotcms.rest.api.v1.authentication;

import java.util.Map;

import com.dotcms.repackage.com.fasterxml.jackson.annotation.JsonProperty;
import com.dotcms.repackage.com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableMap;

@JsonDeserialize(builder = ApiTokenForm.Builder.class)
public class ApiTokenForm  {


    public final String userId;

    public final String tokenId;
    public final boolean showRevoked;
    public final int expirationSeconds;
    public final String network;
    public final Map<String,String> claims;

    private ApiTokenForm(Builder builder) {
        userId = builder.userId;
        tokenId = builder.tokenId;
        showRevoked = builder.showRevoked;
        network = builder.network;
        expirationSeconds = builder.expirationSeconds;
        claims=builder.claims;
    }

    public static final class Builder {
        @JsonProperty
        private String userId; // not present on create
        @JsonProperty
        private String tokenId;
        @JsonProperty
        private boolean showRevoked = false;
        @JsonProperty
        private String network;
        @JsonProperty
        public  Map<String,String> claims = ImmutableMap.of();
        @JsonProperty
        private int expirationSeconds=-1;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder tokenId(String tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        public Builder showRevoked(boolean showRevoked) {
            this.showRevoked = showRevoked;
            return this;
        }

        public Builder netmask(String network) {
            this.network = network;
            return this;
        }

        public Builder expirationSeconds(int expirationSeconds) {
            this.expirationSeconds = expirationSeconds;
            return this;
        }
        public Builder claims(Map<String,String> claims) {
            this.claims = claims;
            return this;
        }
        public ApiTokenForm build() {
            return new ApiTokenForm(this);
        }
    }
}

