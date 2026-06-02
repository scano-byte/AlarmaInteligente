# 🔔 Sistema de Alarmas Inteligentes

## Descripción del Proyecto

AlarmaInteligente es un sistema completo de gestión de alarmas desarrollado en **Java puro** (sin interfaz gráfica). Implementa la lógica interna de un despertador inteligente similar al de un smartphone moderno, con funcionalidades básicas y avanzadas.

El proyecto se centra en:
- ✅ Diseño orientado a objetos robusto
- ✅ Lógica de negocio desacoplada e independiente
- ✅ Funcionalidades básicas y avanzadas
- ✅ Principios SOLID aplicados
- ✅ Documentación técnica profesional

---

## 🎯 Objetivos del Sistema

1. **Gestionar múltiples alarmas** con configuración flexible
2. **Detectar conflictos** entre alarmas cercanas
3. **Implementar despertar circadiano** gradual y saludable
4. **Modo vacaciones** para desactivar temporalmente todas las alarmas
5. **Estadísticas de sueño** para analizar hábitos de descanso
6. **Sistema de snooze** con límites configurables
7. **Repetición semanal** compleja (días específicos, laborales, fin de semana)

---

## 🛠️ Tecnologías Utilizadas

| Componente | Tecnología |
|-----------|-----------|
| **Lenguaje** | Java 11+ |
| **Paradigma** | Orientado a Objetos |
| **Build** | javac (compilador nativo) |
| **Estructura** | Diseño en paquete único |
| **Sincronización** | LocalDateTime (java.time) |

---

## 📋 Funcionalidades Implementadas

### Funcionalidades Básicas ✅

- ✅ Crear/eliminar alarmas
- ✅ Activar/desactivar alarmas
- ✅ Configurar hora, minuto y etiqueta
- ✅ Múltiples alarmas simultáneas
- ✅ Repetición semanal (todos, laborales, fin de semana, días específicos)
- ✅ Sonido personalizado y volumen (0-100%)
- ✅ Sistema de snooze (posponer) con contadores
- ✅ Detener alarma completamente
- ✅ Consultar próximas alarmas activas

### Funcionalidades Avanzadas ✅

1. **Despertar Circadiano** (Circadian Mode)
   - Aumento gradual de volumen y brillo
   - Personalización de duración, volumen inicial/final y brillo
   - Simulación de amanecer natural

2. **Modo Vacaciones** (Vacation Mode)
   - Desactivar temporalmente todas las alarmas
   - Definir rango de fechas
   - Restauración automática al finalizar

3. **Detector de Conflictos** (Alarm Conflict Detector)
   - Alerta cuando dos alarmas suenan muy cerca (<5 min)
   - Threshold configurable (1-30 minutos)
   - Análisis de pares de alarmas

4. **Estadísticas de Sueño** (Sleep Statistics)
   - Registro de períodos de sueño
   - Cálculo de promedio, mínimo y máximo de horas
   - Índice de calidad del sueño (0-100)
   - Consistencia y análisis de snoozes

---

## 📁 Estructura del Proyecto

```
AlarmaInteligente/
├── src/
│   ├── Alarm.java                    # Modelo principal de alarma
│   ├── SoundProfile.java             # Configuración de sonido
│   ├── RecurrencePattern.java        # Patrón de repetición
│   ├── SnoozeConfig.java             # Configuración de snooze
│   ├── AlarmManager.java             # Gestor central
│   ├── SnoozeManager.java            # Gestor de snooze
│   ├── CircadianMode.java            # Despertar circadiano
│   ├── VacationMode.java             # Modo vacaciones
│   ├── AlarmConflictDetector.java    # Detector de conflictos
│   ├── Conflict.java                 # Modelo de conflicto
│   ├── SleepStatistics.java          # Estadísticas de sueño
│   ├── SleepRecord.java              # Registro de sueño individual
│   └── Main.java                     # Programa de demostración
├── bin/                              # Archivos compilados (.class)
├── docs/
│   ├── DISEÑO_OOP.md                 # Análisis y diseño OOP
│   ├── DIAGRAMA_CLASES_UML.md        # Diagrama de clases en Mermaid
│   └── CASOS_DE_USO.md               # Especificación de casos de uso
├── tests/                            # Directorio para tests (futuro)
├── README.md                         # Este archivo
└── .git/                             # Control de versiones

```

---

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Java 11 o superior instalado
- Terminal/línea de comandos

### Compilación

```bash
cd AlarmaInteligente
javac -d ./bin src/*.java
```

### Ejecución de la Demostración

```bash
java -cp ./bin Main
```

### Salida Esperada

El programa muestra:
1. ✅ Creación de 4 alarmas
2. ✅ Configuración de repetición y sonido
3. ✅ Activación de alarmas
4. ✅ Detección de conflictos
5. ✅ Próximas alarmas
6. ✅ Demostración de snooze
7. ✅ Modo Circadiano simulado
8. ✅ Modo Vacaciones
9. ✅ Estadísticas de sueño
10. ✅ Reporte general del sistema

---

## 🏗️ Diseño Orientado a Objetos

### Clases Principales

#### `Alarm` (Modelo)
- Representa una alarma individual
- Almacena: hora, minuto, etiqueta, estado
- Contiene: SoundProfile, RecurrencePattern, SnoozeConfig
- Responsabilidad: datos y validación de alarma

#### `AlarmManager` (Gestor Principal)
- Gestiona CRUD completo de alarmas
- Integra todos los componentes avanzados
- Responsabilidad: orquestación del sistema

#### `CircadianMode` (Despertar Gradual)
- Calcula progresión de volumen y brillo
- Responsabilidad: lógica de despertar circadiano

