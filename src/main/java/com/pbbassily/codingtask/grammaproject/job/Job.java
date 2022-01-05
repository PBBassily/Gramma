package com.pbbassily.codingtask.grammaproject.job;

import com.pbbassily.codingtask.grammaproject.exceptions.FailedExecutionException;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Job {

    private final String name;

    public Job(String name) {
        this.name = name + "-" + UUID.randomUUID().toString();
    }

    public abstract void execute(JobContext context) throws FailedExecutionException;
}
