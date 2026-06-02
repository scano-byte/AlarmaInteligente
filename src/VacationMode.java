package alarmaInteligente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa el Modo Vacaciones.
 */
public class VacationMode {
    private boolean isEnabled;
    private LocalDate startDate;
    private LocalDate endDate;
    public List<String> deactivatedAlarms;

    public VacationMode() {
        this.isEnabled = false;
        this.startDate = null;
        this.endDate = null;
        this.deactivatedAlarms = new ArrayList<>();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<String> getDeactivatedAlarms() {
        return new ArrayList<>(deactivatedAlarms);
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("La fecha fin debe ser posterior a la fecha inicio");
        }
    }

    public void enable(LocalDate startDate, LocalDate endDate) {
        validateDates(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
        this.isEnabled = true;
        this.deactivatedAlarms.clear();
    }

    public void disable() {
        this.isEnabled = false;
        this.startDate = null;
        this.endDate = null;
    }

    public boolean isWithinVacation(LocalDate date) {
        if (!isEnabled || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public void addDeactivatedAlarm(String alarmId) {
        if (alarmId != null && !deactivatedAlarms.contains(alarmId)) {
            deactivatedAlarms.add(alarmId);
        }
    }

    public void clearDeactivatedAlarms() {
        deactivatedAlarms.clear();
    }

    public long getVacationDurationDays() {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    @Override
    public String toString() {
        if (!isEnabled) {
            return "VacationMode{desactivado}";
        }
        return String.format(
                "VacationMode{del %s al %s, %d días, %d alarmas desactivadas}",
                startDate, endDate, getVacationDurationDays(), deactivatedAlarms.size()
        );
    }
}
