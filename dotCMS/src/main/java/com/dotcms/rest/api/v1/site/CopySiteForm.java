package com.dotcms.rest.api.v1.site;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * Form to encapsulate the copy site
 * @author jsanca
 */
public class CopySiteForm {

    private final String copyFromHostId;
    private final boolean copyAll;
    private final boolean copyTemplatesContainers;
    private final boolean copyContentOnPages;
    private final boolean copyFolders;
    private final boolean copyContentOnHost;
    private final boolean copyLinks;
    private final boolean copyHostVariables;
    private final SiteForm host;

    @JsonCreator
    public CopySiteForm(
            @JsonProperty("copyFromHostId") final String copyFromHostId,
            @JsonProperty("copyAll") final boolean copyAll,
            @JsonProperty("copyTemplatesContainers") final boolean copyTemplatesContainers,
            @JsonProperty("copyContentOnPages") final boolean copyContentOnPages,
            @JsonProperty("copyFolders") final boolean copyFolders,
            @JsonProperty("copyContentOnHost") final boolean copyContentOnHost,
            @JsonProperty("copyLinks") final boolean copyLinks,
            @JsonProperty("copyHostVariables") final boolean copyHostVariables,
            @JsonProperty("host") final SiteForm host) {

        this.copyFromHostId = copyFromHostId;
        this.copyAll = copyAll;
        this.copyTemplatesContainers = copyTemplatesContainers;
        this.copyContentOnPages = copyContentOnPages;
        this.copyFolders = copyFolders;
        this.copyContentOnHost = copyContentOnHost;
        this.copyLinks = copyLinks;
        this.copyHostVariables = copyHostVariables;
        this.host = host;
    }

    public SiteForm getHost() {
        return host;
    }

    public String getCopyFromHostId() {
        return copyFromHostId;
    }

    public boolean isCopyAll() {
        return copyAll;
    }

    public boolean isCopyTemplatesContainers() {
        return copyTemplatesContainers;
    }

    public boolean isCopyContentOnPages() {
        return copyContentOnPages;
    }

    public boolean isCopyFolders() {
        return copyFolders;
    }

    public boolean isCopyContentOnHost() {
        return copyContentOnHost;
    }

    public boolean isCopyLinks() {
        return copyLinks;
    }

    public boolean isCopyHostVariables() {
        return copyHostVariables;
    }

    @Override
    public String toString() {
        return "CopySiteForm{" +
                "copyFromHostId='" + copyFromHostId + '\'' +
                ", copyAll=" + copyAll +
                ", copyTemplatesContainers=" + copyTemplatesContainers +
                ", copyContentOnPages=" + copyContentOnPages +
                ", copyFolders=" + copyFolders +
                ", copyContentOnHost=" + copyContentOnHost +
                ", copyLinks=" + copyLinks +
                ", copyHostVariables=" + copyHostVariables +
                ", host=" + host +
                '}';
    }
}
