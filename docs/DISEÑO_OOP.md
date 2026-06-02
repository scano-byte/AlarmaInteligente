# Diseño Orientado a Objetos - Sistema de Alarmas Inteligentes

## 1. Análisis de Requisitos

### Funcionalidades Básicas
- ✅ Crear/eliminar alarmas
- ✅ Activar/desactivar alarmas
- ✅ Configurar hora, minuto, nombre
- ✅ Múltiples alarmas
- ✅ Repetición semanal (día, varios días, todos, laborales, fin de semana)
- ✅ Sonido personalizado y volumen
- ✅ Snooze (posponer)
- ✅ Detener alarma
- ✅ Consultar próximas alarmas activas

### Funcionalidades Avanzadas (implementaremos 3+)
- ✅ **Despertar Circadiano**: Aumento gradual de brillo y volumen
- ✅ **Modo Vacaciones**: Desactivar temporalmente todas las alarmas
- ✅ **Detección de Conflictos**: Alertar sobre alarmas muy cercanas
- ✅ **Perfil de Sueño**: Registrar estadísticas (horas dormidas, veces pospuestas)

---

## 2. Modelo de Clases

### 2.1 Clases Base

#### `Alarm` (Modelo Principal)
**Responsabilidad**: Representar una alarma individual con su configuración.

```
Atributos:
- id: String (UUID único)
- hour: int (0-23)
- minute: int (0-59)
- label: String
- isActive: boolean
- soundProfile: SoundProfile
- recurrencePattern: RecurrencePattern
- snoozeConfig: SnoozeConfig

Métodos:
+ getId(): String
+ getHour(): int
+ getMinute(): int
+ setHour(int): void
+ setMinute(int): void
+ setLabel(String): void
+ isActive(): boolean
+ activate(): void
+ deactivate(): void
+ updateSoundProfile(SoundProfile): void
+ updateRecurrence(RecurrencePattern): void
```

#### `SoundProfile` (Configuración de Sonido)
**Responsabilidad**: Encapsular la configuración de sonido de una alarma.

```
Atributos:
- soundType: String (default, nature, music, etc.)
- volume: int (0-100)
- isVibrationEnabled: boolean

Métodos:
+ getSoundType(): String
+ setSoundType(String): void
+ getVolume(): int
+ setVolume(int): void
+ isVibrationEnabled(): boolean
+ setVibration(boolean): void
```

#### `RecurrencePattern` (Patrón de Repetición)
**Responsabilidad**: Gestionar la lógica de repetición semanal.

```
Atributos:
- daysOfWeek: Set<DayOfWeek> {MONDAY, TUESDAY, ..., SUNDAY}
- isOneTime: boolean

Métodos:
+ addDay(DayOfWeek): void
+ removeDay(DayOfWeek): void
+ setWeekdays(): void (lunes-viernes)
+ setWeekend(): void (sábado-domingo)
+ setAllDays(): void
+ shouldActivateOn(DayOfWeek): boolean
+ isOneTime(): boolean
```

#### `SnoozeConfig` (Configuración de Snooze)
**Responsabilidad**: Gestionar la configuración de postponimiento.

```
Atributos:
- snoozeIntervalMinutes: int (por defecto 10)
- maxSnoozeCount: int (máximo de veces a posponer)
- currentSnoozeCount: int

Métodos:
+ getSnoozeInterval(): int
+ setSnoozeInterval(int): void
+ getMaxSnoozeCount(): int
+ canSnooze(): boolean
+ incrementSnooze(): void
+ resetSnooze(): void
```

---

### 2.2 Clases de Gestión

#### `AlarmManager` (Gestor Principal)
**Responsabilidad**: Gestionar el ciclo de vida completo de alarmas (CRUD, búsqueda).

```
Atributos:
- alarms: List<Alarm>
- activeAlarms: PriorityQueue<Alarm> (ordenadas por tiempo)
- alarmDAO: AlarmDAO (persistencia)

Métodos:
+ createAlarm(hour, minute, label): Alarm
+ deleteAlarm(id): boolean
+ getAlarmById(id): Alarm
+ getAllAlarms(): List<Alarm>
+ getActiveAlarms(): List<Alarm>
+ getNextActiveAlarms(count): List<Alarm>
+ enableAlarm(id): void
+ disableAlarm(id): void
+ updateAlarm(Alarm): void
+ checkAlarmsDue(): List<Alarm> (para ahora)
```

#### `SnoozeManager` (Gestor de Snooze)
**Responsabilidad**: Manejar la lógica de postponimiento de alarmas.

```
Atributos:
- snoozedAlarms: Map<String, LocalDateTime> (id -> próxima activación)

Métodos:
+ snoozeAlarm(alarmId): LocalDateTime
+ isAlarmSnoozed(alarmId): boolean
+ getSnoozeTime(alarmId): LocalDateTime
+ resetSnooze(alarmId): void
+ clearExpiredSnoozes(): void
```

---

### 2.3 Funcionalidades Avanzadas

#### `CircadianMode` (Despertar Circadiano)
**Responsabilidad**: Gestionar la activación gradual de alarmas.

```
Atributos:
- durationMinutes: int (duración del despertar gradual)
- initialVolume: int (volumen inicial %)
- finalVolume: int (volumen final %)
- initialBrightness: int (brillo inicial %)
- finalBrightness: int (brillo final %)

Métodos:
+ startCircadianWakeup(Alarm): void
+ getCurrentVolume(elapsedTime): int
+ getCurrentBrightness(elapsedTime): int
+ getProgressPercentage(elapsedTime): double
```

