package com.pbbassily.codingtask.grammaproject.trigger;

import com.pbbassily.codingtask.grammaproject.job.Job;

import com.pbbassily.codingtask.grammaproject.time.GrammaTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TriggerTest {
    private Trigger underTest;
    @Mock
    private List<Job> jobs;
    @Mock
    private GrammaTime frequency;

    @BeforeEach
    public void setup() {
        when(frequency.getValueInMillis()).thenReturn(1000L);
    }

    @Test
    public void test_getNextFireUpTimeStamp_THEN_return_correct_value() {
        underTest = Trigger
                .builder()
                .baseTime(new TriggerBaseTime(10_000L))
                .frequency(frequency)
                .jobsToBeExecuted(jobs)
                .build();
        assertEquals(11000L, underTest.getNextFireUpTimeStamp());
    }

    @Test
    public void test_getNextFireUpTimeStamp_WHEN_updating_base_time_THEN_return_correct_value() {
        underTest = Trigger
                .builder()
                .baseTime(new TriggerBaseTime(10_000L))
                .frequency(frequency)
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
}
