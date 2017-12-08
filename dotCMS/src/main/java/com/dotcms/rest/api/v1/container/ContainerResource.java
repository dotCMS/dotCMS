package com.dotcms.rest.api.v1.container;

import static com.dotcms.util.CollectionsUtils.map;

import com.dotcms.repackage.com.google.common.annotations.VisibleForTesting;
import com.dotcms.repackage.com.google.common.collect.Lists;
import com.dotcms.repackage.javax.ws.rs.Consumes;
import com.dotcms.repackage.javax.ws.rs.DefaultValue;
import com.dotcms.repackage.javax.ws.rs.GET;
import com.dotcms.repackage.javax.ws.rs.Path;
import com.dotcms.repackage.javax.ws.rs.PathParam;
import com.dotcms.repackage.javax.ws.rs.Produces;
import com.dotcms.repackage.javax.ws.rs.QueryParam;
import com.dotcms.repackage.javax.ws.rs.core.Context;
import com.dotcms.repackage.javax.ws.rs.core.MediaType;
import com.dotcms.repackage.javax.ws.rs.core.Response;
import com.dotcms.repackage.org.apache.commons.beanutils.BeanUtils;
import com.dotcms.repackage.org.glassfish.jersey.server.JSONP;
import com.dotcms.rest.InitDataObject;
import com.dotcms.rest.WebResource;
import com.dotcms.rest.annotation.NoCache;
import com.dotcms.rest.exception.mapper.ExceptionMapperUtil;
import com.dotcms.util.PaginationUtil;
import com.dotcms.util.pagination.ContainerPaginator;
import com.dotcms.util.pagination.OrderDirection;
import com.dotcms.uuid.shorty.ShortType;
import com.dotcms.uuid.shorty.ShortyId;

import com.dotmarketing.beans.ContainerStructure;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.web.WebAPILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.containers.model.Container;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.services.ContainerServices;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.PageMode;
import com.dotmarketing.util.VelocityUtil;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.liferay.portal.model.User;

/**
 * This resource provides all the different end-points associated to information
 * and actions that the front-end can perform on the {@link com.dotmarketing.portlets.containers.model.Container}.
 *
 */
@Path("/v1/containers")
public class ContainerResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private final PaginationUtil paginationUtil;
    private final WebResource webResource;


    
    
    
    
    public ContainerResource() {
        this(new WebResource(),
                new PaginationUtil(new ContainerPaginator()));
    }

    @VisibleForTesting
    public ContainerResource(final WebResource webResource,
                             final PaginationUtil paginationUtil) {
        this.webResource = webResource;
        this.paginationUtil = paginationUtil;
    }

    /**
     * Return a list of {@link com.dotmarketing.portlets.containers.model.Container}, entity response syntax:.
     *
     * <code>
     *  {
     *      contentTypes: array of Container
     *      total: total number of Containers
     *  }
     * <code/>
     *
     * Url sintax: api/v1/container?filter=filter-string&page=page-number&per_page=per-page&ordeby=order-field-name&direction=order-direction&host=host-id
     *
     * where:
     *
     * <ul>
     *     <li>filter-string: just return Container who content this pattern into its title</li>
     *     <li>page: page to return</li>
     *     <li>per_page: limit of items to return</li>
     *     <li>ordeby: field to order by</li>
     *     <li>direction: asc for upward order and desc for downward order</li>
     *     <li>host: filter by host's id</li>
     * </ul>
     *
     * Url example: v1/container?filter=test&page=2&orderby=title
     *
     * @param request
     * @return
     */
    @GET
    @JSONP
    @NoCache
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    public final Response getContainers(@Context final HttpServletRequest request,
                                          @QueryParam(PaginationUtil.FILTER)   final String filter,
                                          @QueryParam(PaginationUtil.PAGE) final int page,
                                          @QueryParam(PaginationUtil.PER_PAGE) final int perPage,
                                          @DefaultValue("title") @QueryParam(PaginationUtil.ORDER_BY) final String orderBy,
                                          @DefaultValue("ASC") @QueryParam(PaginationUtil.DIRECTION) final String direction,
                                          @QueryParam(ContainerPaginator.HOST_PARAMETER_ID) final String hostId) {

        final InitDataObject initData = webResource.init(null, true, request, true, null);
        final User user = initData.getUser();

        try {
            final Map<String, Object> extraParams = map(ContainerPaginator.HOST_PARAMETER_ID, hostId);
            return this.paginationUtil.getPage(request, user, filter, page, perPage, orderBy,
                    OrderDirection.valueOf(direction), extraParams);
        } catch (Exception e) {
            Logger.error(this, e.getMessage(), e);
            return ExceptionMapperUtil.createResponse(e, Response.Status.INTERNAL_SERVER_ERROR);

        }
    }
    
    
    
    @GET
    @JSONP
    @NoCache
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    @Path("/{containerId}/content/{contentletId}")
    public final Response containerContent(@Context final HttpServletRequest req,@Context final HttpServletResponse res,
            @PathParam("containerId") final String containerId,
            @PathParam("contentletId") final String contentletId) 
                    throws DotDataException, DotSecurityException, ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

        final InitDataObject initData = webResource.init(true, req, true);
        final User user = initData.getUser();

        Language id = WebAPILocator.getLanguageWebAPI().getLanguage(req);
        ShortyId containerShorty = APILocator.getShortyAPI().getShorty(containerId).orElseGet(() -> {throw new ResourceNotFoundException("Can't find Container" + containerId);} );
        
        ShortyId contentShorty = APILocator.getShortyAPI().getShorty(contentletId).orElseGet(() -> {throw new ResourceNotFoundException("Can't find contentlet:" + contentletId);} );
        
        PageMode mode = PageMode.get(req);
        
        Container container = (containerShorty.subType == ShortType.CONTAINER) 
            ? APILocator.getContainerAPI().find(containerId, user, mode==PageMode.ANON)
                    : (mode.showLive) 
                        ? (Container) APILocator.getVersionableAPI().findLiveVersion(containerShorty.longId, user, mode==PageMode.ANON)
                        :(Container) APILocator.getVersionableAPI().findWorkingVersion(containerShorty.longId, user, mode==PageMode.ANON);
                        
        Identifier containerIdentifier = APILocator.getIdentifierAPI().find(container);
        
        org.apache.velocity.context.Context context = VelocityUtil.getWebContext(req, res);
        context.remove("EDIT_MODE");

        final Contentlet contentlet = (contentShorty.subType == ShortType.CONTENTLET) ?  APILocator.getContentletAPI().find(contentShorty.longId, user, mode.showLive) 
                : APILocator.getContentletAPI().findContentletByIdentifier(contentShorty.longId, mode==PageMode.ANON, id.getId(), user, mode==PageMode.ANON);
        
        context.put("contentletList" + container.getIdentifier(), Lists.newArrayList(contentlet.getIdentifier()));
        
        
        container = (Container) BeanUtils.cloneBean(container);
        container.setPreLoop(null);
        container.setPostLoop(null);
        

        StringWriter out = new StringWriter();  

        VelocityUtil.getEngine().evaluate(context, out, this.getClass().getName(),ContainerServices.buildVelocity(container, containerIdentifier, false));        
                
        Map<String, String> response = new HashMap<>();
        response.put("render", out.toString());
        
        final Response.ResponseBuilder responseBuilder = Response.ok(response);

        
        
        
        return responseBuilder.build();
        
    }
    
    

    
    
}