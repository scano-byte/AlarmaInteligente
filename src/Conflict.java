package alarmaInteligente;

/**
 * Clase que representa un conflicto entre dos alarmas.
 */
public class Conflict {
    private String alarm1Id;
    private String alarm2Id;
    private int timeDifferenceMinutes;

    public Conflict(String alarm1Id, String alarm2Id, int timeDifferenceMinutes) {
        this.alarm1Id = alarm1Id;
        this.alarm2Id = alarm2Id;
        this.timeDifferenceMinutes = Math.abs(timeDifferenceMinutes);
    }

    public String getAlarm1Id() {
        return alarm1Id;
    }

    public String getAlarm2Id() {
        return alarm2Id;
    }

    public int getTimeDifferenceMinutes() {
        return timeDifferenceMinutes;
    }

    @Override
    public String toString() {
        return String.format("Conflict{alarma1=%s, alarma2=%s, diferencia=%d min}",
                alarm1Id.substring(0, 8) + "...",
                alarm2Id.substring(0, 8) + "...",
                timeDifferenceMinutes);
    }
}
