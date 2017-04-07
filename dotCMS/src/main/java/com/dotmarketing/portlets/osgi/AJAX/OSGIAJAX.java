package com.dotmarketing.portlets.osgi.AJAX;

import com.dotcms.repackage.org.apache.commons.fileupload.FileItemFactory;
import com.dotcms.repackage.org.apache.commons.fileupload.FileItemIterator;
import com.dotcms.repackage.org.apache.commons.fileupload.FileItemStream;
import com.dotcms.repackage.org.apache.commons.fileupload.FileUploadException;
import com.dotcms.repackage.org.apache.commons.fileupload.disk.DiskFileItemFactory;
import com.dotcms.repackage.org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.dotcms.repackage.org.apache.commons.io.IOUtils;
import com.dotmarketing.servlets.taillog.TailLogServlet;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.OSGIUtil;
import com.dotmarketing.util.SecurityLogger;
import com.liferay.util.FileUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class OSGIAJAX extends OSGIBaseAJAX {

    @Override
    public void action ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    }

    public void undeploy ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException, InterruptedException {

        String jar = request.getParameter( "jar" );
        String bundleId = request.getParameter( "bundleId" );

        //First uninstall the bundle
        Bundle bundle;
        try {
            try {
                bundle = OSGIUtil.getInstance().getBundleContext().getBundle( new Long( bundleId ) );
            } catch ( NumberFormatException e ) {
                bundle = OSGIUtil.getInstance().getBundleContext().getBundle( bundleId );
            }

            bundle.uninstall();
          } catch ( BundleException e ) {
            Logger.error( OSGIAJAX.class, "Unable to undeploy bundle [" + e.getMessage() + "]", e );
        }

        //Then move the bundle from the load folder to the undeployed folder

        String loadPath;
        String undeployedPath;

        try {
            loadPath = OSGIUtil.getInstance().getBundleContext().getProperty(OSGIUtil.FELIX_FILEINSTALL_DIR);
        } catch (Exception ex) {
            Logger.error(this, "Unable to find the felix load folder path from OSGI bundle context. Setting it manually to /WEB-INF/felix/load", ex);
            loadPath = "/WEB-INF/felix/load";
        }

        try {
            undeployedPath = OSGIUtil.getInstance().getBundleContext().getProperty(OSGIUtil.FELIX_UNDEPLOYED_DIR);
        } catch (Exception ex) {
            Logger.error(this, "Unable to find the felix undeployed folder path from OSGI bundle context. Setting it manually to /WEB-INF/felix/undeployed", ex);
            undeployedPath = "/WEB-INF/felix/undeployed";
        }

        File from = new File( FileUtil.getRealPath( loadPath + File.separator + jar ) );
        File to = new File( FileUtil.getRealPath( undeployedPath + File.separator + jar ) );

        if(to.exists()) {
            to.delete();
        }

        Boolean success = FileUtil.move( from, to );
        if ( success ) {
        	Logger.info( OSGIAJAX.class, "OSGI Bundle "+jar+ " Undeployed");
            writeSuccess( response, "OSGI Bundle "+jar+ " Undeployed" );
        } else {
            Logger.error( OSGIAJAX.class, "Error undeploying OSGI Bundle "+jar );
            writeError( response, "Error undeploying OSGI Bundle "+jar );
        }
    }

    public void deploy ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String loadPath;
        String undeployedPath;

        try {
            loadPath = OSGIUtil.getInstance().getBundleContext().getProperty(OSGIUtil.FELIX_FILEINSTALL_DIR);
        } catch (Exception ex) {
            Logger.error(this, "Unable to find the felix load folder path from OSGI bundle context. Setting it manually to /WEB-INF/felix/load", ex);
            loadPath = "/WEB-INF/felix/load";
        }

        try {
            undeployedPath = OSGIUtil.getInstance().getBundleContext().getProperty(OSGIUtil.FELIX_UNDEPLOYED_DIR);
        } catch (Exception ex) {
            Logger.error(this, "Unable to find the felix undeployed folder path from OSGI bundle context. Setting it manually to /WEB-INF/felix/undeployed", ex);
            undeployedPath = "/WEB-INF/felix/undeployed";
        }

        String jar = request.getParameter( "jar" );
        File from = new File( FileUtil.getRealPath( undeployedPath + File.separator + jar ) );
        File to = new File( FileUtil.getRealPath( loadPath + File.separator + jar ) );

        Boolean success = from.renameTo( to );
        if ( success ) {
        	Logger.info( OSGIAJAX.class, "OSGI Bundle "+jar+ " Loaded");
            writeSuccess( response, "OSGI Bundle  "+jar+ " Loaded" );
        } else {
            Logger.error( OSGIAJAX.class, "Error Loading OSGI Bundle " + jar );
            writeError( response, "Error Loading OSGI Bundle" + jar );
        }
    }

    public void stop ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        String bundleID = request.getParameter( "bundleId" );
        String jar = request.getParameter( "jar" );
        try {
            try {
                OSGIUtil.getInstance().getBundleContext().getBundle( new Long( bundleID ) ).stop();
            } catch ( NumberFormatException e ) {
                OSGIUtil.getInstance().getBundleContext().getBundle( bundleID ).stop();
            }
            Logger.info( OSGIAJAX.class, "OSGI Bundle "+jar+ " Stopped");
        } catch ( BundleException e ) {
            Logger.error( OSGIAJAX.class, e.getMessage(), e );
            throw new ServletException( e.getMessage() + " Unable to stop bundle", e );
        }
    }

    public void start ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        String bundleID = request.getParameter( "bundleId" );
        String jar = request.getParameter( "jar" );
        try {
            try {
                OSGIUtil.getInstance().getBundleContext().getBundle( new Long( bundleID ) ).start();
            } catch ( NumberFormatException e ) {
                OSGIUtil.getInstance().getBundleContext().getBundle( bundleID ).start();
            }
            Logger.info( OSGIAJAX.class, "OSGI Bundle "+jar+ " Started");
        } catch ( BundleException e ) {
            Logger.error( OSGIAJAX.class, e.getMessage(), e );
            throw new ServletException( e.getMessage() + " Unable to start bundle", e );
        }
    }

    public void add ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload( factory );
        FileItemIterator iterator = null;
        String jar = request.getParameter( "jar" );
        try {
            iterator = upload.getItemIterator( request );
            while ( iterator.hasNext() ) {
                FileItemStream item = iterator.next();
                InputStream in = item.openStream();
                if ( item.getFieldName().equals( "bundleUpload" ) ) {
                    String fname = item.getName();
                    if ( !fname.endsWith( ".jar" ) ) {
                        Logger.warn( this, "Cannot deploy bundle as it is not a JAR" );
                        writeError( response, "Cannot deploy bundle as it is not a JAR" );
                        break;
                    }


                    String felixBaseFolder;
                    try {
                        felixBaseFolder = OSGIUtil.getInstance().getBundleContext().getProperty(OSGIUtil.FELIX_BASE_DIR);
                    } catch (Exception ex) {
                        Logger.error(this, "Unable to find the felix base folder path from OSGI bundle context. Setting it manually to /WEB-INF/felix", ex);
                        felixBaseFolder = "/WEB-INF/felix";
                    }

                    File felixFolder  = (felixBaseFolder.startsWith("/WEB-INF")) ?
                        new File(Config.CONTEXT.getRealPath(felixBaseFolder)) :
                        new File(com.dotmarketing.util.FileUtil.getAbsolutlePath(felixBaseFolder));
                    File osgiJar = new File(FileUtil.getRealPath( felixBaseFolder + "/load/" + fname ) );


                    if ( !felixFolder.exists() 
                            ||   !osgiJar.getCanonicalPath().startsWith(felixFolder.getCanonicalPath())) {
                        response.sendError(403);
                        SecurityLogger.logInfo(this.getClass(),  "Invalid OSGI Upload request:" + osgiJar.getCanonicalPath() + " from:" +request.getRemoteHost() + " " );
                        return;
                    }
                    
                    
                    

                    FileOutputStream out = new FileOutputStream( osgiJar );
                    IOUtils.copyLarge( in, out );
                    IOUtils.closeQuietly( out );
                    IOUtils.closeQuietly( in );
                }
            }
          Logger.info( OSGIAJAX.class, "OSGI Bundle "+jar+ " Uploaded");
        } catch ( FileUploadException e ) {
            Logger.error( OSGIBaseAJAX.class, e.getMessage(), e );
            throw new IOException( e.getMessage(), e );
        }
    }

    /**
     * Returns the packages inside the <strong>osgi-extra.conf</strong> file, those packages are the value
     * for the OSGI configuration property <strong>org.osgi.framework.system.packages.extra</strong>.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getExtraPackages ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        //Read the list of the dotCMS exposed packages to the OSGI context
        String extraPackages = OSGIUtil.getInstance().getExtraOSGIPackages();

        //Send a respose
        writeSuccess( response, extraPackages );
    }

    /**
     * Overrides the content of the <strong>osgi-extra.conf</strong> file
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void modifyExtraPackages ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        //Get the packages from the form
        String extraPackages = request.getParameter( "packages" );

        //Override the file with the values we just read
        BufferedWriter writer = new BufferedWriter( new FileWriter( OSGIUtil.getInstance().FELIX_EXTRA_PACKAGES_FILE ) );
        writer.write( extraPackages );
        writer.close();
        Logger.info( OSGIAJAX.class, "OSGI Extra Packages Saved");
        //Send a response
        writeSuccess( response, "OSGI Extra Packages Saved" );
    }

    public void restart ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        //First we need to stop the framework
        OSGIUtil.getInstance().stopFramework();

        //Now we need to initialize it
        OSGIUtil.getInstance().initializeFramework();
        
        //Send a respose
        writeSuccess( response, "OSGI Framework Restarted" );
    }

}