package com.pbbassily.codingtask.grammaproject.trigger;

import com.google.common.collect.ImmutableList;
import com.pbbassily.codingtask.grammaproject.job.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TriggersTrackerTest {

    private TriggersTracker underTest;
    private TriggersTracker spy;
    @Mock
    private Trigger trigger1;
    @Mock
    private Trigger trigger2;
    @Mock
    private Trigger trigger3;
    @Mock
    private Trigger trigger4;
    @Mock
    private Job job1;
    @Mock
    private Job job2;
    @Mock
    private Job job4;

    @BeforeEach
    public void setup() {
        underTest = new TriggersTracker();
        spy = Mockito.spy(underTest);
    }

    @Test
    public void test_addTrigger_WHEN_different_awaits_THEN_check_state() {
        Mockito.when(trigger1.getNextFireUpTimeStamp()).thenReturn(10L);
        Mockito.when(trigger2.getNextFireUpTimeStamp()).thenReturn(20L);
        Mockito.when(trigger3.getNextFireUpTimeStamp()).thenReturn(30L);
        Mockito.when(trigger4.getNextFireUpTimeStamp()).thenReturn(40L);

        underTest.addTrigger(trigger1);
        underTest.addTrigger(trigger2);
        underTest.addTrigger(trigger3);
        underTest.addTrigger(trigger4);

        assertEquals(4, underTest.getNextEpocToWaitingTriggersMap().size());

        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(10L).contains(trigger1));
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(20L).contains(trigger2));
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(30L).contains(trigger3));
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(40L).contains(trigger4));

        assertEquals(1, underTest.getNextEpocToWaitingTriggersMap().get(10L).size());
        assertEquals(1, underTest.getNextEpocToWaitingTriggersMap().get(20L).size());
        assertEquals(1, underTest.getNextEpocToWaitingTriggersMap().get(30L).size());
        assertEquals(1, underTest.getNextEpocToWaitingTriggersMap().get(40L).size());
    }

    @Test
    public void test_addTrigger_WHEN_similar_awaits_THEN_check_state() {
        Mockito.when(trigger1.getNextFireUpTimeStamp()).thenReturn(10L);
        Mockito.when(trigger2.getNextFireUpTimeStamp()).thenReturn(20L);
        Mockito.when(trigger3.getNextFireUpTimeStamp()).thenReturn(10L);
        Mockito.when(trigger4.getNextFireUpTimeStamp()).thenReturn(20L);

        underTest.addTrigger(trigger1);
        underTest.addTrigger(trigger2);
        underTest.addTrigger(trigger3);
        underTest.addTrigger(trigger4);

        assertEquals(2, underTest.getNextEpocToWaitingTriggersMap().size());
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(10L).contains(trigger1));
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(10L).contains(trigger3));
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(20L).contains(trigger2));
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(20L).contains(trigger4));

        assertEquals(2, underTest.getNextEpocToWaitingTriggersMap().get(10L).size());
        assertEquals(2, underTest.getNextEpocToWaitingTriggersMap().get(20L).size());
    }

    @Test
    public void test_addTrigger_WHEN_identical_awaits_THEN_check_state() {
        Mockito.when(trigger1.getNextFireUpTimeStamp()).thenReturn(10L);
        Mockito.when(trigger2.getNextFireUpTimeStamp()).thenReturn(10L);
        Mockito.when(trigger3.getNextFireUpTimeStamp()).thenReturn(10L);
        Mockito.when(trigger4.getNextFireUpTimeStamp()).thenReturn(10L);

        underTest.addTrigger(trigger1);
        underTest.addTrigger(trigger2);
        underTest.addTrigger(trigger3);
        underTest.addTrigger(trigger4);

        assertEquals(1, underTest.getNextEpocToWaitingTriggersMap().size());

        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(10L).contains(trigger1));
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(10L).contains(trigger3));
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(10L).contains(trigger2));
        assertTrue(underTest.getNextEpocToWaitingTriggersMap().get(10L).contains(trigger4));

        assertEquals(4, underTest.getNextEpocToWaitingTriggersMap().get(10L).size());
    }

    @Test
    public void test_getFiredTriggers_WHEN_matched_awaits_THEN_check_response() {
        Mockito.when(trigger1.getNextFireUpTimeStamp()).thenReturn(10L);
        Mockito.when(trigger2.getNextFireUpTimeStamp()).thenReturn(20L);
        Mockito.when(trigger1.getJobsToBeExecuted()).thenReturn(ImmutableList.of(job1));

        spy.addTrigger(trigger1);
        spy.addTrigger(trigger2);

        Optional<List<Job>> jobsAt10 = spy.getFiredTriggers(10L);
        assertTrue(jobsAt10.isPresent());
        assertThat(ImmutableList.of(job1)).hasSameElementsAs(jobsAt10.get());
        verify(spy).updateNextEpocToWaitingTriggersMap(anyLong());
    }

    @Test
    public void test_getFiredTriggers_WHEN_matched_group_awaits_THEN_check_response() {
        Mockito.when(trigger1.getNextFireUpTimeStamp()).thenReturn(10L);
        Mockito.when(trigger2.getNextFireUpTimeStamp()).thenReturn(20L);
        Mockito.when(trigger4.getNextFireUpTimeStamp()).thenReturn(20L);
        Mockito.when(trigger1.getJobsToBeExecuted()).thenReturn(ImmutableList.of(job1));
        Mockito.when(trigger2.getJobsToBeExecuted()).thenReturn(ImmutableList.of(job2));
        Mockito.when(trigger4.getJobsToBeExecuted()).thenReturn(ImmutableList.of(job4));

        spy.addTrigger(trigger1);
        spy.addTrigger(trigger2);
        spy.addTrigger(trigger4);

        Optional<List<Job>> jobsAt20 = spy.getFiredTriggers(20L);
        Optional<List<Job>> jobsAt10 = spy.getFiredTriggers(10L);
        assertTrue(jobsAt20.isPresent());
        assertThat(ImmutableList.of(job2, job4)).hasSameElementsAs(jobsAt20.get());
        assertTrue(jobsAt10.isPresent());
        assertThat(ImmutableList.of(job1)).hasSameElementsAs(jobsAt10.get());

        verify(spy, times(2)).updateNextEpocToWaitingTriggersMap(anyLong());

        verify(trigger1, times(1)).updateBaseTime(10L);
        verify(trigger2, times(1)).updateBaseTime(20L);
        verify(trigger4, times(1)).updateBaseTime(20L);

        verify(trigger1, times(1)).updateBaseTime(10L);
        verify(trigger2, times(1)).updateBaseTime(20L);
        verify(trigger4, times(1)).updateBaseTime(20L);
    }

    @Test
    public void test_getFiredTriggers_WHEN_no_matched_awaits_THEN_check_response() {
        Mockito.when(trigger1.getNextFireUpTimeStamp()).thenReturn(10L);

        spy.addTrigger(trigger1);

        assertFalse(spy.getFiredTriggers(30L).isPresent());

        verify(spy, never()).updateNextEpocToWaitingTriggersMap(anyLong());

        verify(trigger1, never()).updateBaseTime(10L);
    }
}
