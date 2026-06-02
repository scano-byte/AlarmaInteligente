package alarmaInteligente;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase principal que gestiona todas las alarmas del sistema.
 */
public class AlarmManager {
    private List<Alarm> alarms;
    private SnoozeManager snoozeManager;
    private CircadianMode circadianMode;
    private VacationMode vacationMode;
    private AlarmConflictDetector conflictDetector;
    private SleepStatistics sleepStatistics;

    public AlarmManager() {
        this.alarms = new ArrayList<>();
        this.snoozeManager = new SnoozeManager();
        this.circadianMode = new CircadianMode();
        this.vacationMode = new VacationMode();
        this.conflictDetector = new AlarmConflictDetector();
        this.sleepStatistics = new SleepStatistics();
    }

    // ============ CRUD BÁSICO ============

    public Alarm createAlarm(int hour, int minute, String label) {
        Alarm alarm = new Alarm(hour, minute, label);
        alarms.add(alarm);
        return alarm;
    }

    public Alarm getAlarmById(String alarmId) {
        if (alarmId == null) {
            return null;
        }
        return alarms.stream()
                .filter(a -> a.getId().equals(alarmId))
                .findFirst()
                .orElse(null);
    }

    public List<Alarm> getAllAlarms() {
        return new ArrayList<>(alarms);
    }

    public List<Alarm> getActiveAlarms() {
        return alarms.stream()
                .filter(Alarm::isActive)
                .collect(Collectors.toList());
    }

    public boolean deleteAlarm(String alarmId) {
        Alarm alarm = getAlarmById(alarmId);
        if (alarm != null) {
            alarms.remove(alarm);
            snoozeManager.resetSnooze(alarmId);
            vacationMode.deactivatedAlarms.remove(alarmId);
            return true;
        }
        return false;
    }

    public boolean updateAlarm(Alarm alarm) {
        if (alarm == null) {
            return false;
        }
        Alarm existing = getAlarmById(alarm.getId());
        if (existing != null) {
            existing.setHour(alarm.getHour());
            existing.setMinute(alarm.getMinute());
            existing.setLabel(alarm.getLabel());
            existing.updateSoundProfile(alarm.getSoundProfile());
            existing.updateRecurrence(alarm.getRecurrencePattern());
            existing.updateSnoozeConfig(alarm.getSnoozeConfig());
            return true;
        }
        return false;
    }

    // ============ ACTIVACIÓN/DESACTIVACIÓN ============

    public boolean enableAlarm(String alarmId) {
        if (vacationMode.isEnabled()) {
            return false;
        }

        Alarm alarm = getAlarmById(alarmId);
        if (alarm != null) {
            alarm.activate();
            return true;
        }
        return false;
    }

    public boolean disableAlarm(String alarmId) {
        Alarm alarm = getAlarmById(alarmId);
        if (alarm != null) {
            alarm.deactivate();
            snoozeManager.resetSnooze(alarmId);
            return true;
        }
        return false;
    }

    // ============ BÚSQUEDA Y FILTRADO ============

