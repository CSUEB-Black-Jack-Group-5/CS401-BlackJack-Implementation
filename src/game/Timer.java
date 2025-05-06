package game;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Timer class implementation based off the UML Diagram
 */
public class Timer {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    int duration = 0; // in seconds

    /**
     * Constructor to initialize the timer with a given duration (seconds)
     */
    public Timer(int duration) {
        this.duration = duration;
    }

    /**
     * Starts the timer by setting the current time as the start time,
     * and calculating the end time based on the duration.
     */
    public void startTimer() {
        startTime = LocalDateTime.now();
        endTime = startTime.plusSeconds(duration);
    }

    public void stopTimer() {
        endTime = LocalDateTime.now();
    }

    public void resetTimer() {
        startTime = null;
        endTime = null;
    }

    public boolean isTimeUp() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime.plusSeconds(this.duration));
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return duration;
    }

    /**
     * Returns the # of seconds that have passed since the timer started
     */
    public int getElapsedTime() {
        if(startTime == null) {
            return 0;
        } else {
            Duration elapsedTime = Duration.between(startTime, LocalDateTime.now());
            return (int) elapsedTime.getSeconds();
        }

    }


}