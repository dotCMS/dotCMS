package com.dotcms.auth.providers.jwt.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import com.dotmarketing.business.APILocator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableMap;
import com.liferay.portal.model.User;

import io.vavr.control.Try;

/**
 * Encapsulates all the different pieces of information that make up the JSON Web Token (JWT).
 * 
 * @author jsanca
 * @version 3.7
 * @since Jun 14, 2016
 */
public interface JWToken extends Serializable {

    /**
     * Gets the id of this token
     * @return
     */
    public String getId();

    public String getSubject();

    public ImmutableMap<String, Object> getClaims();

    public String getIssuer();
   
    public Date getModificationDate();

    public Date getExpiresDate();
    /**
     * gets an cidr network which is validated
     * when this token is used
     * @return
     */
    public String getAllowNetworks();
    /**
     * Optionally gets the user associated with this token.
     * If the user is not active, no user will be returned
     * @return
     */
    @JsonIgnore
    default Optional<User> getActiveUser(){
        User user = Try.of(()-> APILocator.getUserAPI().loadUserById(getUserId())).getOrNull();
        if(user!=null && user.isActive()) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
    /**
     * Returns the type of Token, either User or API
     * token
     * @return
     */
    default TokenType getTokenType() {
        return TokenType.getTokenType(getSubject());
    }
    /**
     * Returns the associated userId for the
     * token
     * @return
     */
    String getUserId();
    
    default boolean isExpired() {
        return getExpiresDate() ==null || getExpiresDate().before(new Date());
    }
    
    
    

} // E:O:F:JWTBean.