#### `VacationMode` (Modo Vacaciones)
**Responsabilidad**: Desactivar temporalmente todas las alarmas.

```
Atributos:
- isEnabled: boolean
- startDate: LocalDate
- endDate: LocalDate
- deactivatedAlarms: List<String> (ids)

Métodos:
+ enable(startDate, endDate): void
+ disable(): void
+ isActive(): boolean
+ isWithinVacation(date): boolean
```

#### `AlarmConflictDetector` (Detector de Conflictos)
**Responsabilidad**: Detectar alarmas que suenan demasiado cercanas.

```
Atributos:
- conflictThresholdMinutes: int (por defecto 5 minutos)

Métodos:
+ detectConflicts(alarms): List<Conflict>
+ hasConflict(alarm1, alarm2): boolean
+ getConflictingAlarms(alarmId): List<Alarm>
+ setThreshold(minutes): void
```

#### `SleepStatistics` (Perfil de Sueño)
**Responsabilidad**: Registrar y analizar estadísticas de sueño.

```
Atributos:
- sleepRecords: List<SleepRecord>
- totalSnoozes: int
- averageSnoozeTime: double

Métodos:
+ recordSleep(bedTime, wakeUpTime): void
+ recordSnooze(alarmId, duration): void
+ getAverageSleepHours(): double
+ getTotalSnoozes(): int
+ getSleepQualityScore(): int (0-100)
+ getStatistics(): SleepStatisticsReport
```

---

## 3. Relaciones Entre Clases

```
AlarmManager ────┐
                 ├─→ Alarm (composición 1..*)
                 ├─→ SnoozeManager (agregación)
                 ├─→ CircadianMode (agregación)
                 ├─→ VacationMode (agregación)
                 ├─→ AlarmConflictDetector (agregación)
                 └─→ SleepStatistics (agregación)

Alarm ────────┐
              ├─→ SoundProfile (composición)
              ├─→ RecurrencePattern (composición)
              └─→ SnoozeConfig (composición)
```

---

## 4. Principios de Diseño Aplicados

### 4.1 SOLID
- **S (Single Responsibility)**: Cada clase tiene una única responsabilidad
  - `Alarm`: almacenar datos
  - `AlarmManager`: gestionar alarmas
  - `CircadianMode`: gestionar despertar gradual
  
- **O (Open/Closed)**: Abierto para extensión, cerrado para modificación
  - Se pueden agregar nuevos tipos de alarmas sin modificar código existente
  
- **L (Liskov Substitution)**: Las subclases sustituyen a las superclases
  - Posible implementar diferentes tipos de alarmas (alarmas por ubicación, por clima, etc.)
  
- **I (Interface Segregation)**: Interfaces específicas
  - `IAlarmListener`: para escuchar eventos de alarmas
  
- **D (Dependency Inversion)**: Depender de abstracciones
  - AlarmManager usa interfaces, no implementaciones concretas

### 4.2 Patrones de Diseño
- **Manager Pattern**: `AlarmManager`, `SnoozeManager`
- **Strategy Pattern**: `SoundProfile` y `RecurrencePattern` como estrategias
- **Observer Pattern**: Sistema de listeners para eventos de alarmas
- **Singleton Pattern**: Para instancia única del AlarmManager (opcional)

---

## 5. Encapsulación y Visibilidad

| Elemento | Visibilidad | Justificación |
|----------|-------------|---------------|
| `Alarm.id` | `private` | Debe asignarse al crear, no modificarse |
| `Alarm.hour/minute` | `private` | Acceso solo mediante setters con validación |
| `Alarm.isActive` | `private` | Cambios controlados mediante activate/deactivate |
| `SoundProfile.volume` | `private` | Rango validado (0-100) |
| `AlarmManager.alarms` | `private` | Acceso controlado mediante métodos |
| Métodos públicos | `public` | Interfaz de uso directo |

---

## 6. Manejo de Tiempo

### Consideraciones:
- Usar `java.time.LocalTime` para hora/minuto
- Usar `java.time.LocalDateTime` para marca de tiempo completa
- Usar `java.time.DayOfWeek` para días de semana
- Usar `java.time.LocalDate` para fechas en modo vacaciones

### Comparación de Alarmas:
```
Alarma A: 07:30
Alarma B: 07:35
Diferencia: 5 minutos → Conflicto detectado
```

---

## 7. Casos de Uso Principales

1. **Crear alarma**: Usuario → AlarmManager.createAlarm()
2. **Activar alarma**: Usuario → Alarm.activate() + AlarmManager.enableAlarm()
3. **Snooze**: AlarmManager → SnoozeManager.snoozeAlarm()
4. **Despertar circadiano**: AlarmManager → CircadianMode.startCircadianWakeup()
5. **Modo vacaciones**: Usuario → VacationMode.enable()
6. **Detectar conflictos**: AlarmManager → AlarmConflictDetector.detectConflicts()

---

## 8. Decisiones de Diseño Razonadas

### ¿Por qué Alarm es una clase separada?
- Encapsula los datos y comportamiento de una alarma individual
- Responsabilidad única
- Facilita operaciones CRUD

### ¿Por qué RecurrencePattern es una clase?
- La lógica de repetición es compleja (7 días, laborales, fin de semana)
- Reutilizable
- Separación de responsabilidades

### ¿Por qué SoundProfile es una clase?
- Agrupa la configuración de sonido
- Se puede reutilizar en múltiples alarmas (perfiles predefinidos)
- Fácil de serializar/persistir

### ¿Por qué SnoozeManager es independiente?
- Gestiona un aspecto importante de forma aislada
- Puede ser testeable por separado
- Evita que AlarmManager se sobrecargue

