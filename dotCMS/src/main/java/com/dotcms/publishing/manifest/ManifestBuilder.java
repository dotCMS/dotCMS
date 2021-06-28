package com.dotcms.publishing.manifest;

import static com.dotcms.util.CollectionsUtils.list;
import static com.dotcms.util.CollectionsUtils.map;

import com.dotcms.contenttype.model.type.ContentType;
import com.dotcms.publisher.util.PusheableAsset;
import com.dotcms.publishing.manifest.ManifestItem.ManifestInfo;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.util.FileUtil;
import com.liferay.portal.model.User;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManifestBuilder implements Closeable {
    private final static String HEADERS_LINE =
            "INCLUDE/EXCLUDE,object type, Id, title, site, folder, exclude by, include by";
    private FileWriter csvWriter;

    private File manifestFile;

    public void create() throws IOException {
        manifestFile = File.createTempFile("ManifestBuilder_", ".csv");

        csvWriter = new FileWriter(manifestFile);
        writeLine(HEADERS_LINE);
    }

    private void writeLine(String headersLine) throws IOException {
        csvWriter.append(headersLine);
        csvWriter.append("\n");
    }

    public <T> void include(final ManifestItem manifestItem, final String reason){
        final ManifestInfo manifestInfo = manifestItem.getManifestInfo();
        final String line = getManifestFileIncludeLine(manifestInfo, reason);

        try {
            writeLine(line);
        } catch (IOException e) {
            throw new DotRuntimeException(e);
        }
    }

    private String getManifestFileIncludeLine(final ManifestInfo manifestInfo,
            final String includeReason) {
        return getManifestFileLine("INCLUDE", manifestInfo, includeReason, "");
    }

    private String getManifestFileExcludeLine(final ManifestInfo manifestInfo,
            final String excludeReason) {
        return getManifestFileLine("EXCLUDE", manifestInfo, "", excludeReason);
    }

    private String getManifestFileLine(
            final String includeExclude, final ManifestInfo manifestInfo,
            final String includeReason, final String excludeReason) {

        return list(
                includeExclude,
                manifestInfo.objectType(),
                manifestInfo.id(),
                manifestInfo.title(),
                manifestInfo.site(),
                manifestInfo.folder(),
                excludeReason,
                includeReason).stream().collect(Collectors.joining(","));
    }

    public <T> void exclude(final ManifestItem manifestItem, final String reason){
        final ManifestInfo manifestInfo = manifestItem.getManifestInfo();
        final String line = getManifestFileExcludeLine(manifestInfo, reason);

        try {
            writeLine(line);
        } catch (IOException e) {
            throw new DotRuntimeException(e);
        }
    }

    public File getManifestFile(){

        if (manifestFile == null) {
            throw new IllegalStateException("Must call create method before");
        }

        return manifestFile;
    }

    @Override
    public void close() {
        if (csvWriter != null) {
            try {
                csvWriter.close();
            } catch(IOException e) {
                //ignore
            }
        }
    }

}
