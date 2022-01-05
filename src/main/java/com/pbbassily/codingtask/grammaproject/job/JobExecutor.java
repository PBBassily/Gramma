package com.pbbassily.codingtask.grammaproject.job;

import com.pbbassily.codingtask.grammaproject.exceptions.FailedExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.concurrent.TimeUnit;

public class JobExecutor {

    private final static Logger logger = LogManager.getLogger(JobExecutor.class);

    public static void execute(Job job, JobContext context) {

        long started = System.currentTimeMillis();

        try {
            job.execute(context);
            logger.info("\n[Job Id]: " + job.getName()
                    + "\n[Execution Time]: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - started)
                    + " Seconds.\n[Status]: Done Successfully!");
        } catch (FailedExecutionException e) {
            logger.error("\n[Job Id]: " + job.getName()
                    + "\n[Execution Time]: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - started)
                    + " Seconds.\n[Status]: Failed to execute");
            e.printStackTrace();
        }

    }
}
