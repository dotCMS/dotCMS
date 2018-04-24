package com.dotmarketing.util;

import com.dotcms.api.web.HttpServletRequestThreadLocal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Usage:
 * 
 * PageMode mode = PageMode.get(request);
 * PageMode mode = PageMode.get(session);
 * 
 * mode.isAdmin ; mode.showLive ; mode.respectAnonPerms ;
 * 
 * 
 * if( PageMode.get(request).isAdmin){ doAdminStuff(); }
 * 
 * contentAPI.find("sad", user, mode.respectAnonPerms);
 * 
 * contentAPI.findByIdentifier("id", 1, mode.showLive, user, mode.respectAnonPerms);
 * 
 * PageMode.setPageMode(request, PageMode.PREVIEW_MODE);
 * 
 * 
 * 
 * @author will
 *
 */
public enum PageMode {

    LIVE(true, false), 
    ADMIN_MODE(true, true), 
    PREVIEW_MODE(false, true), 
    EDIT_MODE(false, true),
    NAVIGATE_EDIT_MODE(false, true);;

    private static PageMode DEFAULT_PAGE_MODE = LIVE;

    public final boolean showLive;
    public final boolean isAdmin;
    public final boolean respectAnonPerms;

    PageMode(boolean live, boolean admin) {
        this.showLive = live;
        this.isAdmin = admin;
        this.respectAnonPerms=!admin;
    }


    public static PageMode get(final HttpSession ses) {

        PageMode mode = PageMode.isPageModeSet(ses)
                        ? (PageMode) ses.getAttribute(WebKeys.PAGE_MODE_SESSION)
                        : DEFAULT_PAGE_MODE;

        return mode;
    }

    public static PageMode getWithNavigateMode(final HttpServletRequest req) {
        HttpSession ses = req.getSession();
        PageMode mode = PageMode.isPageModeSet(ses)
                ? PageMode.getCurrentPageMode(ses)
                : DEFAULT_PAGE_MODE;

        return mode;
    }

    public static PageMode get(final HttpServletRequest req) {
        if (req == null || req.getSession(false) == null || null!= req.getHeader("X-Requested-With")) {
            return DEFAULT_PAGE_MODE;
        }
        return get(req.getSession());
    }
    
    public static PageMode get(final String modeStr) {
        for(PageMode mode : values()) {
                if(mode.name().equals(modeStr)) {
                    return mode;
                }
        }
        return DEFAULT_PAGE_MODE;
    }

    public static PageMode setPageMode(final HttpServletRequest request, boolean contentLocked, boolean canLock) {
        
        PageMode mode = PREVIEW_MODE;
        if (contentLocked && canLock) {
            mode=EDIT_MODE;
        } 
        return setPageMode(request,mode);

    }

    public static PageMode setPageMode(final HttpServletRequest request, PageMode mode) {
        request.getSession().setAttribute(WebKeys.PAGE_MODE_SESSION, mode);
        request.setAttribute(WebKeys.PAGE_MODE_SESSION, mode);
        return mode;
    }

    private static boolean isPageModeSet(final HttpSession ses) {
        return (ses != null && ses.getAttribute(com.dotmarketing.util.WebKeys.PAGE_MODE_SESSION) != null
                && ses.getAttribute("tm_date") == null);
    }

    private static PageMode getCurrentPageMode(final HttpSession ses) {
        PageMode sessionPageMode = (PageMode) ses.getAttribute(WebKeys.PAGE_MODE_SESSION);

        if (isNavigateEditMode(ses)) {
            return PageMode.NAVIGATE_EDIT_MODE;
        } else {
            return sessionPageMode;
        }
    }

    private static boolean isNavigateEditMode(final HttpSession ses) {
        PageMode sessionPageMode = (PageMode) ses.getAttribute(WebKeys.PAGE_MODE_SESSION);
        HttpServletRequest request = HttpServletRequestThreadLocal.INSTANCE.getRequest();

        return  sessionPageMode == PageMode.EDIT_MODE &&
                request != null &&
                request.getAttribute(WebKeys.PAGE_MODE_SESSION) == null;
    }

}
