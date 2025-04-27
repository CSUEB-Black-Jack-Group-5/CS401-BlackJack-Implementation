import game.Timer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;

public class TimerTests {
    @Test
    public void timer_constructor() {
        for (int i = 0; i < 10000; i += 500) {
            Timer timer = new Timer(i);
            Assertions.assertEquals(i, timer.getDuration());
        }
    }

    @Test
    public void timer_startStopTimer_noDelay() {
        Timer timer = new Timer(100);
        Assertions.assertNull(timer.getStartTime());

        timer.startTimer();
        Assertions.assertNotNull(timer.getStartTime());
        long start = timer.getStartTime().toEpochSecond(ZoneOffset.UTC);

        timer.stopTimer();
        Assertions.assertNotNull(timer.getStartTime());
        long end = timer.getEndTime().toEpochSecond(ZoneOffset.UTC);

        Assertions.assertEquals(start, end, 1);                 // accept differences within 1 second difference for this test
        Assertions.assertTrue(end > start);
    }

    @Test
    public void timer_startStopTimer_delay() throws InterruptedException {
        Timer timer = new Timer(100);
        Assertions.assertNull(timer.getStartTime());

        timer.startTimer();
        Assertions.assertNotNull(timer.getStartTime());
        long start = timer.getStartTime().toEpochSecond(ZoneOffset.UTC);

        Thread.sleep(3000);                                         // wait ~3 seconds

        timer.stopTimer();
        long end = timer.getEndTime().toEpochSecond(ZoneOffset.UTC);
        Assertions.assertEquals(start, end - 3000, 1);
        Assertions.assertTrue(end > start);
    }

    @Test
    public void timer_reset() {
        Timer timer = new Timer(100);
        timer.startTimer();
        Assertions.assertNotNull(timer.getStartTime());
        timer.stopTimer();
        Assertions.assertNotNull(timer.getEndTime());

        timer.resetTimer();
        Assertions.assertNull(timer.getStartTime());
        Assertions.assertNull(timer.getEndTime());
    }

    @Test
    public void timer_getElapsedTime() throws InterruptedException {
        Timer timer = new Timer(100);
        timer.startTimer();
        Assertions.assertNotNull(timer.getStartTime());
        Thread.sleep(1000);
        timer.stopTimer();
        Assertions.assertNotNull(timer.getEndTime());
        Assertions.assertEquals(1, timer.getElapsedTime());
    }

    @Test
    public void timer_isTimeUp() throws InterruptedException {
        Timer timer = new Timer(1);
        timer.startTimer();
        Thread.sleep(1500);
        Assertions.assertTrue(timer.isTimeUp());
    }
}
