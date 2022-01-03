package com.pbbassily.codingtask.grammaproject.executor;

import com.pbbassily.codingtask.grammaproject.job.Job;
import com.pbbassily.codingtask.grammaproject.job.JobContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class Executor extends Thread {

    @NonNull private final Job jobToBeExecuted;
    @NonNull private final JobContext jobContext;

    @SneakyThrows
    @Override
    public void run() {
        jobToBeExecuted.execute(jobContext);
        Thread.sleep(20_000);
    }

}
