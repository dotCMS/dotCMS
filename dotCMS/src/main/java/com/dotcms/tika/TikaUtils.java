package com.dotcms.tika;

import com.dotcms.osgi.OSGIConstants;
import com.dotcms.repackage.org.apache.commons.io.IOUtils;
import com.dotcms.repackage.org.apache.commons.io.input.ReaderInputStream;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.fileassets.business.FileAssetAPI;
import com.dotmarketing.portlets.structure.model.Structure;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.OSGIUtil;
import com.dotmarketing.util.StringUtils;
import com.dotmarketing.util.UtilMethods;
import com.liferay.portal.model.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

public class TikaUtils {

    private TikaProxyService tikaService;
    private Boolean osgiInitialized;

    public TikaUtils() {

        osgiInitialized = OSGIUtil.getInstance().isInitialized();
        if (osgiInitialized) {

            //Search for the TikaServiceBuilder service instance expose through OSGI
            TikaServiceBuilder tikaServiceBuilder = OSGIUtil.getInstance()
                    .getService(TikaServiceBuilder.class,
                            OSGIConstants.BUNDLE_NAME_DOTCMS_TIKA);
            if (null == tikaServiceBuilder) {
                throw new IllegalStateException(
                        String.format("OSGI Service [%s] not found for bundle [%s]",
                                TikaServiceBuilder.class,
                                OSGIConstants.BUNDLE_NAME_DOTCMS_TIKA));
            }

            /*
            Creating a new instance of the TikaProxyService in order to use the Tika services exposed in OSGI,
            when the createTikaService method is called a new instance of Tika is also created
            by the TikaProxyService implementation.
             */
            this.tikaService = tikaServiceBuilder.createTikaService();
        } else {
            Logger.error(this.getClass(), "OSGI Framework not initialized");
        }
    }

    /**
     * This method takes a file and uses tika to parse the metadata from it. It
     * returns a Map of the metadata <strong>BUT this method won't try to create any metadata file
     * if does not exist or to override the existing metadata file for the given Contentlet and
     * besaid that will put in memory the given file content before to parse it</strong>.
     *
     * @param inode Contentlet owner of the file to parse
     * @param binFile File to parse the metadata from it
     */
    public Map<String, String> getMetaDataMapForceMemory(String inode, File binFile) {
        return getMetaDataMap(inode, binFile, true);
    }

    /**
     * This method takes a file and uses tika to parse the metadata from it. It
     * returns a Map of the metadata and creates a metadata file for the given
     * Contentlet if does not already exist, if already exist only the metadata is returned and no
     * file is override.
     *
     * @param inode Contentlet owner of the file to parse
     * @param binFile File to parse the metadata from it
     */
    public Map<String, String> getMetaDataMap(String inode, File binFile) {
        return getMetaDataMap(inode, binFile, false);
    }

