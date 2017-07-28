package com.airbnb.airpal.core.store.jobs;

import com.airbnb.airpal.api.Job;
import com.airbnb.airpal.core.AirpalUser;
import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryActiveJobsStore implements ActiveJobsStore
{
    private ConcurrentMap<String, Set<Job>> activeJobs = new ConcurrentHashMap<>();

    @Override
    public Set<Job> getJobsForUser(AirpalUser user)
    {
        if (!activeJobs.containsKey(user.getUserName())) {
            return Collections.emptySet();
        }

        return ImmutableSet.copyOf(activeJobs.get(user.getUserName()));
    }

    @Override
    public void jobStarted(Job job)
    {
        Set<Job> jobsForUser = activeJobs.get(job.getUser());
        log.info("============inside activeJobs jobStarted =================="+job.toString());
        if (jobsForUser == null) {
          log.info("============inside activeJobs jobStarted =====inside =======jobsForUser == null======"+job.toString());
            jobsForUser = Collections.newSetFromMap(new ConcurrentHashMap<Job, Boolean>());
            activeJobs.putIfAbsent(job.getUser(), jobsForUser);
        }
        log.info("============inside activeJobs jobStarted =====inside =======jobsForUser not null======"+job.toString());
        activeJobs.get(job.getUser()).add(job);
    }

    @Override
    public void jobFinished(Job job)
    {  
        log.info("============inside activeJobs jobFinished =================="+job.toString());
        Set<Job> jobsForUser = activeJobs.get(job.getUser());

        if (jobsForUser == null) {
          log.info("============inside activeJobs jobFinished =====inside =======jobsForUser == null======"+job.toString());
            return;
        }
        log.info("============inside activeJobs jobFinished =====inside =======jobsForUser not null======"+job.toString());
        jobsForUser.remove(job);
    }
}
