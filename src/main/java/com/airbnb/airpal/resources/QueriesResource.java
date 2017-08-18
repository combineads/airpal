package com.airbnb.airpal.resources;

import com.airbnb.airpal.api.Job;
import com.airbnb.airpal.api.JobState;
import com.airbnb.airpal.core.AirpalUser;
import com.airbnb.airpal.core.AuthorizationUtil;
import com.airbnb.airpal.core.execution.ExecutionClient;
import com.airbnb.airpal.core.store.history.JobHistoryStore;
import com.airbnb.airpal.presto.PartitionedTable;
import com.airbnb.airpal.presto.Table;
import com.facebook.presto.client.Column;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.google.inject.Inject;
import org.secnod.shiro.jaxrs.Auth;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.airbnb.airpal.resources.QueryResource.JOB_ORDERING;

import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/api/queries")
@Produces({MediaType.APPLICATION_JSON})
public class QueriesResource
{
    private final JobHistoryStore jobHistoryStore;
    private final ExecutionClient executionClient;
    private final String defaultCatalog;
    @Inject
    public QueriesResource(
            JobHistoryStore jobHistoryStore,
            ExecutionClient executionClient,
            @Named("default-catalog") final String defaultCatalog)
    {
        this.jobHistoryStore = jobHistoryStore;
        this.executionClient = executionClient;
        this.defaultCatalog = defaultCatalog;
    }

    @GET
    public Response getQueries(
            @Auth AirpalUser user,
            @QueryParam("results") int numResults,
            @QueryParam("table") List<PartitionedTable> tables,
            @QueryParam("catalog") Optional<String> catalogOptional)
    {
        String schema=user.getDefaultSchema();
       log.info("schema for getting tables: isss======= schema1====  "+schema);
        final String catalog = catalogOptional.or(defaultCatalog);
        log.info("catalog for getting tables: isss======= schema1====  "+catalog); 
   
        Iterable<Job> recentlyRun;
        int results = Optional.of(numResults).or(200);

        if (tables.size() < 1) {
//            recentlyRun = jobHistoryStore.getRecentlyRun(results);
         recentlyRun = jobHistoryStore.getRecentlyRun(results, catalog, schema);
        } else {
            recentlyRun = jobHistoryStore.getRecentlyRun(
                    results,
                    Iterables.transform(tables, new PartitionedTable.PartitionedTableToTable()));
        }

        ImmutableList.Builder<Job> filtered = ImmutableList.builder();
        for (Job job : recentlyRun) {
            if (job.getTablesUsed().isEmpty() && (job.getState() == JobState.FAILED)) {
                filtered.add(job);
                continue;
            }
            for (Table table : job.getTablesUsed()) {
                if (AuthorizationUtil.isAuthorizedRead(user, table)) {
                    filtered.add(new Job(
                            job.getUser(),
                            job.getQuery(),
                            job.getUuid(),
                            job.getOutput(),
                            job.getQueryStats(),
                            job.getState(),
                            Collections.<Column>emptyList(),
                            Collections.<Table>emptySet(),
                            job.getQueryStartedDateTime(),
                            job.getError(),
                            job.getQueryFinishedDateTime()));
                }
            }
        }

        List<Job> sortedResult = Ordering
                .natural()
                .nullsLast()
                .onResultOf(JOB_ORDERING)
                .reverse()
                .immutableSortedCopy(filtered.build());
        return Response.ok(sortedResult).build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response cancelQuery(
            @Auth AirpalUser user,
            @PathParam("uuid") UUID uuid)
    {
        boolean success = executionClient.cancelQuery(user, uuid);
        if (success) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
