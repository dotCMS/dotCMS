package com.dotmarketing.portlets.contentlet.model;

import com.dotcms.content.elasticsearch.business.event.ContentletArchiveEvent;
import com.dotcms.content.elasticsearch.business.event.ContentletCheckinEvent;
import com.dotcms.content.elasticsearch.business.event.ContentletDeletedEvent;
import com.dotcms.content.elasticsearch.business.event.ContentletPublishEvent;

/**
 * Listener to handle Contentlet events
 * @author jsanca
 */
public interface ContentletListener {

    /**
     * Gets the identifier, by default the class name
     * @return String
     */
    default String getId() {
        return this.getClass().getName();
    }

    /**
     * When a contentlet is being modified or created this event is triggered
     * @param contentletCheckinEvent {@link ContentletCheckinEvent}
     */
    default void onModified(ContentletCheckinEvent contentletCheckinEvent) {}

    /**
     * When a contentlet is being modified (publish/unpublish) this event is triggered
     * @param contentletPublishEvent {@link ContentletPublishEvent}
     */
    default void onModified(ContentletPublishEvent contentletPublishEvent) {}

    /**
     * When a contentlet is un/archive this event is triggered
     * @param contentletArchiveEvent {@link ContentletArchiveEvent}
     */
    default void onDeleted(ContentletArchiveEvent contentletArchiveEvent) { }

    /**
     * When a contentlet is deleted this event is triggered
     * @param contentletDeletedEvent {@link ContentletDeletedEvent}
     */
    default void onDeleted(ContentletDeletedEvent contentletDeletedEvent) { }
}
