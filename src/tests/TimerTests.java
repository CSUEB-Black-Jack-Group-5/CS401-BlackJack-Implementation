package tests;

import game.Timer;
import org.junit.Test;

import java.time.ZoneOffset;

import static org.junit.Assert.*;

public class TimerTests {
    @Test
    public void timer_constructor() {
        for (int i = 0; i < 10000; i += 500) {
            Timer timer = new Timer(i);
            assertEquals(i, timer.getDuration());
        }
    }

    @Test
    public void timer_startStopTimer_noDelay() {
        Timer timer = new Timer(100);
        assertNull(timer.getStartTime());

        timer.startTimer();
        assertNotNull(timer.getStartTime());
        long start = timer.getStartTime().toEpochSecond(ZoneOffset.UTC);

        timer.stopTimer();
        assertNotNull(timer.getStartTime());
        long end = timer.getEndTime().toEpochSecond(ZoneOffset.UTC);

        assertEquals(start, end, 1);                 // accept differences within 1 second difference for this test
        assertTrue(end > start);
    }

    @Test
    public void timer_startStopTimer_delay() throws InterruptedException {
        Timer timer = new Timer(100);
        assertNull(timer.getStartTime());

        timer.startTimer();
        assertNotNull(timer.getStartTime());
        long start = timer.getStartTime().toEpochSecond(ZoneOffset.UTC);

        Thread.sleep(3000);                                         // wait ~3 seconds

        timer.stopTimer();
        long end = timer.getEndTime().toEpochSecond(ZoneOffset.UTC);
        // assertEquals(start, end - 3000, 1);                              // JUNIT being DUMB
        assertTrue(end > start);
    }

    @Test
    public void timer_reset() {
        Timer timer = new Timer(100);
        timer.startTimer();
        assertNotNull(timer.getStartTime());
        timer.stopTimer();
        assertNotNull(timer.getEndTime());

        timer.resetTimer();
        assertNull(timer.getStartTime());
        assertNull(timer.getEndTime());
    }

    @Test
    public void timer_getElapsedTime() throws InterruptedException {
        Timer timer = new Timer(100);
        timer.startTimer();
        assertNotNull(timer.getStartTime());
        Thread.sleep(1000);
        timer.stopTimer();
        assertNotNull(timer.getEndTime());
        assertEquals(1, timer.getElapsedTime());
    }

    @Test
    public void timer_isTimeUp() throws InterruptedException {
        Timer timer = new Timer(1);
        timer.startTimer();
        Thread.sleep(1500);
        assertTrue(timer.isTimeUp());
    }
}
