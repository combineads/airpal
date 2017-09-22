package com.airbnb.airpal.presto;

import static com.google.common.base.MoreObjects.firstNonNull;
import static io.airlift.units.Duration.succinctDuration;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.net.URI;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Provider;

import com.facebook.presto.client.ClientSession;
import com.google.common.collect.ImmutableMap;

import io.airlift.units.Duration;

public class ClientSessionFactory
{
    private final boolean debug;
    private final String defaultSchema;
    private final String catalog;
    private final String source;
    private final String user;
    private final Provider<URI> server;
    private final String timeZoneId;
    private final Locale locale;
    private final Duration clientSessionTimeout;

    public ClientSessionFactory(Provider<URI> server, String user, String source, String catalog, String defaultSchema, boolean debug, Duration clientSessionTimeout)
    {
        this.server = server;
        this.user = user;
        this.source = source;
        this.catalog = catalog;
        this.defaultSchema = defaultSchema;
        this.debug = debug;
        this.timeZoneId = TimeZone.getTimeZone("UTC").getID();
        this.locale = Locale.getDefault();
        this.clientSessionTimeout = firstNonNull(clientSessionTimeout, succinctDuration(1, MINUTES));
    }

    public ClientSession create(String user, String schema)
    {
        return new ClientSession(server.get(),
                user,
                source,
                "airpal",
                catalog,
                schema,
                timeZoneId,
                locale,
                ImmutableMap.<String, String>of(),
                null,
                debug,
                clientSessionTimeout
        );
    }

    public ClientSession create(String schema)
    {
        return new ClientSession(server.get(),
                user,
                source,
                "airpal",
                catalog,
                schema,
                timeZoneId,
                locale,
                ImmutableMap.<String, String>of(),
                null,
                debug,
                clientSessionTimeout
        );
    }

    public ClientSession create()
    {
        return new ClientSession(server.get(),
                user,
                source,
                "airpal",
                catalog,
                defaultSchema,
                timeZoneId,
                locale,
                ImmutableMap.<String, String>of(),
                null,
                debug,
                clientSessionTimeout
        );
    }
}
