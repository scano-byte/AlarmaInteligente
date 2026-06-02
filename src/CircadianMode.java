package alarmaInteligente;

/**
 * Clase que implementa el Modo Despertar Circadiano.
 */
public class CircadianMode {
    private int durationMinutes;
    private int initialVolume;
    private int finalVolume;
    private int initialBrightness;
    private int finalBrightness;
    private boolean isEnabled;

    private static final int DEFAULT_DURATION = 30;
    private static final int DEFAULT_INITIAL_VOLUME = 10;
    private static final int DEFAULT_FINAL_VOLUME = 100;
    private static final int DEFAULT_INITIAL_BRIGHTNESS = 5;
    private static final int DEFAULT_FINAL_BRIGHTNESS = 100;

    public CircadianMode() {
        this.durationMinutes = DEFAULT_DURATION;
        this.initialVolume = DEFAULT_INITIAL_VOLUME;
        this.finalVolume = DEFAULT_FINAL_VOLUME;
        this.initialBrightness = DEFAULT_INITIAL_BRIGHTNESS;
        this.finalBrightness = DEFAULT_FINAL_BRIGHTNESS;
        this.isEnabled = false;
    }

    public CircadianMode(int durationMinutes, int initialVolume, int finalVolume,
                        int initialBrightness, int finalBrightness) {
        setDurationMinutes(durationMinutes);
        setInitialVolume(initialVolume);
        setFinalVolume(finalVolume);
        setInitialBrightness(initialBrightness);
        setFinalBrightness(finalBrightness);
        this.isEnabled = false;
    }

    private void validateDuration(int minutes) {
        if (minutes < 5 || minutes > 60) {
            throw new IllegalArgumentException("La duración debe estar entre 5 y 60 minutos");
        }
    }

    private void validateVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("El volumen debe estar entre 0 y 100");
        }
    }

    private void validateBrightness(int brightness) {
        if (brightness < 0 || brightness > 100) {
            throw new IllegalArgumentException("El brillo debe estar entre 0 y 100");
        }
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int minutes) {
        validateDuration(minutes);
        this.durationMinutes = minutes;
    }

    public int getInitialVolume() {
        return initialVolume;
    }

    public void setInitialVolume(int volume) {
        validateVolume(volume);
        this.initialVolume = volume;
    }

    public int getFinalVolume() {
        return finalVolume;
    }

    public void setFinalVolume(int volume) {
        validateVolume(volume);
        this.finalVolume = volume;
    }

    public int getInitialBrightness() {
        return initialBrightness;
    }

    public void setInitialBrightness(int brightness) {
        validateBrightness(brightness);
        this.initialBrightness = brightness;
    }

    public int getFinalBrightness() {
        return finalBrightness;
    }

    public void setFinalBrightness(int brightness) {
        validateBrightness(brightness);
        this.finalBrightness = brightness;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public double getProgressPercentage(long elapsedTimeMillis) {
        long totalTimeMillis = durationMinutes * 60 * 1000L;
        if (elapsedTimeMillis >= totalTimeMillis) {
            return 100.0;
        }
        return (elapsedTimeMillis * 100.0) / totalTimeMillis;
    }

    public int getCurrentVolume(long elapsedTimeMillis) {
        double progress = getProgressPercentage(elapsedTimeMillis);
        double currentVolume = initialVolume + (progress / 100.0) * (finalVolume - initialVolume);
        return Math.min((int) Math.round(currentVolume), 100);
    }

    public int getCurrentBrightness(long elapsedTimeMillis) {
        double progress = getProgressPercentage(elapsedTimeMillis);
        double currentBrightness = initialBrightness + (progress / 100.0) * (finalBrightness - initialBrightness);
        return Math.min((int) Math.round(currentBrightness), 100);
    }

    public void simulateWakeup() {
        System.out.println("\n🌅 === SIMULANDO DESPERTAR CIRCADIANO ===");
        System.out.println(String.format("Duración: %d minutos\n", durationMinutes));

        long stepMillis = (durationMinutes * 60 * 1000L) / 10;

        for (int step = 1; step <= 10; step++) {
            long elapsedTime = stepMillis * step;
            int volume = getCurrentVolume(elapsedTime);
            int brightness = getCurrentBrightness(elapsedTime);
            double progress = getProgressPercentage(elapsedTime);

            System.out.printf("Paso %2d/10: %.1f%% completo | Volumen: %3d%% | Brillo: %3d%%\n",
                    step, progress, volume, brightness);
        }
        System.out.println("✅ Despertar circadiano finalizado\n");
    }

    @Override
    public String toString() {
        return String.format(
                "CircadianMode{duración=%d min, vol=%d→%d, brillo=%d→%d, habilitado=%s}",
                durationMinutes, initialVolume, finalVolume,
                initialBrightness, finalBrightness,
                isEnabled ? "sí" : "no"
        );
    }
}
