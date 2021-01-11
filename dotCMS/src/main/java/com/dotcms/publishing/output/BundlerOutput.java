package com.dotcms.publishing.output;

import com.dotcms.publishing.PublisherConfig;
import com.dotcms.util.DotPreconditions;
import com.dotmarketing.util.Logger;
import com.google.common.collect.ImmutableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public abstract class BundlerOutput implements Closeable {
    private List<File> files;
    protected PublisherConfig publisherConfig;

    public BundlerOutput(final PublisherConfig publisherConfig){
        this.publisherConfig = publisherConfig;
        files = new ArrayList<>();
    }

    protected abstract OutputStream innerAddFile(File file) throws IOException;

    public abstract File getFile();

    public final synchronized OutputStream addFile(final File file) throws IOException {
        DotPreconditions.checkArgument(file != null);

        try {
            files.add(file);
            return innerAddFile(file);
        } catch(IOException e) {
            Logger.error(this.getClass(), e);
            throw e;
        }
    }


    public final Collection<File> getFiles(final FileFilter fileFilter) {
        final Stream<File> fileStream = files
                .stream()
                .filter(file -> fileFilter.accept(file));

        return ImmutableList.copyOf(fileStream.iterator());
    }

    public boolean exists(final File searchedFile) {
        return files
                .stream()
                .map(file -> file.getPath())
                .anyMatch(path -> searchedFile.getPath().equals(path));
    }

    public abstract void delete(final File f);
}
