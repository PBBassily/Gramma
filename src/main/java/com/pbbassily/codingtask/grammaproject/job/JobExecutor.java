package com.pbbassily.codingtask.grammaproject.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.concurrent.TimeUnit;

public class JobExecutor {


    public static void execute(Job job, JobContext context) {
        Logger logger = LogManager.getLogger(JobExecutor.class);
        long started = System.currentTimeMillis();
        job.execute(context);
        logger.debug("\n[Job Id]: " + job.getName()
                + "\n[Execution Time]: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - started)
                + " Seconds.");
    }
}
