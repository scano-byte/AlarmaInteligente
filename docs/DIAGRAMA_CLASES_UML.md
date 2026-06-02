# Diagrama UML de Clases

## Diagrama Completo - Sistema de Alarmas Inteligentes

```mermaid
classDiagram
    class Alarm {
        -id: String
        -hour: int
        -minute: int
        -label: String
        -isActive: boolean
        -soundProfile: SoundProfile
        -recurrencePattern: RecurrencePattern
        -snoozeConfig: SnoozeConfig
        +getId() String
        +getHour() int
        +getMinute() int
        +setHour(int) void
        +setMinute(int) void
        +setLabel(String) void
        +isActive() boolean
        +activate() void
        +deactivate() void
        +updateSoundProfile(SoundProfile) void
        +updateRecurrence(RecurrencePattern) void
    }

    class SoundProfile {
        -soundType: String
        -volume: int
        -isVibrationEnabled: boolean
        +getSoundType() String
        +setSoundType(String) void
        +getVolume() int
        +setVolume(int) void
        +isVibrationEnabled() boolean
        +setVibration(boolean) void
    }

    class RecurrencePattern {
        -daysOfWeek: Set~DayOfWeek~
        -isOneTime: boolean
        +addDay(DayOfWeek) void
        +removeDay(DayOfWeek) void
        +setWeekdays() void
        +setWeekend() void
        +setAllDays() void
        +shouldActivateOn(DayOfWeek) boolean
        +isOneTime() boolean
    }

    class SnoozeConfig {
        -snoozeIntervalMinutes: int
        -maxSnoozeCount: int
        -currentSnoozeCount: int
        +getSnoozeInterval() int
        +setSnoozeInterval(int) void
        +getMaxSnoozeCount() int
        +canSnooze() boolean
        +incrementSnooze() void
        +resetSnooze() void
    }

    class AlarmManager {
        -alarms: List~Alarm~
        -snoozeManager: SnoozeManager
        -circadianMode: CircadianMode
        -vacationMode: VacationMode
        -conflictDetector: AlarmConflictDetector
        -sleepStatistics: SleepStatistics
        +createAlarm(int, int, String) Alarm
        +deleteAlarm(String) boolean
        +getAlarmById(String) Alarm
        +getAllAlarms() List~Alarm~
        +getActiveAlarms() List~Alarm~
        +getNextActiveAlarms(int) List~Alarm~
        +enableAlarm(String) void
        +disableAlarm(String) void
        +updateAlarm(Alarm) void
        +checkAlarmsDue() List~Alarm~
    }

    class SnoozeManager {
        -snoozedAlarms: Map~String,LocalDateTime~
        +snoozeAlarm(String) LocalDateTime
        +isAlarmSnoozed(String) boolean
        +getSnoozeTime(String) LocalDateTime
        +resetSnooze(String) void
        +clearExpiredSnoozes() void
    }

    class CircadianMode {
        -durationMinutes: int
        -initialVolume: int
        -finalVolume: int
        -initialBrightness: int
        -finalBrightness: int
        +startCircadianWakeup(Alarm) void
        +getCurrentVolume(long) int
        +getCurrentBrightness(long) int
        +getProgressPercentage(long) double
    }

    class VacationMode {
        -isEnabled: boolean
        -startDate: LocalDate
        -endDate: LocalDate
        -deactivatedAlarms: List~String~
        +enable(LocalDate, LocalDate) void
        +disable() void
        +isActive() boolean
        +isWithinVacation(LocalDate) boolean
    }

    class AlarmConflictDetector {
        -conflictThresholdMinutes: int
        +detectConflicts(List~Alarm~) List~Conflict~
        +hasConflict(Alarm, Alarm) boolean
        +getConflictingAlarms(String) List~Alarm~
        +setThreshold(int) void
    }

    class SleepStatistics {
        -sleepRecords: List~SleepRecord~
        -totalSnoozes: int
        -averageSnoozeTime: double
        +recordSleep(LocalDateTime, LocalDateTime) void
        +recordSnooze(String, long) void
        +getAverageSleepHours() double
        +getTotalSnoozes() int
        +getSleepQualityScore() int
        +getStatistics() SleepStatisticsReport
    }

    class SleepRecord {
        -bedTime: LocalDateTime
        -wakeUpTime: LocalDateTime
        -duration: Duration
        +getDuration() Duration
    }

    class Conflict {
        -alarm1Id: String
        -alarm2Id: String
        -timeDifferenceMinutes: int
        +getAlarm1Id() String
        +getAlarm2Id() String
        +getTimeDifference() int
    }

    class SleepStatisticsReport {
        -averageSleepHours: double
        -totalSnoozes: int
        -sleepQuality: int
        -lastWeekData: List~SleepRecord~
        +getReport() String
    }

    %% Relaciones
    AlarmManager "1" *-- "0..*" Alarm: manages
    AlarmManager "1" *-- "1" SnoozeManager: uses
    AlarmManager "1" *-- "1" CircadianMode: uses
    AlarmManager "1" *-- "1" VacationMode: uses
    AlarmManager "1" *-- "1" AlarmConflictDetector: uses
    AlarmManager "1" *-- "1" SleepStatistics: uses

    Alarm "1" *-- "1" SoundProfile: has
    Alarm "1" *-- "1" RecurrencePattern: has
    Alarm "1" *-- "1" SnoozeConfig: has

    SleepStatistics "1" *-- "0..*" SleepRecord: contains
    SleepStatistics "1" *-- "1" SleepStatisticsReport: generates

    AlarmConflictDetector "1" *-- "0..*" Conflict: detects

    %% Estilos
    style Alarm fill:#ffcccc
    style AlarmManager fill:#ccccff
    style SnoozeManager fill:#ccffcc
    style CircadianMode fill:#ffffcc
    style VacationMode fill:#ffccff
```

