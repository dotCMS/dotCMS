package com.dotcms.rest.api.v2.languages;

import com.dotcms.repackage.com.google.common.annotations.VisibleForTesting;
import com.dotcms.rest.api.v1.authentication.ResponseUtil;
import com.dotcms.util.DotPreconditions;
import com.dotmarketing.exception.DoesNotExistException;
import com.dotmarketing.util.PortletID;
import com.dotmarketing.util.StringUtils;
import com.google.common.collect.ImmutableList;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.beanutils.BeanUtils;
import org.glassfish.jersey.server.JSONP;
import com.dotcms.rest.InitDataObject;
import com.dotcms.rest.ResponseEntityView;
import com.dotcms.rest.WebResource;
import com.dotcms.rest.annotation.InitRequestRequired;
import com.dotcms.rest.annotation.NoCache;
import com.dotcms.rest.api.v1.I18NForm;
import com.dotcms.util.I18NUtil;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.ApiProvider;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.languagesmanager.business.LanguageAPI;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.util.Logger;
import com.liferay.portal.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Language end point
 */
@Path("/v2/languages")
public class LanguagesResource {

    private final LanguageAPI languageAPI;
    private final WebResource webResource;
    private final com.dotcms.rest.api.v1.languages.LanguagesResource oldLanguagesResource;

    public LanguagesResource() {
        this(APILocator.getLanguageAPI(),
                new WebResource(new ApiProvider()));
    }

    @VisibleForTesting
    public LanguagesResource(final LanguageAPI languageAPI,
                                final WebResource webResource) {

        this.languageAPI  = languageAPI;
        this.webResource  = webResource;
        this.oldLanguagesResource = new com.dotcms.rest.api.v1.languages.LanguagesResource(languageAPI, webResource, I18NUtil.INSTANCE);
    }

    @GET
    @JSONP
    @NoCache
    @Produces({MediaType.APPLICATION_JSON})
    /**
     * return a array with all the languages
     */
    public Response  list(@Context final HttpServletRequest request, @Context final HttpServletResponse response, @QueryParam("contentInode") final String contentInode)
            throws DotDataException, DotSecurityException {

        Logger.debug(this, () -> String.format("listing languages %s", request.getRequestURI()));

        final InitDataObject init = webResource.init(request, response, true);
        final User user = init.getUser();

        final List<Language> languages = contentInode != null ?
                languageAPI.getAvailableContentLanguages(contentInode, user) :
                languageAPI.getLanguages();

        return Response.ok(new ResponseEntityView(ImmutableList.copyOf(languages))).build();
    }

    /**
     * Persists a new {@link Language}
     *
     * @param request HttpServletRequest
     * @param languageForm LanguageForm
     * @return Response
     */
    @POST
    @JSONP
    @NoCache
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    public final Response saveLanguage(@Context final HttpServletRequest request,
            @Context final HttpServletResponse response,
            final LanguageForm languageForm) {
        this.webResource.init(null, request, response,
                true, PortletID.LANGUAGES.toString());
        try {
            DotPreconditions.notNull(languageForm,"Expected Request body was empty.");
            final Language language = saveOrUpdateLanguage(null, languageForm);
            return Response.ok(new ResponseEntityView(language)).build(); // 200
        } catch (Exception e) {
            final String langCode = languageForm == null ? "" : languageForm.getLanguageCode();
            Logger.error(this.getClass(), "Exception on save, Language code: " + langCode
                    + ", exception message: " + e.getMessage(), e);
            return ResponseUtil.mapExceptionResponse(e);
        }
    }

    @POST
    @JSONP
    @NoCache
    @Path("/i18n")
    @InitRequestRequired
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    public Response getMessages(@Context HttpServletRequest request,
                                final I18NForm i18NForm) {
        return oldLanguagesResource.getMessages(request, i18NForm);
    }

    private Language saveOrUpdateLanguage(final String languageId, final LanguageForm form) {
        final Language newLanguage = new Language();

        if (StringUtils.isSet(languageId)) {
            try {
                final Language origLanguage = this.languageAPI.getLanguage(languageId);
                BeanUtils.copyProperties(newLanguage, origLanguage);
            } catch (Exception e) {
                Logger.debug(this.getClass(), () -> "Unable to find language with id:" + languageId);
                throw new DoesNotExistException(e.getMessage(), e);
            }
        }

        newLanguage.setLanguageCode(form.getLanguageCode());
        newLanguage.setLanguage(form.getLanguage());
        newLanguage.setCountryCode(form.getCountryCode());
        newLanguage.setCountry(form.getCountry());

        this.languageAPI.saveLanguage(newLanguage);
        return newLanguage;
    }
}
