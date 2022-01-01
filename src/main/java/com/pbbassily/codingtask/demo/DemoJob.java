package com.pbbassily.codingtask.demo;

import com.pbbassily.codingtask.grammaproject.job.Job;
import com.pbbassily.codingtask.grammaproject.job.JobContext;

public class DemoJob implements Job {
    @Override
    public void execute(JobContext context) {
        System.out.println("Hey it is my job" + context.getData());
    }
}
