package com.pbbassily.codingtask.grammaproject.trigger;

import com.pbbassily.codingtask.grammaproject.job.Job;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TriggerTest {
    private Trigger underTest;
    @Mock
    private List<Job> jobs;

    @Test
    public void test_getNextFireUpTimeStamp_THEN_return_correct_value() {
        underTest = Trigger
                .builder()
                .baseTimestamp(10000L)
                .frequency(1)
                .jobsToBeExecuted(jobs)
                .build();
        assertEquals(11000L, underTest.getNextFireUpTimeStamp());
    }

    @Test
    public void test_getNextFireUpTimeStamp_WHEN_updating_base_time_THEN_return_correct_value() {
        underTest = Trigger
                .builder()
                .baseTimestamp(10000L)
                .frequency(1)
                .jobsToBeExecuted(jobs)
                .build();
        assertEquals(11000L, underTest.getNextFireUpTimeStamp());

        underTest.updateBaseTime(11000L);
        assertEquals(12000L, underTest.getNextFireUpTimeStamp());

        underTest.updateBaseTime(12000L);
        assertEquals(13000L, underTest.getNextFireUpTimeStamp());

        underTest.updateBaseTime(13000L);
        assertEquals(14000L, underTest.getNextFireUpTimeStamp());
    }

    @Test
    public void test_buildTrigger_WHEN_frequency_is_wrong_THEN_throw_exception() {

        assertThrows(IllegalArgumentException.class, () -> Trigger.builder()
                    .baseTimestamp(10000L)
                    .frequency(-1)
                    .jobsToBeExecuted(jobs)
                    .build());
        assertThrows(IllegalArgumentException.class, () -> Trigger.builder()
                .baseTimestamp(10000L)
                .frequency(0)
                .jobsToBeExecuted(jobs)
                .build());
    }
}
