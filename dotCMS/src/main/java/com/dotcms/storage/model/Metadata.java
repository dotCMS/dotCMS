package com.dotcms.storage.model;

import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Metadata implements Serializable {

    public static final String CUSTOM_PROP_PREFIX = "custom::";

    private final String fieldName;

    private final Map<String, Serializable> fieldsMeta;

    private final Map<String, Serializable> customMeta;

    public Metadata(final String fieldName,
            final Map<String, Serializable> fieldsMeta) {

        this.fieldName = fieldName;
        final Map<String, Serializable> meta = fieldsMeta == null ? ImmutableMap.of(): fieldsMeta;
        this.fieldsMeta = ImmutableMap.copyOf(meta);
        this.customMeta = ImmutableMap.copyOf(meta.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(CUSTOM_PROP_PREFIX))
                .collect(Collectors.toMap(o ->
                    o.getKey().substring(CUSTOM_PROP_PREFIX.length()
                ), Entry::getValue)));
    }

    public String getFieldName() {
        return fieldName;
    }

    public Map<String, Serializable> getFieldsMeta() {
        return fieldsMeta;
    }

    public Map<String, Serializable> getCustomMeta() {
        return customMeta;
    }
}