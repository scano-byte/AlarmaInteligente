package alarmaInteligente;

/**
 * Clase que configura el comportamiento de snooze (posponer) de una alarma.
 */
public class SnoozeConfig {
    private int snoozeIntervalMinutes;
    private int maxSnoozeCount;
    private int currentSnoozeCount;

    private static final int DEFAULT_SNOOZE_INTERVAL = 10;
    private static final int DEFAULT_MAX_SNOOZE_COUNT = 5;

    public SnoozeConfig() {
        this.snoozeIntervalMinutes = DEFAULT_SNOOZE_INTERVAL;
        this.maxSnoozeCount = DEFAULT_MAX_SNOOZE_COUNT;
        this.currentSnoozeCount = 0;
    }

    public SnoozeConfig(int snoozeIntervalMinutes, int maxSnoozeCount) {
        setSnoozeInterval(snoozeIntervalMinutes);
        setMaxSnoozeCount(maxSnoozeCount);
        this.currentSnoozeCount = 0;
    }

    public int getSnoozeInterval() {
        return snoozeIntervalMinutes;
    }

    public void setSnoozeInterval(int minutes) {
        if (minutes < 1 || minutes > 60) {
            throw new IllegalArgumentException("El intervalo de snooze debe estar entre 1 y 60 minutos");
        }
        this.snoozeIntervalMinutes = minutes;
    }

    public int getMaxSnoozeCount() {
        return maxSnoozeCount;
    }

    public void setMaxSnoozeCount(int count) {
        if (count < 1 || count > 10) {
            throw new IllegalArgumentException("El máximo de snoozes debe estar entre 1 y 10");
        }
        this.maxSnoozeCount = count;
    }

    public int getCurrentSnoozeCount() {
        return currentSnoozeCount;
    }

    public boolean canSnooze() {
        return currentSnoozeCount < maxSnoozeCount;
    }

    public void incrementSnooze() {
        if (!canSnooze()) {
            throw new IllegalStateException(
                    String.format("Se ha alcanzado el máximo de snoozes (%d)", maxSnoozeCount)
            );
        }
        currentSnoozeCount++;
    }

    public void resetSnooze() {
        currentSnoozeCount = 0;
    }

    @Override
    public String toString() {
        return String.format("SnoozeConfig{intervalo=%d min, máximo=%d, actual=%d/%d}",
                snoozeIntervalMinutes, maxSnoozeCount, currentSnoozeCount, maxSnoozeCount);
    }
}
