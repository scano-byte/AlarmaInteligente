package alarmaInteligente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona estadísticas de sueño del usuario.
 */
public class SleepStatistics {
    private List<SleepRecord> sleepRecords;
    private int totalSnoozes;
    private int totalAlarmsDismissed;

    public SleepStatistics() {
        this.sleepRecords = new ArrayList<>();
        this.totalSnoozes = 0;
        this.totalAlarmsDismissed = 0;
    }

    public void recordSleep(LocalDateTime bedTime, LocalDateTime wakeUpTime) {
        sleepRecords.add(new SleepRecord(bedTime, wakeUpTime));
    }

    public void recordSnooze(String alarmId) {
        totalSnoozes++;
    }

    public void recordAlarmDismissed() {
        totalAlarmsDismissed++;
    }

    public double getAverageSleepHours() {
        if (sleepRecords.isEmpty()) {
            return 0;
        }
        double totalHours = sleepRecords.stream()
                .mapToDouble(SleepRecord::getHours)
                .sum();
        return totalHours / sleepRecords.size();
    }

    public double getMinSleepHours() {
        if (sleepRecords.isEmpty()) {
            return 0;
        }
        return sleepRecords.stream()
                .mapToDouble(SleepRecord::getHours)
                .min()
                .orElse(0);
    }

    public double getMaxSleepHours() {
        if (sleepRecords.isEmpty()) {
            return 0;
        }
        return sleepRecords.stream()
                .mapToDouble(SleepRecord::getHours)
                .max()
                .orElse(0);
    }

    public int getTotalSnoozes() {
        return totalSnoozes;
    }

    public double getAverageSnoozeCount() {
        if (totalAlarmsDismissed == 0) {
            return 0;
        }
        return totalSnoozes / (double) totalAlarmsDismissed;
    }

    public int getSleepQualityScore() {
        if (sleepRecords.isEmpty()) {
            return 0;
        }

        double avgSleep = getAverageSleepHours();
        double consistency = calculateConsistency();
        double snoozeImpact = Math.max(0, 100 - (getTotalSnoozes() * 5));

        double quality = (avgSleep >= 7.0 ? 40 : avgSleep * 5.7) +
                         (consistency * 40) +
                         (snoozeImpact * 0.2);

        return Math.min(100, Math.max(0, (int) Math.round(quality)));
    }

    private double calculateConsistency() {
        if (sleepRecords.size() < 2) {
            return 1.0;
        }

        double avgSleep = getAverageSleepHours();
        double variance = sleepRecords.stream()
                .mapToDouble(r -> Math.pow(r.getHours() - avgSleep, 2))
                .average()
                .orElse(0);

        double stdDev = Math.sqrt(variance);
        return Math.max(0, 1 - (stdDev / 2.0));
    }

    public int getTotalSleepRecords() {
        return sleepRecords.size();
    }

    public List<SleepRecord> getLastRecords(int count) {
        int start = Math.max(0, sleepRecords.size() - count);
        return new ArrayList<>(sleepRecords.subList(start, sleepRecords.size()));
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== REPORTE DE ESTADÍSTICAS DE SUEÑO ===\n\n");
        report.append(String.format("Registros de sueño: %d\n", getTotalSleepRecords()));
        report.append(String.format("Promedio de sueño: %.1f horas\n", getAverageSleepHours()));
        report.append(String.format("Mínimo: %.1f h, Máximo: %.1f h\n", getMinSleepHours(), getMaxSleepHours()));
        report.append(String.format("Total de snoozes: %d\n", getTotalSnoozes()));
        report.append(String.format("Promedio snoozes/alarma: %.2f\n", getAverageSnoozeCount()));
        report.append(String.format("Calidad del sueño: %d/100\n", getSleepQualityScore()));

        return report.toString();
    }

    public void clearRecords() {
        sleepRecords.clear();
        totalSnoozes = 0;
        totalAlarmsDismissed = 0;
    }

    @Override
    public String toString() {
        return String.format("SleepStatistics{registros=%d, promedio=%.1f h, snoozes=%d, calidad=%d/100}",
                getTotalSleepRecords(), getAverageSleepHours(), getTotalSnoozes(), getSleepQualityScore());
    }
}
