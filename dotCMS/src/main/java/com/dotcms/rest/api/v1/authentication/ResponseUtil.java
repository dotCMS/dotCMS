package com.dotcms.rest.api.v1.authentication;

import com.dotcms.exception.ExceptionUtil;
import com.dotcms.repackage.javax.ws.rs.core.Response;
import com.dotcms.rest.ErrorResponseHelper;
import com.dotcms.rest.exception.SecurityException;
import com.dotcms.rest.exception.mapper.ExceptionMapperUtil;
import com.dotmarketing.portlets.contentlet.business.DotContentletValidationException;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.SecurityLogger;
import com.dotmarketing.util.UtilMethods;
import com.liferay.portal.language.LanguageException;
import com.liferay.portal.language.LanguageUtil;
import com.liferay.portal.model.User;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;

import static com.dotcms.exception.ExceptionUtil.BAD_REQUEST_EXCEPTIONS;
import static com.dotcms.exception.ExceptionUtil.NOT_FOUND_EXCEPTIONS;
import static com.dotcms.exception.ExceptionUtil.SECURITY_EXCEPTIONS;
import static com.dotcms.exception.ExceptionUtil.causedBy;
import static com.dotcms.exception.ExceptionUtil.getRootCause;

/**
 * Just a helper to encapsulate AuthenticationResource functionality.
 * @author jsanca
 */
public class ResponseUtil implements Serializable {

    public static ResponseUtil INSTANCE =
            new ResponseUtil();


    private ResponseUtil() {

    }

    /**
     * Get Error response based on a status and message key
     * This support is a single message
     *
     * @param request
     * @param status
     * @param userId
     * @param messageKey
     * @return Response
     */
    public Response getErrorResponse(final HttpServletRequest request,
                                     final Response.Status status,
                                     final Locale locale,
                                     final String userId,
                                     final String messageKey,
                                     final Object... arguments) {

        return ErrorResponseHelper.INSTANCE.getErrorResponse(status, locale, messageKey, arguments);
    }

    /**
     * Get the translation of the message key in the specified locale, if the locale is null
     * then the message is translated into the default user language
     *
     * @param locale           Current user language
     * @param messageKey       Message key to be translated
     * @param messageArguments (Optional) if the message require some argument
     * @return
     */
    public static String getFormattedMessage(Locale locale, String messageKey, Object... messageArguments) {
        String message;
        try {
            message = (UtilMethods.isSet(locale)) ?
                    LanguageUtil.get(locale, messageKey) :
                    LanguageUtil.get((User) null, messageKey);

            return MessageFormat.format(message, messageArguments);
        } catch (LanguageException e) {
            Logger.error(ResponseUtil.class, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * In case of an invalid access attempt this will fulfill the response so it can be interpreted by a front-end consumer
     * Takes an exception and tries to find the most appropriate match code to send an error response.
     * @param e Exception
     * @return Response
     */
    public static Response mapExceptionResponse(final Exception e){
        // case for non-authenticated users (bad credentials)
        if(causedBy(e, SecurityException.class)){
            return createNonAuthenticatedResponse(e);
        }

        if(causedBy(e, SECURITY_EXCEPTIONS)){
            return createUnAuthorizedResponse(e);
        }

        if(causedBy(e, NOT_FOUND_EXCEPTIONS)){
            return ExceptionMapperUtil.createResponse(e, Response.Status.NOT_FOUND);
        }

        if(e instanceof DotContentletValidationException){
            final DotContentletValidationException ve = DotContentletValidationException.class.cast(e);
            return ExceptionMapperUtil.createResponse(Response.Status.BAD_REQUEST, ve);
        }

        final Throwable rootCause = getRootCause(e);
        if( rootCause instanceof DotContentletValidationException){
            final DotContentletValidationException ve = DotContentletValidationException.class.cast(rootCause);
            return ExceptionMapperUtil.createResponse(Response.Status.BAD_REQUEST, ve);
        }

        if(causedBy(e, BAD_REQUEST_EXCEPTIONS)){
            return ExceptionMapperUtil.createResponse(e, Response.Status.BAD_REQUEST);
        }

        return ExceptionMapperUtil.createResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
    }

    /**
     * In case of an invalid access attempt this will fulfill the response so it can be interpreted by a front-end consumer
     * @param e Exception
     * @return Response
     */
    private static Response createUnAuthorizedResponse (final Exception e) {

        SecurityLogger.logInfo(ResponseUtil.class, e.getMessage());
        return ExceptionMapperUtil.createResponse(e, Response.Status.FORBIDDEN);
    }

    /**
     * In case of an invalid access attempt this will fulfill the response so it can be interpreted by a front-end consumer
     * @param e Exception
     * @return Response
     */
    private static Response createNonAuthenticatedResponse (final Exception e) {

        SecurityLogger.logInfo(ResponseUtil.class, e.getMessage());
        return ExceptionMapperUtil.createResponse(e, Response.Status.UNAUTHORIZED);
    }

} // E:O:F:ResponseUtil.