#### `VacationMode` (Vacaciones)
- Activa/desactiva temporalmente todas las alarmas
- Responsabilidad: gestión del modo vacaciones

#### `AlarmConflictDetector` (Detector de Conflictos)
- Detecta alarmas cercanas
- Responsabilidad: análisis de conflictos

#### `SleepStatistics` (Estadísticas)
- Registra y analiza hábitos de sueño
- Responsabilidad: análisis estadístico

### Relaciones Entre Clases

```
AlarmManager ──┬─→ Alarm (0..*)
               ├─→ SnoozeManager
               ├─→ CircadianMode
               ├─→ VacationMode
               ├─→ AlarmConflictDetector
               └─→ SleepStatistics

Alarm ─────────┬─→ SoundProfile
               ├─→ RecurrencePattern
               └─→ SnoozeConfig
```

### Principios SOLID Aplicados

| Principio | Aplicación |
|-----------|-----------|
| **S** | Cada clase tiene una única responsabilidad |
| **O** | Extensible sin modificar código existente |
| **L** | Posibilidad de subclases intercambiables |
| **I** | Interfaces específicas (futuro) |
| **D** | Dependencias inyectables |

---

## 📊 Ejemplo de Uso

```java
// Crear gestor de alarmas
AlarmManager manager = new AlarmManager();

// Crear una alarma
Alarm alarm = manager.createAlarm(7, 0, "Despertador");
alarm.getRecurrencePattern().setWeekdays();
manager.enableAlarm(alarm.getId());

// Configurar sonido
SoundProfile sound = new SoundProfile("nature", 75, true);
alarm.updateSoundProfile(sound);

// Activar modo circadiano
CircadianMode circadian = manager.getCircadianMode();
circadian.setEnabled(true);
circadian.setDurationMinutes(20);

// Ver próximas alarmas
List<Alarm> next = manager.getNextActiveAlarms(5);

// Detectar conflictos
List<Conflict> conflicts = manager.detectConflicts();
```

---

## 📖 Documentación Adicional

- [DISEÑO_OOP.md](docs/DISEÑO_OOP.md) - Análisis completo de diseño orientado a objetos
- [DIAGRAMA_CLASES_UML.md](docs/DIAGRAMA_CLASES_UML.md) - Diagrama de clases UML en Mermaid
- [CASOS_DE_USO.md](docs/CASOS_DE_USO.md) - Especificación detallada de 11 casos de uso

---

## 🔄 Flujo de Trabajo Git

Se utilizó el siguiente flujo de trabajo:

```
main (rama principal)
 │
 └─→ develop (rama de desarrollo)
      │
      ├─→ feature/alarm-management
      ├─→ feature/advanced-features
      ├─→ feature/snooze-management
      └─→ feature/statistics

Commits descriptivos siguiendo convención:
  - feat: Nueva funcionalidad
  - fix: Corrección de bug
  - docs: Documentación
  - refactor: Refactorización de código
```

---

## 📝 Reflexión sobre IA Generativa

### Cómo se utilizó IA

✅ **Generación de estructuras de código**: Plantillas iniciales de clases
✅ **Asistencia en validaciones**: Métodos de validación y manejo de excepciones
✅ **Documentación**: Comentarios Javadoc
✅ **Revisión de lógica**: Verificación de algoritmos de cálculo

### Limitaciones Encontradas

⚠️ **Índices de arrays**: Necesidad de revisión manual de límites
⚠️ **Lógica de negocio**: Se requirió validación manual de reglas
⚠️ **Optimización**: Reescrituras para cumplir requisitos específicos
⚠️ **Estructura de paquetes**: Restructuración inicial para simplificar

### Validación Manual Realizada

✓ Compilación y ejecución exitosa
✓ Prueba de todos los casos de uso principales
✓ Revisión de algoritmos de detección de conflictos
✓ Validación de cálculos de estadísticas de sueño

---

## 🎓 Aprendizajes Principales

1. **Diseño OOP robusto**: Separación de responsabilidades
2. **Manejo de tiempo**: Uso de java.time.LocalDateTime y DayOfWeek
3. **Colecciones en Java**: ArrayList, HashMap, EnumSet
4. **Validación de entrada**: Precondiciones y postcondiciones
5. **Patrones de diseño**: Manager Pattern, Strategy Pattern
6. **Documentación técnica**: Especificación clara de requisitos

---

## 🚀 Mejoras Futuras

### Funcionalidades Adicionales

- [ ] Persistencia en base de datos SQLite
- [ ] Alarmas geolocalizadas (simuladas)
- [ ] Retos matemáticos para desactivar alarma
- [ ] Alarmas por categorías (trabajo, estudio, etc.)
- [ ] Integración con API de clima
- [ ] Exportación de estadísticas a CSV/JSON
- [ ] API REST para integración con frontend

### Mejoras Técnicas

- [ ] Suite de tests JUnit completa
- [ ] Logging con SLF4J/Logback
- [ ] Inyección de dependencias (Spring)
- [ ] Serialización/Deserialización XML/JSON
- [ ] Multihilo para alarmas simultáneas
- [ ] Patrón Observer para notificaciones

---

## 📄 Licencia

Este proyecto es educativo y de código abierto.

---

## 👤 Autor

Desarrollado como práctica educativa de:
- Análisis de requisitos
- Diseño orientado a objetos
- Implementación en Java
- Documentación técnica profesional
- Uso responsable de IA generativa

---

## 📞 Contacto y Soporte

Para preguntas o sugerencias, consultar la documentación técnica en `/docs/`

---

**Última actualización:** Junio 2026  
**Estado:** Funcional - Fase 1 Completada