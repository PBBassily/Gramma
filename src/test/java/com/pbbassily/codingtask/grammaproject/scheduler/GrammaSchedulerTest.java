package com.pbbassily.codingtask.grammaproject.scheduler;

import com.pbbassily.codingtask.grammaproject.time.GrammaTime;
import com.pbbassily.codingtask.grammaproject.time.IntervalGuardian;
import com.pbbassily.codingtask.grammaproject.trigger.Trigger;
import com.pbbassily.codingtask.grammaproject.trigger.TriggersTracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Timer;
import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GrammaSchedulerTest {

    @Mock
    private GrammaSchedulerContext context;

    @Mock
    private Timer timer;

    @Mock
    private TriggersTracker triggersTracker;

    @Mock
    private ExecutorService executorService;

    @Mock
    private GrammaTime epocTime;

    @Mock
    private IntervalGuardian intervalGuardian;

    @Mock
    private Trigger trigger;

    private GrammaScheduler underTest;
    private GrammaScheduler spy;



    @BeforeEach
    public void setup() {
        when(context.getEpocTime()).thenReturn(epocTime);
        when(context.getTimer()).thenReturn(timer);
        when(context.getTriggersTracker()).thenReturn(triggersTracker);
        when(context.getIntervalGuardian()).thenReturn(intervalGuardian);
        when(context.getExecutorService()).thenReturn(executorService);

        underTest = new GrammaScheduler(context);

        spy = Mockito.spy(underTest);
    }

    @Test
    public void test_registerTrigger_WHEN_trigger_mocked_THEN_correctly_added() {
        underTest.registerTrigger(trigger);
        verify(triggersTracker).addTrigger(trigger);
    }

    @Test
    public void test_start_THEN_schedule_timer_and_save_initial_timestamp() {
        underTest.start();
        verify(timer).schedule(any(), anyLong(), anyLong());
        verify(intervalGuardian).setInitialTimestamp(anyLong());
    }

    @Test
    public void test_stop_THEN_stop_executor_and_timer() {
        underTest.stop();
        verify(timer).cancel();
        verify(executorService).shutdown();
    }

    @Test
    public void test_start_WHEN_guardian_return_false_THEN_stop_not_called() {
        when(intervalGuardian.isIntervalElapsed(anyLong())).thenReturn(false);
        spy.start();
        verify(spy, never()).stop();
    }

    @Test
    public void test_start_WHEN_guardian_return_ture_THEN_stop_should_called() {
        when(intervalGuardian.isIntervalElapsed(anyLong())).thenReturn(true);
        spy.start();
        verify(spy).stop();
    }

}
