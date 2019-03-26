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
package com.liferay.portal.tools;

import com.dotmarketing.util.Logger;
import com.liferay.util.FileUtil;
import com.liferay.util.StringUtil;
import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * <a href="DBLoader.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @version $Revision: 1.12 $
 */
public class DBLoader {

  public static void main(String[] args) {
    new DBLoader();
  }

  public DBLoader() {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
      Connection con = DriverManager.getConnection("jdbc:hsqldb:test", "sa", "");

      StringBuffer sb = new StringBuffer();

      PreparedStatement ps = null;

      BufferedReader br =
          new BufferedReader(
              new StringReader(FileUtil.read("../sql/portal/portal-hypersonic.sql")));

      String line = null;

      while ((line = br.readLine()) != null) {
        if (!line.startsWith("//")) {
          sb.append(line);

          if (line.endsWith(";")) {
            String sql = sb.toString();

            sql =
                StringUtil.replace(
                    sql,
                    new String[] {"\\'", "\\\"", "\\n", "\\r"},
                    new String[] {"\\u0027\\u0027", "\"", "\\u000a", "\\u000a"});

            sb = new StringBuffer();

            ps = con.prepareStatement(sql);

            ps.executeUpdate();
            ps.close();
          }
        }
      }

      br.close();

      // Shutdown Hypersonic

      Statement statement = con.createStatement();

      statement.execute("SHUTDOWN COMPACT");

      statement.close();

      con.close();

      // Hypersonic will encode unicode characters twice, this will undo
      // it

      String content = FileUtil.read("test.script");

      content = StringUtil.replace(content, "\\u005cu", "\\u");

      FileUtil.write("test.script", content);
    } catch (Exception e) {
      Logger.error(this, e.getMessage(), e);
    }
  }
}
