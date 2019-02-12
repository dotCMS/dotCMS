package com.dotmarketing.portlets.htmlpageasset.business.render;

import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.htmlpageasset.business.render.page.HTMLPageAssetRendered;
import com.dotmarketing.portlets.htmlpageasset.business.render.page.PageView;
import com.dotmarketing.util.PageMode;
import com.liferay.portal.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Provides utility methods to render a {@link com.dotmarketing.portlets.htmlpageasset.model.HTMLPageAsset}
 */
public interface HTMLPageAssetRenderedAPI {

    /**
     * Returns the rendered version of an HTML Page, i.e., the HTML code that will be rendered in
     * the browser.
     *
     * @param request The {@link HttpServletRequest} object.
     * @param response The {@link HttpServletResponse} object.
     * @param user The {@link User} performing this action..
     * @param mode
     * @return The {@link HTMLPageAssetRendered} object containing the metadata of the different objects that
     * @param user     The {@link User} performing this action.
     * @param uri      The path to the HTML Page whose information will be retrieved.
     * @return The {@link PageView} object containing the metadata of the different objects that
     * make up an HTML Page.
     * @throws DotSecurityException The user does not have the specified permissions to perform
     *                              this action.
     * @throws DotDataException     An error occurred when accessing the data source.
     * @throws IOException
     */
     PageView getPageMetadata(
            final PageContext context,
            final HttpServletRequest request,
            final HttpServletResponse response)
                throws DotSecurityException, DotDataException;

    /***
     * Returns the rendered version of an HTML Page, i.e., the HTML code that will be rendered in
     * the browser.
     *
     * @param context The {@link PageContext} object.
     * @return The {@link HTMLPageAssetRendered} object containing the metadata of the different objects that
     * make up an HTML Page.
     * @throws DotSecurityException The user does not have the specified permissions to perform
     *                              this action.
     * @throws DotDataException     An error occurred when accessing the data source.
     * @throws IOException
     */
    PageView getPageRendered(
            final PageContext context,
            final HttpServletRequest request,
            final HttpServletResponse response)
                throws DotDataException, DotSecurityException;


    String getPageHtml(
            final PageContext context,
            final HttpServletRequest request,
            final HttpServletResponse response) throws DotSecurityException, DotDataException;

    /**
     * Return the page's default mode for edit page portlet, if user have the page's lock then return
     * {@link PageMode#EDIT_MODE}, in other case return {@link PageMode#PREVIEW_MODE}
     *
     * @param user
     * @param request
     * @param pageUri
     * @return
     */
     PageMode getDefaultEditPageMode(
             final User user,
             final HttpServletRequest request,
             final String pageUri,
             final HttpServletResponse response
     );
}