---

## Justificación Detallada de la Arquitectura

### Clase: `Alarm` (Modelo Principal)

**¿Por qué existe?**
`Alarm` es la entidad central del sistema. Representa una alarma individual con todos sus parámetros configurables.

**Responsabilidades:**
- Almacenar datos de la alarma (hora, minuto, etiqueta, estado)
- Mantener referencias a su configuración (sonido, repetición, snooze)
- Validar que la hora y minuto estén dentro de rangos válidos
- Proporcionar métodos para activar/desactivar la alarma

**Encapsulación:**
Todos los atributos son `private` para garantizar que nadie pueda alterar directamente el estado de la alarma sin pasar por los métodos de validación. Por ejemplo, no se puede llamar directamente `alarm.hour = 25` (sería inválido), sino que debe usarse `alarm.setHour(25)`, que generará una excepción.

**Relaciones:**
- Composición con `SoundProfile`: Define cómo suena la alarma
- Composición con `RecurrencePattern`: Define cuándo repite
- Composición con `SnoozeConfig`: Define comportamiento del posponer

---

### Clase: `AlarmManager` (Controlador Principal)

**¿Por qué existe?**
`AlarmManager` actúa como el **controlador central** (patrón Controller/Facade). Orquesta todo el sistema, proporcionando una interfaz única para crear, modificar, buscar y gestionar alarmas.

**Responsabilidades:**
- Gestión CRUD de alarmas (Create, Read, Update, Delete)
- Activación/desactivación de alarmas individuales
- Búsqueda de alarmas (por ID, todas activas, próximas que sonarán)
- Detección de alarmas que suenan en este momento
- Integración con componentes avanzados (CircadianMode, VacationMode, etc.)

**Encapsulación:**
Mantiene la lista de alarmas como `private`, evitando que código externo las modifique directamente. Solo se puede acceder a través de métodos públicos que aplican validaciones.

