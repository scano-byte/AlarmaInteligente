package alarmaInteligente;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Clase que gestiona el comportamiento de snooze (posponer) de alarmas.
 */
public class SnoozeManager {
    private Map<String, LocalDateTime> snoozedAlarms;

    public SnoozeManager() {
        this.snoozedAlarms = new HashMap<>();
    }

    public LocalDateTime snoozeAlarm(String alarmId, int snoozeMinutes) {
        if (alarmId == null || alarmId.isEmpty()) {
            throw new IllegalArgumentException("El ID de alarma no puede estar vacío");
        }
        if (snoozeMinutes < 1 || snoozeMinutes > 60) {
            throw new IllegalArgumentException("Los minutos de snooze deben estar entre 1 y 60");
        }

        LocalDateTime newActivation = LocalDateTime.now().plusMinutes(snoozeMinutes);
        snoozedAlarms.put(alarmId, newActivation);
        return newActivation;
    }

    public boolean isAlarmSnoozed(String alarmId) {
        if (alarmId == null) {
            return false;
        }
        LocalDateTime snoozeTime = snoozedAlarms.get(alarmId);
        if (snoozeTime == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(snoozeTime)) {
            snoozedAlarms.remove(alarmId);
            return false;
        }
        return true;
    }

    public LocalDateTime getSnoozeTime(String alarmId) {
        if (alarmId == null) {
            return null;
        }
        LocalDateTime snoozeTime = snoozedAlarms.get(alarmId);
        if (snoozeTime != null && LocalDateTime.now().isAfter(snoozeTime)) {
            snoozedAlarms.remove(alarmId);
            return null;
        }
        return snoozeTime;
    }

    public boolean resetSnooze(String alarmId) {
        if (alarmId == null) {
            return false;
        }
        return snoozedAlarms.remove(alarmId) != null;
    }

    public void clearExpiredSnoozes() {
        Iterator<Map.Entry<String, LocalDateTime>> iterator = snoozedAlarms.entrySet().iterator();
        LocalDateTime now = LocalDateTime.now();

        while (iterator.hasNext()) {
            Map.Entry<String, LocalDateTime> entry = iterator.next();
            if (now.isAfter(entry.getValue())) {
                iterator.remove();
            }
        }
    }

    public int getSnoozedCount() {
        clearExpiredSnoozes();
        return snoozedAlarms.size();
    }

    public void clearAllSnoozes() {
        snoozedAlarms.clear();
    }

    @Override
    public String toString() {
        clearExpiredSnoozes();
        return String.format("SnoozeManager{pospuestas=%d}", snoozedAlarms.size());
    }
}
