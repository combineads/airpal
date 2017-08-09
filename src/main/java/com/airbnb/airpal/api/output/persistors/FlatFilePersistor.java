package com.airbnb.airpal.api.output.persistors;

import com.airbnb.airpal.api.Job;
import com.airbnb.airpal.api.output.builders.JobOutputBuilder;
import com.airbnb.airpal.core.execution.QueryExecutionAuthorizer;
import com.airbnb.airpal.core.store.files.ExpiringFileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static java.lang.String.format;

@RequiredArgsConstructor
@Slf4j
public class FlatFilePersistor
        implements Persistor
{
    private final ExpiringFileStore fileStore;

    @Override
    public boolean canPersist(QueryExecutionAuthorizer authorizer)
    {
        // Everyone can create files to download.
        return true;
    }

    @Override
    public URI persist(JobOutputBuilder outputBuilder, Job job)
    {
        File file = outputBuilder.build();

        try {
            fileStore.addFile(file.getName(), file);
        }
        catch (IOException e) {
            log.error("Caught error adding file to local store", e);
        }

        return URI.create(format("http://bbc2.sics.se:44830/hopsworks-api/airpal/api/files/%s", file.getName()));
    }
}
