package com.dotcms.rest.elasticsearch;

import static com.dotcms.util.CollectionsUtils.map;

import com.dotcms.content.elasticsearch.business.ESSearchResults;
import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.enterprise.license.LicenseLevel;
import com.dotcms.repackage.javax.ws.rs.*;
import com.dotcms.repackage.javax.ws.rs.core.Context;
import com.dotcms.repackage.javax.ws.rs.core.MediaType;
import com.dotcms.repackage.javax.ws.rs.core.Response;
import com.dotcms.repackage.javax.ws.rs.core.Response.Status;
import com.dotcms.repackage.org.apache.commons.io.IOUtils;
import com.dotcms.repackage.org.codehaus.jettison.json.JSONArray;
import com.dotcms.repackage.org.codehaus.jettison.json.JSONException;
import com.dotcms.repackage.org.codehaus.jettison.json.JSONObject;
import com.dotcms.rest.*;
import com.dotcms.rest.exception.mapper.ExceptionMapperUtil;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.DotStateException;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.PageMode;

import com.liferay.portal.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Path("/es")
public class ESContentResourcePortlet extends BaseRestPortlet {

	ContentletAPI esapi = APILocator.getContentletAPI();
    private final WebResource webResource = new WebResource();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("search")
	public Response search(@Context HttpServletRequest request, @Context HttpServletResponse response, String esQueryStr) throws DotDataException, DotSecurityException{

		InitDataObject initData = webResource.init(null, true, request, false, null);
		HttpSession session = request.getSession();
		User user = initData.getUser();
		ResourceResponse responseResource = new ResourceResponse(initData.getParamsMap());



		PageMode mode = PageMode.get(request);

		JSONObject esQuery;

		try {
			esQuery = new JSONObject(esQueryStr);
		} catch (Exception e1) {
			Logger.warn(this.getClass(), "Unable to create JSONObject", e1);
			return ExceptionMapperUtil.createResponse(e1, Response.Status.BAD_REQUEST);
		}

		try {
			ESSearchResults esresult = esapi.esSearch(esQuery.toString(), mode.showLive, user, mode.showLive);
			
			JSONObject json = new JSONObject();
			JSONArray jsonCons = new JSONArray();

			for(Object x : esresult){
				Contentlet c = (Contentlet) x;
				try {
					jsonCons.put(ContentResource.contentletToJSON(c, request, response, "false", user));
				} catch (Exception e) {
					Logger.warn(this.getClass(), "unable JSON contentlet " + c.getIdentifier());
					Logger.debug(this.getClass(), "unable to find contentlet", e);
				}
			}

			try {
				json.put("contentlets", jsonCons);
			} catch (JSONException e) {
				Logger.warn(this.getClass(), "unable to create JSONObject");
				Logger.debug(this.getClass(), "unable to create JSONObject", e);
			}

			esresult.getContentlets().clear();
			json.append("esresponse", new JSONObject(esresult.getResponse().toString()));

			if ( request.getParameter("pretty") != null ) {
				return responseResource.response(json.toString(4));
			} else {
				return responseResource.response(json.toString());
			}

		}catch(DotStateException dse) {
	        if(LicenseUtil.getLevel() < LicenseLevel.STANDARD.level){
	            final String noLicenseMessage = "Unable to execute ES API Requests. Please apply an Enterprise License";
	            Logger.warn(this.getClass(), noLicenseMessage);
	            return Response.status(Status.FORBIDDEN)
	                    .entity(map("message", noLicenseMessage))
	                    .header("error-message", noLicenseMessage)
	                    .build();
	        }
            Logger.warn(this.getClass(), "Error processing :" + dse.getMessage(), dse);
            return responseResource.responseError(dse.getMessage());
	        
		} catch (Exception e) {
			Logger.warn(this.getClass(), "Error processing :" + e.getMessage(), e);
			return responseResource.responseError(e.getMessage());
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("search")
	public Response searchPost(@Context HttpServletRequest request, @Context HttpServletResponse response, String esQuery) throws DotDataException, DotSecurityException{
		return search(request, response, esQuery);
	}
	
	@GET
	@Path("raw")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchRawGet(@Context HttpServletRequest request) {
		return searchRaw(request);

	}

	@POST
	@Path("raw")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchRaw(@Context HttpServletRequest request) {

        InitDataObject initData = webResource.init(null, true, request, false, null);

		HttpSession session = request.getSession();

        PageMode mode = PageMode.get(request);

		ResourceResponse responseResource = new ResourceResponse(initData.getParamsMap());

		User user = initData.getUser();
		try {
			String esQuery = IOUtils.toString(request.getInputStream());

			return responseResource.response(esapi.esSearchRaw(esQuery, mode.showLive, user, mode.showLive).toString());

		} catch (Exception e) {
			Logger.error(this.getClass(), "Error processing :" + e.getMessage(), e);
			return responseResource.responseError(e.getMessage());

		}
	}

}