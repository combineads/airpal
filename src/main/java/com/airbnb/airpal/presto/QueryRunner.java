package com.airbnb.airpal.presto;

import com.facebook.presto.client.ClientSession;
import com.facebook.presto.client.StatementClient;
import com.facebook.presto.client.StatementClientFactory;
import okhttp3.OkHttpClient;

import java.io.Closeable;

import static com.google.common.base.Preconditions.checkNotNull;

public class QueryRunner
        implements Closeable {

    private final ClientSession session;
    private final OkHttpClient httpClient;

    protected QueryRunner(ClientSession session, OkHttpClient httpClient) {
        this.session = checkNotNull(session, "session is null");
        this.httpClient = httpClient;
    }

    public StatementClient startInternalQuery(String query) {
        return StatementClientFactory.newStatementClient(httpClient, session, query);
    }

    @Override
    public void close() {
        //httpClient.close();
    }

    public static class QueryRunnerFactory {
        private final ClientSessionFactory sessionFactory;
        private final OkHttpClient httpClient;

        public QueryRunnerFactory(ClientSessionFactory sessionFactory, OkHttpClient httpClient) {
            this.httpClient = httpClient;
            this.sessionFactory = sessionFactory;
        }

        public QueryRunner create(String user, String schema) {
            return new QueryRunner(sessionFactory.create(user, schema), httpClient);
        }

        public QueryRunner create() {
            return new QueryRunner(sessionFactory.create(), httpClient);
        }
    }
}