    public List<Alarm> getNextActiveAlarms(int count) {
        LocalTime now = LocalTime.now();
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        return alarms.stream()
                .filter(Alarm::isActive)
                .filter(a -> a.getRecurrencePattern().isOneTime() ||
                            a.getRecurrencePattern().shouldActivateOn(today))
                .sorted(Comparator.comparingInt((Alarm a) -> {
                    int time = a.getHour() * 60 + a.getMinute();
                    int currentTime = now.getHour() * 60 + now.getMinute();
                    return (time - currentTime + 1440) % 1440;
                }))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Alarm> checkAlarmsDue() {
        LocalTime now = LocalTime.now();
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        return alarms.stream()
                .filter(Alarm::isActive)
                .filter(a -> a.getRecurrencePattern().isOneTime() ||
                            a.getRecurrencePattern().shouldActivateOn(today))
                .filter(a -> !snoozeManager.isAlarmSnoozed(a.getId()))
                .filter(a -> {
                    int alarmMinutes = a.getHour() * 60 + a.getMinute();
                    int currentMinutes = now.getHour() * 60 + now.getMinute();
                    return alarmMinutes == currentMinutes;
                })
                .collect(Collectors.toList());
    }

    // ============ SNOOZE ============

    public LocalDateTime snoozeAlarm(String alarmId) {
        Alarm alarm = getAlarmById(alarmId);
        if (alarm == null) {
            return null;
        }

        SnoozeConfig snoozeConfig = alarm.getSnoozeConfig();
        if (!snoozeConfig.canSnooze()) {
            return null;
        }

        snoozeConfig.incrementSnooze();
        LocalDateTime snoozeTime = snoozeManager.snoozeAlarm(alarmId, snoozeConfig.getSnoozeInterval());
        sleepStatistics.recordSnooze(alarmId);

        return snoozeTime;
    }

    public boolean stopAlarm(String alarmId) {
        Alarm alarm = getAlarmById(alarmId);
        if (alarm != null) {
            alarm.getSnoozeConfig().resetSnooze();
            snoozeManager.resetSnooze(alarmId);
            sleepStatistics.recordAlarmDismissed();
            return true;
        }
        return false;
    }

    // ============ CONFLICTOS ============

    public List<Conflict> detectConflicts() {
        return conflictDetector.detectConflicts(getActiveAlarms());
    }

    public String getConflictReport() {
        return conflictDetector.getConflictReport(getActiveAlarms());
    }

    public List<Alarm> getConflictingAlarms(String alarmId) {
        return conflictDetector.getConflictingAlarms(alarmId, alarms);
    }

    // ============ MODO CIRCADIANO ============

    public CircadianMode getCircadianMode() {
        return circadianMode;
    }

    public void enableCircadianMode() {
        circadianMode.setEnabled(true);
    }

    public void disableCircadianMode() {
        circadianMode.setEnabled(false);
    }

    // ============ MODO VACACIONES ============

    public VacationMode getVacationMode() {
        return vacationMode;
    }

    public void enableVacationMode(LocalDate startDate, LocalDate endDate) {
        vacationMode.enable(startDate, endDate);

        getActiveAlarms().forEach(alarm -> {
            alarm.deactivate();
            vacationMode.addDeactivatedAlarm(alarm.getId());
        });
    }

    public void disableVacationMode() {
        for (String alarmId : vacationMode.getDeactivatedAlarms()) {
            Alarm alarm = getAlarmById(alarmId);
            if (alarm != null) {
                alarm.activate();
            }
        }

        vacationMode.disable();
        vacationMode.clearDeactivatedAlarms();
    }

    // ============ ESTADÍSTICAS ============

    public SleepStatistics getSleepStatistics() {
        return sleepStatistics;
    }

    public void recordSleep(LocalDateTime bedTime, LocalDateTime wakeUpTime) {
        sleepStatistics.recordSleep(bedTime, wakeUpTime);
    }

    public String getSleepStatisticsReport() {
        return sleepStatistics.generateReport();
    }

    // ============ INFORMACIÓN GENERAL ============

    public int getTotalAlarms() {
        return alarms.size();
    }

    public int getActiveAlarmsCount() {
        return (int) alarms.stream().filter(Alarm::isActive).count();
    }

    public int getSnoozedCount() {
        return snoozeManager.getSnoozedCount();
    }

    public String generateSystemReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n╔════════════════════════════════════════╗\n");
        report.append("║  REPORTE DEL SISTEMA DE ALARMAS      ║\n");
        report.append("╚════════════════════════════════════════╝\n\n");

        report.append(String.format("Total de alarmas: %d\n", getTotalAlarms()));
        report.append(String.format("Alarmas activas: %d\n", getActiveAlarmsCount()));
        report.append(String.format("Alarmas pospuestas: %d\n", getSnoozedCount()));

        if (vacationMode.isEnabled()) {
            report.append(String.format("Modo Vacaciones: ✅ %s\n", vacationMode));
        }

        if (circadianMode.isEnabled()) {
            report.append(String.format("Modo Circadiano: ✅ %s\n", circadianMode));
        }

        List<Alarm> nextAlarms = getNextActiveAlarms(5);
        if (!nextAlarms.isEmpty()) {
            report.append("\n📋 Próximas alarmas:\n");
            for (Alarm alarm : nextAlarms) {
                report.append(String.format("  • %02d:%02d - %s\n",
                        alarm.getHour(), alarm.getMinute(), alarm.getLabel()));
            }
        }

        List<Conflict> conflicts = detectConflicts();
        if (!conflicts.isEmpty()) {
            report.append(String.format("\n⚠️  Conflictos detectados: %d\n", conflicts.size()));
        }

        report.append("\n📊 Estadísticas:\n");
        report.append(String.format("  %s\n", sleepStatistics));

        return report.toString();
    }

    @Override
    public String toString() {
        return String.format("AlarmManager{total=%d, activas=%d, pospuestas=%d}",
                getTotalAlarms(), getActiveAlarmsCount(), getSnoozedCount());
    }
}
