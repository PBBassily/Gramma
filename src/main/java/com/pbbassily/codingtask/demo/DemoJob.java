package com.pbbassily.codingtask.demo;

import com.pbbassily.codingtask.grammaproject.job.Job;
import com.pbbassily.codingtask.grammaproject.job.JobContext;

public class DemoJob extends Job {
    public DemoJob(String name) {
        super(name);
    }

    @Override
    public void execute(JobContext context) {
        System.out.println("[" + this.getName() + "]: Hey it is my job with context: " + context.getData());
    }
}
