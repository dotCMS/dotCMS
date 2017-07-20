package com.dotcms.api.system.event.local;

import java.util.Map;

/**
 * Based on a strategy will return the event type associated to the EventSubscriber.
 * @author jsanca
 */
public interface EventSubscriberFinder {

    /**
     * Finds a set of {@link EventSubscriber} based on some strategy and returns a map with event type associated to the {@link EventSubscriber}
     *
     * @param subcriber {@link Object}  object with the subscriter to register
     * @return Map of Class type (event type) associated to the {@link EventSubscriber}
     *
     * @throws IllegalArgumentException if {@code source} is not appropriate for
     *         this strategy (in ways that this interface does not define).
     */
    Map<Class<?>, EventSubscriber> findSubscriber(Object subcriber);
} // E:O:F:EventSubscriberFinder.
