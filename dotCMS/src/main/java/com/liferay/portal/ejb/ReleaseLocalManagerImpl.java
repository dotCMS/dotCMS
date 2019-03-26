/**
 * Copyright (c) 2000-2005 Liferay, LLC. All rights reserved.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.liferay.portal.ejb;

import com.liferay.portal.NoSuchReleaseException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Release;
import com.liferay.portal.util.ReleaseInfo;
import java.util.Date;

/**
 * <a href="ReleaseLocalManagerImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @version $Revision: 1.3 $
 */
public class ReleaseLocalManagerImpl implements ReleaseLocalManager {

  // Business methods

  public Release getRelease() throws PortalException, SystemException {
    Release release = null;

    try {
      release = ReleaseUtil.findByPrimaryKey(Release.DEFAULT_ID);
    } catch (NoSuchReleaseException nsre) {
      release = ReleaseUtil.create(Release.DEFAULT_ID);

      Date now = new Date();

      release.setCreateDate(now);
      release.setModifiedDate(now);

      ReleaseUtil.update(release);
    }

    return release;
  }

  public Release updateRelease() throws PortalException, SystemException {
    Release release = getRelease();

    release.setModifiedDate(new Date());
    release.setBuildNumber(ReleaseInfo.getBuildNumber());
    release.setBuildDate(ReleaseInfo.getBuildDate());

    ReleaseUtil.update(release);

    return release;
  }
}
