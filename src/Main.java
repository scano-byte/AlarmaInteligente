import alarmaInteligente.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase Main: Demostración de funcionalidad del Sistema de Alarmas Inteligentes
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║  SISTEMA DE ALARMAS INTELIGENTES - DEMOSTRACIÓN                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝\n");

        // Crear el gestor de alarmas
        AlarmManager manager = new AlarmManager();

        // 1. CREAR ALARMAS =========================================================
        System.out.println("1️⃣  CREANDO ALARMAS\n");

        Alarm alarm1 = manager.createAlarm(7, 0, "Despertador Matinal");
        Alarm alarm2 = manager.createAlarm(8, 30, "Reunión de trabajo");
        Alarm alarm3 = manager.createAlarm(22, 30, "Recordatorio de sueño");

        System.out.println("✅ Alarma 1: " + alarm1.getLabel() + " a las " + alarm1.getHour() + ":" + String.format("%02d", alarm1.getMinute()));
        System.out.println("✅ Alarma 2: " + alarm2.getLabel() + " a las " + alarm2.getHour() + ":" + String.format("%02d", alarm2.getMinute()));
        System.out.println("✅ Alarma 3: " + alarm3.getLabel() + " a las " + alarm3.getHour() + ":" + String.format("%02d", alarm3.getMinute()));

        // 2. CONFIGURAR ALARMAS ====================================================
        System.out.println("\n2️⃣  CONFIGURANDO ALARMAS\n");

        // Configurar repetición semanal para Alarma 1 (días laborales)
        alarm1.getRecurrencePattern().setWeekdays();
        System.out.println("✅ Alarma 1 configurada para: " + alarm1.getRecurrencePattern());

        // Configurar sonido personalizado para Alarma 2
        SoundProfile soundProfile2 = new SoundProfile(SoundProfile.SOUND_MUSIC, 75, true);
        alarm2.updateSoundProfile(soundProfile2);
        System.out.println("✅ Alarma 2 con sonido: " + alarm2.getSoundProfile());

        // Configurar snooze personalizado para Alarma 3
        SnoozeConfig snoozeConfig3 = new SnoozeConfig(15, 3);
        alarm3.updateSnoozeConfig(snoozeConfig3);
        System.out.println("✅ Alarma 3 con snooze: " + alarm3.getSnoozeConfig());

        // 3. ACTIVAR ALARMAS =======================================================
        System.out.println("\n3️⃣  ACTIVANDO ALARMAS\n");

        manager.enableAlarm(alarm1.getId());
        manager.enableAlarm(alarm2.getId());
        manager.enableAlarm(alarm3.getId());

        System.out.println("✅ Todas las alarmas están activas");
        System.out.println("   Total activas: " + manager.getActiveAlarmsCount());

        // 4. DETECTAR CONFLICTOS ===================================================
        System.out.println("\n4️⃣  DETECTANDO CONFLICTOS\n");

        // Crear dos alarmas muy cercanas (conflicto)
        Alarm conflictAlarm = manager.createAlarm(7, 3, "Alarma Conflictiva");
        manager.enableAlarm(conflictAlarm.getId());
        conflictAlarm.getRecurrencePattern().setWeekdays();

        System.out.println(manager.getConflictReport());

        // 5. VER PRÓXIMAS ALARMAS ==================================================
        System.out.println("\n5️⃣  PRÓXIMAS ALARMAS ACTIVAS\n");

        System.out.println("Top 3 próximas alarmas:");
        for (Alarm alarm : manager.getNextActiveAlarms(3)) {
            System.out.println(String.format("  • %02d:%02d - %s (%s)", 
                    alarm.getHour(), alarm.getMinute(), alarm.getLabel(), alarm.getRecurrencePattern()));
        }

        // 6. DEMOSTRACIÓN DE SNOOZE ===============================================
        System.out.println("\n6️⃣  DEMOSTRACIÓN DE SNOOZE\n");

        System.out.println("Iniciando snooze para: " + alarm3.getLabel());
        LocalDateTime snoozeTime = manager.snoozeAlarm(alarm3.getId());
        System.out.println("✅ Próxima activación: " + snoozeTime);
        System.out.println("   Contador snooze: " + alarm3.getSnoozeConfig());

        System.out.println("\nIntentando snooze adicional...");
        snoozeTime = manager.snoozeAlarm(alarm3.getId());
        System.out.println("✅ Segunda activación: " + snoozeTime);
        System.out.println("   Contador: " + alarm3.getSnoozeConfig());

        // 7. PARAR ALARMA ===========================================================
        System.out.println("\n7️⃣  DETENIENDO ALARMA\n");

        manager.stopAlarm(alarm3.getId());
        System.out.println("✅ Alarma " + alarm3.getLabel() + " detenida");
        System.out.println("   Contador snooze reseteado: " + alarm3.getSnoozeConfig());

        // 8. MODO CIRCADIANO =======================================================
        System.out.println("\n8️⃣  MODO DESPERTAR CIRCADIANO\n");

        CircadianMode circadian = manager.getCircadianMode();
        circadian.setDurationMinutes(20);
        circadian.setInitialVolume(5);
        circadian.setFinalVolume(100);
        circadian.setEnabled(true);

        System.out.println("✅ Modo Circadiano activado: " + circadian);
        circadian.simulateWakeup();

        // 9. MODO VACACIONES =======================================================
        System.out.println("\n9️⃣  MODO VACACIONES\n");

        LocalDate today = LocalDate.now();
        LocalDate vacationEnd = today.plusDays(7);

        System.out.println("Activando modo vacaciones del " + today + " al " + vacationEnd);
        manager.enableVacationMode(today, vacationEnd);

        System.out.println("✅ " + manager.getVacationMode());
        System.out.println("   Alarmas activas ahora: " + manager.getActiveAlarmsCount() + " (deberían ser 0)");

        // Desactivar modo vacaciones
        manager.disableVacationMode();
        System.out.println("\n✅ Modo vacaciones desactivado");
        System.out.println("   Alarmas activas restauradas: " + manager.getActiveAlarmsCount());

        // 10. ESTADÍSTICAS DE SUEÑO ================================================
        System.out.println("\n🔟 ESTADÍSTICAS DE SUEÑO\n");

        SleepStatistics stats = manager.getSleepStatistics();

        // Registrar algunos periodos de sueño
        LocalDateTime bedTime1 = LocalDateTime.now().minusHours(8);
        LocalDateTime wakeTime1 = LocalDateTime.now();
        manager.recordSleep(bedTime1, wakeTime1);

        LocalDateTime bedTime2 = LocalDateTime.now().minusHours(7).minusMinutes(30);
        LocalDateTime wakeTime2 = LocalDateTime.now().minusHours(-1);
        manager.recordSleep(bedTime2, wakeTime2);

        System.out.println(stats.generateReport());

        // 11. REPORTE GENERAL ======================================================
        System.out.println("\n1️⃣ 1️⃣  REPORTE GENERAL DEL SISTEMA\n");

        System.out.println(manager.generateSystemReport());

        // 12. INFORMACIÓN DETALLADA ================================================
        System.out.println("\n1️⃣ 2️⃣  INFORMACIÓN DETALLADA DE UNA ALARMA\n");

        System.out.println(alarm1.toDetailedString());

        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║  FIN DE LA DEMOSTRACIÓN                                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
    }
}
