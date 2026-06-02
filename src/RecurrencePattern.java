package alarmaInteligente;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;

/**
 * Clase que representa el patrón de repetición de una alarma.
 */
public class RecurrencePattern {
    private Set<DayOfWeek> daysOfWeek;
    private boolean isOneTime;

    public RecurrencePattern() {
        this.daysOfWeek = EnumSet.noneOf(DayOfWeek.class);
        this.isOneTime = true;
    }

    public RecurrencePattern(Set<DayOfWeek> daysOfWeek) {
        if (daysOfWeek == null || daysOfWeek.isEmpty()) {
            throw new IllegalArgumentException("El conjunto de días no puede estar vacío");
        }
        this.daysOfWeek = EnumSet.copyOf(daysOfWeek);
        this.isOneTime = false;
    }

    public void addDay(DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException("El día no puede ser nulo");
        }
        daysOfWeek.add(day);
        isOneTime = false;
    }

    public void removeDay(DayOfWeek day) {
        if (day != null) {
            daysOfWeek.remove(day);
        }
    }

    public void setWeekdays() {
        daysOfWeek.clear();
        daysOfWeek.add(DayOfWeek.MONDAY);
        daysOfWeek.add(DayOfWeek.TUESDAY);
        daysOfWeek.add(DayOfWeek.WEDNESDAY);
        daysOfWeek.add(DayOfWeek.THURSDAY);
        daysOfWeek.add(DayOfWeek.FRIDAY);
        isOneTime = false;
    }

    public void setWeekend() {
        daysOfWeek.clear();
        daysOfWeek.add(DayOfWeek.SATURDAY);
        daysOfWeek.add(DayOfWeek.SUNDAY);
        isOneTime = false;
    }

    public void setAllDays() {
        daysOfWeek.clear();
        for (DayOfWeek day : DayOfWeek.values()) {
            daysOfWeek.add(day);
        }
        isOneTime = false;
    }

    public boolean shouldActivateOn(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            return false;
        }
        if (isOneTime) {
            return false;
        }
        return daysOfWeek.contains(dayOfWeek);
    }

    public boolean isOneTime() {
        return isOneTime;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return EnumSet.copyOf(daysOfWeek);
    }

    public void setOneTime() {
        this.isOneTime = true;
        this.daysOfWeek.clear();
    }

    @Override
    public String toString() {
        if (isOneTime) {
            return "RecurrencePattern{una sola vez}";
        }
        if (daysOfWeek.size() == 7) {
            return "RecurrencePattern{todos los días}";
        }
        if (daysOfWeek.size() == 5 && !daysOfWeek.contains(DayOfWeek.SATURDAY)) {
            return "RecurrencePattern{días laborales}";
        }
        if (daysOfWeek.size() == 2 && daysOfWeek.contains(DayOfWeek.SATURDAY)) {
            return "RecurrencePattern{fin de semana}";
        }
        return String.format("RecurrencePattern{%s}", daysOfWeek);
    }
}