**Relaciones:**
- Composición 1-a-muchos con `Alarm`: Un AlarmManager gestiona múltiples alarmas
- Agregación con `SnoozeManager`, `CircadianMode`, `VacationMode`, etc.: Usa estos componentes sin poseerlos por completo

**Patrón:**
Implementa el patrón **Facade**: presenta una interfaz simple para un subsistema complejo internamente.

---

### Clase: `SoundProfile` (Estrategia de Sonido)

**¿Por qué existe?**
Encapsula toda la configuración de sonido de una alarma, permitiendo cambiar tipo de sonido, volumen y vibración sin modificar la alarma misma.

**Responsabilidades:**
- Almacenar tipo de sonido (default, nature, music, etc.)
- Almacenar volumen (validado entre 0-100)
- Almacenar estado de vibración
- Proporcionar métodos para cambiar estos valores de forma segura

**Encapsulación:**
El volumen es `private int volume` con validación: `setVolume(int v)` lanza excepción si v < 0 o v > 100.

**Patrón:**
Implementa el patrón **Strategy**: diferentes estrategias de sonido pueden implementarse sin cambiar `Alarm`.

---

### Clase: `RecurrencePattern` (Patrón de Repetición)

**¿Por qué existe?**
Maneja la complejidad de la repetición semanal: días específicos, laborales, fin de semana, etc. Separar esta lógica de `Alarm` sigue el principio SRP (Single Responsibility).

**Responsabilidades:**
- Almacenar qué días de la semana debe sonar la alarma
- Determinar si la alarma debe sonar en un día específico
- Proporcionar métodos de conveniencia (setWeekdays, setWeekend, setAllDays)
- Mantener la distinción entre alarmas de una sola vez vs. repetidas

**Estructura de datos:**
Usa `Set<DayOfWeek>` (en realidad `EnumSet`) porque:
- Garantiza sin duplicados
- Búsqueda O(1) para "¿debo sonar hoy?"
- Es eficiente en memoria

---

### Clase: `SnoozeConfig` (Configuración de Posponer)

**¿Por qué existe?**
Encapsula la configuración del snooze para reutilizarla y cambiarla independientemente de la alarma.

**Responsabilidades:**
- Almacenar intervalo de snooze (minutos entre pospuestas)
- Almacenar número máximo de snoozes permitidos
- Rastrear cuántas veces se ha pospuesto en la sesión actual
- Validar si aún se permite posponer

**Encapsulación:**
`currentSnoozeCount` es `private` y solo se incrementa a través de `incrementSnooze()` cuando se verifica que `canSnooze() == true`.

---

### Clase: `SnoozeManager` (Gestor de Pospuestas)

**¿Por qué existe?**
Maneja la lógica de qué alarmas están pospuestas en este momento, cuándo vuelven a sonar, y limpieza de snoozes expirados. Separa responsabilidades de `AlarmManager`.

**Responsabilidades:**
- Mantener un registro de alarmas pospuestas y su próxima hora de activación
- Determinar si una alarma está actualmente pospuesta
- Limpiar snoozes que ya expiraron (pasó su tiempo de reactivación)
- Resetear snooze de una alarma

**Estructura:**
`Map<String, LocalDateTime>`: ID alarma → cuándo vuelve a sonar

**Relación:**
`AlarmManager` usa `SnoozeManager` para gestionar snoozes sin mezclar lógica.

---

### Clase: `CircadianMode` (Despertar Gradual)

**¿Por qué existe?**
Implementa una funcionalidad avanzada: despertar natural y gradual. Calcula cómo aumentar volumen y brillo linealmente en el tiempo.

**Responsabilidades:**
- Configurar duración del despertar gradual
- Configurar volumen/brillo inicial y final
- Calcular volumen y brillo actuales basado en tiempo transcurrido
- Iniciar la simulación de amanecer para una alarma

**Fórmula de progresión:**
```
Progreso = tiempoTranscurrido / duracionTotal
VolumenActual = volumenenInicial + (volumenFinal - volumenInicial) × Progreso
```