    /**
     * Verifies if the Contentlet is a File asset in order to identify if it
     * is missing a metadata file, if the metadata does not exist this method
     * parses the file asset and generates it.
     *
     * @param contentlet Content to validate if have or not a metadata file
     */
    public Boolean generateMetaDataIfRequired(Contentlet contentlet)
            throws DotSecurityException, DotDataException {

        if (contentlet.getStructure().getStructureType() == Structure.STRUCTURE_TYPE_FILEASSET) {

            //See if we have content metadata file
            File contentMeta = APILocator.getFileAssetAPI()
                    .getContentMetadataFile(contentlet.getInode());

            //If the metadata file does not exist we need to parse and get the metadata for the file
            if (!contentMeta.exists()) {

                File binFile = APILocator.getContentletAPI()
                        .getBinaryFile(contentlet.getInode(), FileAssetAPI.BINARY_FIELD,
                                APILocator.getUserAPI().getSystemUser());
                if (binFile != null) {

                    //Parse the metadata from this file
                    getMetaDataMap(contentlet.getInode(), binFile);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Right now the method use the Tika facade directly for parse the document without any kind of restriction about the parser because the
     * new Tika().parse method use the AutoDetectParser by default.
     *
     * @author Graziano Aliberti - Engineering Ingegneria Informatica S.p.a
     *
     * May 31, 2013 - 12:27:19 PM
     */
    private Map<String, String> getMetaDataMap(String inode, File binFile, boolean forceMemory) {

        if (!osgiInitialized) {
            Logger.error(this.getClass(),
                    "Unable to get file Meta Data, OSGI Framework not initialized");
            return new HashMap<>();
        }

        this.tikaService.setMaxStringLength(-1);

        Map<String, String> metaMap = new HashMap<>();

        // Search for the stored content metadata on disk
        File contentMetadataFile = APILocator.getFileAssetAPI().getContentMetadataFile(inode);

        Reader fulltext = null;
        InputStream is = null;

        char[] buf;
        byte[] bytes;
        int count;

        // if the limit is not "unlimited"
        // I can use the faster parseToString
        try {

            if (forceMemory) {
                // no worry about the limit and less time to process.
                final String content = this.tikaService
                        .parseToString(Files.newInputStream(binFile.toPath()));

                //Creating the meta data map to use by our content
                metaMap = buildMetaDataMap();
                metaMap.put(FileAssetAPI.CONTENT_FIELD, content);
            } else {

                is = this.tikaService.tikaInputStreamGet(binFile);
                fulltext = this.tikaService.parse(is);

                //Creating the meta data map to use by our content
                metaMap = buildMetaDataMap();

                buf = new char[1024];
                bytes = new byte[1024];
                count = fulltext.read(buf);

                if (count > 0 && !contentMetadataFile.exists()) {

                    //Create the new content metadata file
                    prepareMetaDataFile(contentMetadataFile);

                    OutputStream out = Files.newOutputStream(contentMetadataFile.toPath());

                    // compressor config
                    String compressor = Config.getStringProperty("CONTENT_METADATA_COMPRESSOR", "none");
                    if (compressor.equals("gzip")) {
                        out = new GZIPOutputStream(out);
                    } else if (compressor.equals("bzip2")) {
                        out = new BZip2CompressorOutputStream(out);
                    }

                    ReaderInputStream ris = null;

                    try {

                        ris = new ReaderInputStream(fulltext, StandardCharsets.UTF_8);

                        int metadataLimit = Config.getIntProperty("META_DATA_MAX_SIZE", 5) * 1024 * 1024;
                        int numOfChunks = metadataLimit / 1024;

                        do {
                            String lowered = new String(buf);
                            lowered = lowered.toLowerCase();
                            bytes = lowered.getBytes(StandardCharsets.UTF_8);
                            out.write(bytes, 0, count);
                            numOfChunks--;
                        } while ((count = fulltext.read(buf)) > 0 && numOfChunks > 0);
                    } catch (IOException ioExc) {
                        Logger.error(this.getClass(), "Error Reading TikaParse Stream.", ioExc);
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                                Logger.warn(this.getClass(), "Error Closing Stream.", e);
                            }
                        }

                        if (ris != null) {
                            try {
                                ris.close();
                            } catch (IOException e) {
                                Logger.warn(this.getClass(), "Error Closing Stream.", e);
                            }
                        }

                        IOUtils.closeQuietly(out);
                        IOUtils.closeQuietly(fulltext);
                    }
                } else if (!contentMetadataFile.exists()) {
                    /*
                    Create an empty file as we have nothing to put here but it is a record
                    that we already try to process this file.
                     */
                    prepareMetaDataFile(contentMetadataFile);
                }
            }
        } catch (IOException ioExc) {
            Logger.error(this.getClass(), "Error Reading TikaParse Stream.", ioExc);
        } catch (Exception e) {
            Logger.error(this.getClass(),
                    "Could not parse file metadata for file : " + binFile.getAbsolutePath() + ". "
                            + e.getMessage());
        } finally {
            if (null != fulltext) {
                IOUtils.closeQuietly(fulltext);
            }
            if (null != is) {
                IOUtils.closeQuietly(is);
            }
            try {
                metaMap.put(FileAssetAPI.SIZE_FIELD, String.valueOf(binFile.length()));
            } catch (Exception ex) {
                Logger.error(this.getClass(),
                        "Could not parse file metadata for file : " + binFile.getAbsolutePath()
                                + ". " + ex.getMessage());
            }
        }

        User systemUser = null;
        try {
            systemUser = APILocator.getUserAPI().getSystemUser();
        } catch (DotDataException e) {
            Logger.error(this,"Unable to get System User. ",e);
        }

        Map<String, Object> additionProps = new HashMap<>();
        try {
            additionProps = com.dotmarketing.portlets.contentlet.util.ContentletUtil.getContentPrintableMap(systemUser, APILocator.getContentletAPI().find(inode,systemUser,true));
        } catch (Exception e) {
            Logger.error(this,"Unable to add additional metadata to map",e);
        }
        for(Map.Entry<String, Object> entry : additionProps.entrySet()){
            metaMap.put(entry.getKey().toLowerCase(), entry.getValue().toString().toLowerCase());
        }

        return metaMap;
    }

    /**
     * Creates the metadata file where the parsed info will be stored
     */
    private void prepareMetaDataFile(File contentMetadataFile) throws IOException {

        //In order to re-create it
        contentMetadataFile.getParentFile().mkdirs();
        contentMetadataFile.createNewFile();
    }

    /**
     * Detects the media type of the given file. The type detection is
     * based on the document content and a potential known file extension.
     *
     * @param file the file
     * @return detected media type
     * @throws IOException if the file can not be read
     */
    public String detect(File file) throws IOException {
        if (!osgiInitialized) {
            Logger.error(this.getClass(),
                    "Unable to get file media type, OSGI Framework not initialized");
            return "";
        }

        return this.tikaService.detect(file);
    }

    private Map<String, String> buildMetaDataMap() {

        Map<String, String> metaMap = new HashMap<>();
        for (int i = 0; i < this.tikaService.metadataNames().length; i++) {
            String name = this.tikaService.metadataNames()[i];
            if (UtilMethods.isSet(name) && this.tikaService.metadataGetName(name) != null) {
                // we will want to normalize our metadata for searching
                String[] x = translateKey(name);
                for (String y : x) {
                    metaMap.put(y, this.tikaService.metadataGetName(name));
                }
            }
        }

        return metaMap;
    }

    /**
     * normalize metadata from various filetypes this method will return an
     * array of metadata keys that we can use to normalize the values in our
     * fileAsset metadata For example, tiff:ImageLength = "height" for image
     * files, so we return {"tiff:ImageLength", "height"} and both metadata are
     * written to our metadata field
     */
    private String[] translateKey(String key) {
        String[] x = getTranslationMap().get(key);
        if (x == null) {
            x = new String[]{StringUtils.camelCaseLower(key)};
        }
        return x;
    }

    private Map<String, String[]> translateMeta = null;

    private Map<String, String[]> getTranslationMap() {
        if (translateMeta == null) {
            synchronized ("translateMeta".intern()) {
                if (translateMeta == null) {
                    translateMeta = new HashMap<String, String[]>();
                    translateMeta.put("tiff:ImageWidth", new String[]{"tiff:ImageWidth", "width"});
                    translateMeta
                            .put("tiff:ImageLength", new String[]{"tiff:ImageLength", "height"});
                }
            }
        }
        return translateMeta;
    }

}