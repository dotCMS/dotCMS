package com.dotcms.rest.api.v1.page;


import com.dotcms.rendering.velocity.viewtools.navigation.NavResult;
import com.dotcms.rendering.velocity.viewtools.navigation.NavTool;
import com.dotcms.repackage.com.google.common.annotations.VisibleForTesting;
import com.dotcms.repackage.javax.ws.rs.GET;
import com.dotcms.repackage.javax.ws.rs.Path;
import com.dotcms.repackage.javax.ws.rs.PathParam;
import com.dotcms.repackage.javax.ws.rs.Produces;
import com.dotcms.repackage.javax.ws.rs.QueryParam;
import com.dotcms.repackage.javax.ws.rs.core.Context;
import com.dotcms.repackage.javax.ws.rs.core.MediaType;
import com.dotcms.repackage.javax.ws.rs.core.Response;
import com.dotcms.rest.InitDataObject;
import com.dotcms.rest.ResponseEntityView;
import com.dotcms.rest.WebResource;
import com.dotcms.rest.annotation.NoCache;
import com.dotcms.rest.api.v1.authentication.ResponseUtil;
import com.dotcms.rest.exception.ForbiddenException;
import com.dotcms.rest.exception.mapper.ExceptionMapperUtil;

import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.PermissionLevel;
import com.dotmarketing.business.web.WebAPILocator;
import com.dotmarketing.exception.DoesNotExistException;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.StringUtils;
import com.dotmarketing.util.VelocityUtil;

import com.liferay.util.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.tools.view.context.ChainedContext;
import org.apache.velocity.tools.view.context.ViewContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.model.User;

@Path("/v1/nav")
public class NavResource {


    private final WebResource webResource;


    /**
     * Creates an instance of this REST end-point.
     */
    public NavResource() {
        this(new WebResource());
    }

    @VisibleForTesting
    protected NavResource(final WebResource webResource) {

        this.webResource = webResource;

    }

    /**
     * Returns navigation metadata in JSON format for objects that have been marked show on menu
     *
     * <pre>
     * Format:
     * http://localhost:8080/api/v1/nav/{start-url}?depth={}
     * <br/>
     * Example - will send the navigation under the /about-us folder, 2 levels deep
     * http://localhost:8080/api/v1/nav/about-us?depth=2
     * </pre>
     *
     * @param request The {@link HttpServletRequest} object.
     * @param response The {@link HttpServletResponse} object.
     * @param uri The path to the HTML Page whose information will be retrieved.
     * @param depth - an int for how many levels to include
     * @return a json representation of the navigation
     */
    @NoCache
    @GET
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    @Path("/{uri: .*}")
    public final Response loadJson(@Context final HttpServletRequest request, @Context final HttpServletResponse response,
            @PathParam("uri") final String uri, @QueryParam("depth") final String depth) {

        final InitDataObject auth = webResource.init(false, request, true);
        final User user = auth.getUser();

        try {
            final int maxDepth = Integer.parseInt(depth);

            final Host h =WebAPILocator.getHostWebAPI().getCurrentHostNoThrow(request);
            APILocator.getPermissionAPI().checkPermission(h, PermissionLevel.READ, user);

            final ViewContext ctx = new ChainedContext(VelocityUtil.getBasicContext(), request, response, Config.CONTEXT);

            final String path = (!uri.startsWith("/")) ? "/" + uri : uri;

            final NavTool tool = new NavTool();
            tool.init(ctx);
            final NavResult nav = tool.getNav(path);

            final Map<String, Object> navMap = (nav!=null) ? navToMap(nav, maxDepth, 1) : new HashMap<>();

            if(navMap.isEmpty()) {
                throw new DoesNotExistException("The provided URL was not found");
            }

            final ObjectMapper mapper = new ObjectMapper();
            final String json = mapper.writeValueAsString(navMap);

            return Response.ok(new ResponseEntityView(json)).build(); // 200

        } catch (Exception e) {
            Logger.error(this.getClass(),"Exception on NavResource exception message: " + e.getMessage(), e);
            return ResponseUtil.mapExceptionResponse(e);
        }
    }


    private Map<String, Object> navToMap(final NavResult nav, final int maxDepth, final int currentDepth) throws Exception {

        final Map<String, Object> navMap = new HashMap<String, Object>();
        navMap.put("title", nav.getTitle());
        navMap.put("target", nav.getTarget());
        navMap.put("code", nav.getCodeLink());
        navMap.put("folder", nav.getFolderId());
        navMap.put("host", nav.getHostId());
        navMap.put("href", nav.getHref());
        navMap.put("languageId", nav.getLanguageId());
        navMap.put("order", nav.getOrder());
        navMap.put("type", nav.getType());
        navMap.put("hash", nav.hashCode());

        if (currentDepth < maxDepth) {
            final List<Map<String, Object>> childs = new ArrayList<>();
            for (final NavResult child : nav.getChildren()) {
                int startDepth=currentDepth;
                childs.add(navToMap(child, maxDepth, ++startDepth));
            }
            navMap.put("children", childs);
        }

        return navMap;
    }

}