**Lógica:**
Si durationMinutes=20, initialVolume=10%, finalVolume=100%:
- En t=0: volumen = 10%
- En t=10min: volumen = 55%
- En t=20min: volumen = 100%

---

### Clase: `VacationMode` (Modo Vacaciones)

**¿Por qué existe?**
Permite desactivar temporalmente todas las alarmas para un rango de fechas sin eliminarlas.

**Responsabilidades:**
- Activar/desactivar el modo vacaciones
- Almacenar rango de fechas (inicio y fin)
- Determinar si una fecha está dentro del período de vacaciones
- Guardar lista de alarmas desactivadas para restaurarlas después

**Encapsulación:**
`isEnabled` es `private` y se cambia mediante `enable(startDate, endDate)` y `disable()`, que validan que las fechas sean coherentes.

---

### Clase: `AlarmConflictDetector` (Detector de Conflictos)

**¿Por qué existe?**
Detecta cuando dos alarmas suenan demasiado cercanas (p. ej., 07:30 y 07:32) para alertar al usuario.

**Responsabilidades:**
- Analizar pares de alarmas y calcular diferencia de tiempo
- Generar objetos `Conflict` cuando dos alarmas están muy cercanas
- Permitir configurar el umbral de conflicto (threshold)
- Buscar todas las alarmas que entran en conflicto con una alarma específica

**Lógica:**
```
Para cada par de alarmas (A1, A2):
  diferencia = |hora1:minuto1 - hora2:minuto2|
  if (diferencia < thresholdMinutes && diferencia > 0) {
      crear nuevo Conflict(A1.id, A2.id, diferencia)
  }
```

---

### Clase: `SleepStatistics` (Análisis de Sueño)

**¿Por qué existe?**
Recopila y analiza datos sobre hábitos de sueño del usuario para proporcionar insights.

**Responsabilidades:**
- Registrar períodos de sueño (hora de dormir y hora de despertar)
- Registrar cada snooze realizado
- Calcular promedio de horas dormidas
- Calcular total de snoozes
- Generar score de calidad de sueño (0-100)
- Generar reporte de estadísticas

**Cálculos:**
- **Promedio de sueño:** suma de duraciones / número de registros
- **Score de calidad:** función que penaliza exceso/defecto de sueño y snoozes frecuentes

**Relación:**
Guarda una lista de `SleepRecord` (cada uno con bedTime y wakeUpTime) y genera `SleepStatisticsReport` bajo demanda.

---

### Clases de Datos: `SleepRecord`, `Conflict`, `SleepStatisticsReport`

Estas son clases simples que encapsulan datos:

- **`SleepRecord`**: Almacena hora de dormir, hora de despertar, y calcula automáticamente la duración.
- **`Conflict`**: Almacena IDs de dos alarmas y su diferencia de tiempo.
- **`SleepStatisticsReport`**: Contiene resultados de análisis listo para mostrar.

Son "Data Transfer Objects" (DTOs) que facilitan pasar datos entre componentes.

---

## Relaciones de Composición vs. Agregación

| Relación | Tipo | Razón |
|----------|------|-------|
| AlarmManager ↔ Alarm | Composición | Alarmas pertenecen a AlarmManager; se destruyen con él |
| Alarm ↔ SoundProfile | Composición | SoundProfile es parte integral e inseparable de Alarm |
| Alarm ↔ RecurrencePattern | Composición | RecurrencePattern define el comportamiento de Alarm |
| AlarmManager ↔ SnoozeManager | Agregación | SnoozeManager puede existir sin AlarmManager |
| AlarmManager ↔ CircadianMode | Agregación | CircadianMode es un servicio opcional que AlarmManager usa |
| SleepStatistics ↔ SleepRecord | Composición | Records pertenecen única y exclusivamente a SleepStatistics |

---

## Explicación de Relaciones

