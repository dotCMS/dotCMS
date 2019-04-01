/**
 * Copyright (c) 2000-2005 Liferay, LLC. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.liferay.portal.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.dotmarketing.exception.DotDataException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Portlet;

/**
 * <a href="PortletManager.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @version $Revision: 1.77 $
 *
 */
public interface PortletManager {

  public Portlet getPortletById(String portletId) throws SystemException, java.rmi.RemoteException;

  public Collection<Portlet> getPortlets() throws SystemException, java.rmi.RemoteException;

  public Map<String, Portlet> addPortlets(String[] xmls) throws SystemException;

  public void deletePortlet(String portletId) throws SystemException, PortalException;

  public Portlet updatePortlet(String portletId, String groupId, String defaultPreferences, boolean narrow, String roles, boolean active)
      throws PortalException, SystemException, RemoteException;

  Optional<Portlet> findById(String portletId) throws DotDataException;

  Portlet savePortlet(Portlet portlet) throws DotDataException;

  String portletToXml(Portlet portlet) throws DotDataException;
}
