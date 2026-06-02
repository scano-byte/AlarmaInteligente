package alarmaInteligente;

import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Clase que representa un registro de sueño.
 */
public class SleepRecord {
    private LocalDateTime bedTime;
    private LocalDateTime wakeUpTime;
    private Duration duration;

    public SleepRecord(LocalDateTime bedTime, LocalDateTime wakeUpTime) {
        if (bedTime == null || wakeUpTime == null) {
            throw new IllegalArgumentException("Las horas de sueño no pueden ser nulas");
        }
        if (wakeUpTime.isBefore(bedTime)) {
            throw new IllegalArgumentException("La hora de despertar debe ser posterior a la de acostarse");
        }

        this.bedTime = bedTime;
        this.wakeUpTime = wakeUpTime;
        this.duration = Duration.between(bedTime, wakeUpTime);
    }

    public LocalDateTime getBedTime() {
        return bedTime;
    }

    public LocalDateTime getWakeUpTime() {
        return wakeUpTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public double getHours() {
        return duration.toMinutes() / 60.0;
    }

    @Override
    public String toString() {
        return String.format("SleepRecord{acostado=%s, despierto=%s, duración=%.1f h}",
                bedTime, wakeUpTime, getHours());
    }
}