### Composición (vinculación fuerte)
- **AlarmManager ◆ Alarm**: El AlarmManager gestiona un conjunto de alarmas. Si se destruye AlarmManager, se destruyen las alarmas.
- **Alarm ◆ SoundProfile, RecurrencePattern, SnoozeConfig**: Cada alarma tiene exactamente una configuración de sonido, patrón de repetición y snooze. Son parte integral de la alarma.
- **SleepStatistics ◆ SleepRecord**: Los registros de sueño pertenecen única y exclusivamente a SleepStatistics.

### Agregación (vinculación débil)
- **AlarmManager → SnoozeManager, CircadianMode, etc.**: El AlarmManager usa estos componentes, pero pueden existir independientemente.

---

## Justificación de Visibilidad

| Atributo/Método | Visibilidad | Razón |
|-----------------|------------|-------|
| `id` | `private` | Único, inmutable después de creación |
| `hour`, `minute` | `private` | Requieren validación (0-23, 0-59) |
| `isActive` | `private` | Cambios controlados por activate/deactivate |
| `soundProfile` | `private` | Acceso mediante updateSoundProfile() con validación |
| Métodos getter/setter | `public` | Interfaz de usuario del objeto |
| `alarms` (AlarmManager) | `private` | Evita modificación directa de la colección |
| `conflictThresholdMinutes` | `private` | Configurable mediante setter con validación |

---

## Patrones de Diseño Identificados

### 1. **Manager Pattern**
- `AlarmManager`, `SnoozeManager`
- Centraliza la lógica de gestión
- Facilita el acceso y manipulación de datos

### 2. **Composite Pattern**
- `Alarm` contiene `SoundProfile`, `RecurrencePattern`, `SnoozeConfig`
- Permite tratar alarmas como unidades complejas

### 3. **Strategy Pattern**
- `SoundProfile` y `RecurrencePattern` como estrategias intercambiables
- Se puede cambiar el sonido o la repetición sin modificar Alarm

### 4. **Observer Pattern** (futuro)
- Los listeners pueden registrarse para eventos de alarmas
- Ejemplo: UI recibe notificaciones cuando alarma está a punto de sonar

---

## Responsabilidades por Clase

```
┌─────────────────────────────────────────────────────────┐
│ Alarm                                                   │
├─────────────────────────────────────────────────────────┤
│ • Almacenar datos de una alarma individual             │
│ • Gestionar estado (activo/inactivo)                   │
│ • Delegación de comportamiento                         │
├─────────────────────────────────────────────────────────┤
│ Cohesión: ALTA (todos los métodos usan los atributos) │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ AlarmManager                                            │
├─────────────────────────────────────────────────────────┤
│ • CRUD de alarmas                                      │
│ • Búsqueda de alarmas (activas, próximas)             │
│ • Coordinación con componentes avanzados              │
├─────────────────────────────────────────────────────────┤
│ Cohesión: ALTA (centraliza lógica de gestión)         │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ CircadianMode                                           │
├─────────────────────────────────────────────────────────┤
│ • Calcular progresión de volumen y brillo             │
│ • Gestionar duración del despertar                    │
│ • Independiente del resto del sistema                 │
├─────────────────────────────────────────────────────────┤
│ Cohesión: ALTA (todo relacionado a despertar gradual) │
└─────────────────────────────────────────────────────────┘
```

---

## Evolución del Diseño

### Versión 1 (Actual)
- Clases básicas: Alarm, AlarmManager
- Configuración: SoundProfile, RecurrencePattern
- Funcionalidades avanzadas: CircadianMode, VacationMode, AlarmConflictDetector, SleepStatistics

### Versión 2 (Futuro)
- Persistencia: AlarmDAO, alojamiento en BD
- Alarmas por ubicación: LocationBasedAlarm, GeoFenceManager
- Alarmas por clima: WeatherAwareAlarm, WeatherService
- Retos matemáticos: MathChallengeAlarm, ChallengeGenerator
- Sistema de notificaciones: NotificationService, EventListener

