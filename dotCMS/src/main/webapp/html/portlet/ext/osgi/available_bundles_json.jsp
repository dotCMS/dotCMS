<%@page import="com.liferay.util.FileUtil"%>
<%@page import="com.dotmarketing.util.Config"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.File"%>
<%@page import="java.util.List"%>
<%@ page import="com.dotmarketing.util.OSGIUtil" %>

<%!
    /**
     * Return true if the name corresponds to a jar file
     *
     * @param name The file name
     * @return true if the name is a jar file, false otherwise.
     */
    private boolean isJarName ( String name ) {
        name = name.toLowerCase();
        return name.endsWith( ".jar" );
    }
%>

<%
    String path;
    try {
        path = OSGIUtil.getInstance().getBundleContext().getProperty(OSGIUtil.FELIX_UNDEPLOYED_DIR);
    } catch (Exception ex) {
        path = "/WEB-INF/felix/undeployed";
    }

    String f = FileUtil.getRealPath( path );
    if ( f == null ) {
        return;
    }

    File d = new File( f );
    String[] a = d.list();
    if ( a == null ) {
        a = new String[0];
    }
%>
{
	"identifier":"value", 
	"label":"label",
	"items":[
    <% for ( String b : a ) {
        if ( isJarName( b ) ) { %>
            {"value":"<%= b %>","label":"<%= b %>"},
    <%  }
       }%>
	]
}