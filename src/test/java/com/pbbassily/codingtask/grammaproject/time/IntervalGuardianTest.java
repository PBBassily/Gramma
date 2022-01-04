package com.pbbassily.codingtask.grammaproject.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IntervalGuardianTest {

    @Mock
    private GrammaTime execTime;

    private IntervalGuardian underTest;


    @BeforeEach
    public void setup() {
        when(execTime.getValueInMillis()).thenReturn(1L);
        underTest =  new IntervalGuardian(execTime);
    }


    @Test
    public void test_isIntervalElapsed_WHEN_times_up_THEN_return_true() {
        underTest.setInitialTimestamp(1l);
        assertTrue(underTest.isIntervalElapsed(2l));

        assertTrue(underTest.isIntervalElapsed(3l));
    }

    @Test
    public void test_isIntervalElapsed_WHEN_times_not_up_THEN_return_false() {
        underTest.setInitialTimestamp(10l);
        assertFalse(underTest.isIntervalElapsed(2l));

        assertFalse(underTest.isIntervalElapsed(3l));
    }
}
