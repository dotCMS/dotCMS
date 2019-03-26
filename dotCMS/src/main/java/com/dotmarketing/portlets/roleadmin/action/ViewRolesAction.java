package com.dotmarketing.portlets.roleadmin.action;

import com.dotcms.repackage.javax.portlet.PortletConfig;
import com.dotcms.repackage.javax.portlet.RenderRequest;
import com.dotcms.repackage.javax.portlet.RenderResponse;
import com.liferay.portal.struts.PortletAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewRolesAction extends PortletAction {

  @Override
  public ActionForward render(
      ActionMapping mapping,
      ActionForm form,
      PortletConfig config,
      RenderRequest req,
      RenderResponse res)
      throws Exception {
    return mapping.findForward("portlet.ext.roleadmin.view_roles");
  }
}
