package alarmaInteligente;

import java.time.LocalTime;
import java.util.UUID;

/**
 * Clase que representa una alarma individual.
 */
public class Alarm {
    private final String id;
    private int hour;
    private int minute;
    private String label;
    private boolean isActive;
    private SoundProfile soundProfile;
    private RecurrencePattern recurrencePattern;
    private SnoozeConfig snoozeConfig;

    public Alarm(int hour, int minute, String label) {
        validateTime(hour, minute);
        validateLabel(label);

        this.id = UUID.randomUUID().toString();
        this.hour = hour;
        this.minute = minute;
        this.label = label;
        this.isActive = false;
        this.soundProfile = new SoundProfile();
        this.recurrencePattern = new RecurrencePattern();
        this.snoozeConfig = new SnoozeConfig();
    }

    private void validateTime(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(String.format("La hora debe estar entre 0 y 23, recibido: %d", hour));
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException(String.format("El minuto debe estar entre 0 y 59, recibido: %d", minute));
        }
    }

    private void validateLabel(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new IllegalArgumentException("La etiqueta no puede estar vacía");
        }
    }

    public String getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getLabel() {
        return label;
    }

    public boolean isActive() {
        return isActive;
    }

    public SoundProfile getSoundProfile() {
        return soundProfile;
    }

    public RecurrencePattern getRecurrencePattern() {
        return recurrencePattern;
    }

    public SnoozeConfig getSnoozeConfig() {
        return snoozeConfig;
    }

    public LocalTime getTime() {
        return LocalTime.of(hour, minute);
    }

    public void setHour(int hour) {
        validateTime(hour, minute);
        this.hour = hour;
    }

    public void setMinute(int minute) {
        validateTime(hour, minute);
        this.minute = minute;
    }

    public void setLabel(String label) {
        validateLabel(label);
        this.label = label;
    }

    public void updateSoundProfile(SoundProfile soundProfile) {
        if (soundProfile == null) {
            throw new IllegalArgumentException("El perfil de sonido no puede ser nulo");
        }
        this.soundProfile = soundProfile;
    }

    public void updateRecurrence(RecurrencePattern recurrencePattern) {
        if (recurrencePattern == null) {
            throw new IllegalArgumentException("El patrón de repetición no puede ser nulo");
        }
        this.recurrencePattern = recurrencePattern;
    }

    public void updateSnoozeConfig(SnoozeConfig snoozeConfig) {
        if (snoozeConfig == null) {
            throw new IllegalArgumentException("La configuración de snooze no puede ser nula");
        }
        this.snoozeConfig = snoozeConfig;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    public void setTime(int hour, int minute) {
        validateTime(hour, minute);
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Alarm)) {
            return false;
        }
        return this.id.equals(((Alarm) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
                "Alarm{id='%s', hora=%02d:%02d, etiqueta='%s', activa=%s, %s, %s, %s}",
                id.substring(0, 8) + "...",
                hour, minute,
                label,
                isActive ? "sí" : "no",
                recurrencePattern,
                soundProfile,
                snoozeConfig
        );
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALARMA DETALLADA ===\n");
        sb.append(String.format("ID: %s\n", id));
        sb.append(String.format("Hora: %02d:%02d\n", hour, minute));
        sb.append(String.format("Etiqueta: %s\n", label));
        sb.append(String.format("Estado: %s\n", isActive ? "Activa" : "Inactiva"));
        sb.append(String.format("Repetición: %s\n", recurrencePattern));
        sb.append(String.format("Sonido: %s\n", soundProfile));
        sb.append(String.format("Snooze: %s\n", snoozeConfig));
        return sb.toString();
    }
}
