package alarmaInteligente;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que detecta conflictos entre alarmas.
 */
public class AlarmConflictDetector {
    private int conflictThresholdMinutes;

    private static final int DEFAULT_CONFLICT_THRESHOLD = 5;

    public AlarmConflictDetector() {
        this.conflictThresholdMinutes = DEFAULT_CONFLICT_THRESHOLD;
    }

    public AlarmConflictDetector(int thresholdMinutes) {
        setThreshold(thresholdMinutes);
    }

    public void setThreshold(int minutes) {
        if (minutes < 1 || minutes > 30) {
            throw new IllegalArgumentException("El umbral debe estar entre 1 y 30 minutos");
        }
        this.conflictThresholdMinutes = minutes;
    }

    public int getThreshold() {
        return conflictThresholdMinutes;
    }

    public List<Conflict> detectConflicts(List<Alarm> alarms) {
        List<Conflict> conflicts = new ArrayList<>();

        if (alarms == null || alarms.size() < 2) {
            return conflicts;
        }

        for (int i = 0; i < alarms.size(); i++) {
            for (int j = i + 1; j < alarms.size(); j++) {
                if (hasConflict(alarms.get(i), alarms.get(j))) {
                    int timeDiff = calculateTimeDifference(alarms.get(i), alarms.get(j));
                    conflicts.add(new Conflict(alarms.get(i).getId(), alarms.get(j).getId(), timeDiff));
                }
            }
        }

        return conflicts;
    }

    public boolean hasConflict(Alarm alarm1, Alarm alarm2) {
        if (alarm1 == null || alarm2 == null) {
            return false;
        }

        if (!alarm1.isActive() || !alarm2.isActive()) {
            return false;
        }

        int timeDiff = calculateTimeDifference(alarm1, alarm2);
        return timeDiff <= conflictThresholdMinutes && timeDiff > 0;
    }

    private int calculateTimeDifference(Alarm alarm1, Alarm alarm2) {
        int time1Minutes = alarm1.getHour() * 60 + alarm1.getMinute();
        int time2Minutes = alarm2.getHour() * 60 + alarm2.getMinute();
        return Math.abs(time1Minutes - time2Minutes);
    }

    public List<Alarm> getConflictingAlarms(String alarmId, List<Alarm> allAlarms) {
        List<Alarm> conflictingAlarms = new ArrayList<>();

        if (alarmId == null || allAlarms == null) {
            return conflictingAlarms;
        }

        Alarm targetAlarm = null;
        for (Alarm alarm : allAlarms) {
            if (alarm.getId().equals(alarmId)) {
                targetAlarm = alarm;
                break;
            }
        }

        if (targetAlarm == null) {
            return conflictingAlarms;
        }

        for (Alarm alarm : allAlarms) {
            if (!alarm.getId().equals(alarmId) && hasConflict(targetAlarm, alarm)) {
                conflictingAlarms.add(alarm);
            }
        }

        return conflictingAlarms;
    }

    public String getConflictReport(List<Alarm> alarms) {
        List<Conflict> conflicts = detectConflicts(alarms);

        if (conflicts.isEmpty()) {
            return "✅ No hay conflictos detectados entre las alarmas.";
        }

        StringBuilder report = new StringBuilder();
        report.append(String.format("⚠️  Se detectaron %d conflictos:\n\n", conflicts.size()));

        for (Conflict conflict : conflicts) {
            report.append(String.format("- %s\n", conflict));
        }

        return report.toString();
    }

    @Override
    public String toString() {
        return String.format("AlarmConflictDetector{umbral=%d min}", conflictThresholdMinutes);
    }
}
