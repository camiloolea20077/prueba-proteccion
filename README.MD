# Motor de Priorización y Paginación de Tickets

Este módulo implementa un **motor de priorización dinámica de tickets** con **paginación a nivel de base de datos**, usando **Spring Boot + JdbcTemplate + PostgreSQL**.

El objetivo es ordenar los tickets **no solo por un campo fijo**, sino por una **prioridad calculada en tiempo real**, teniendo en cuenta:
- Tipo de ticket
- Prioridad manual
- Tiempo transcurrido desde su creación

---

## 🚀 ¿Qué problema resuelve?

En sistemas de soporte, los tickets antiguos pueden quedar olvidados si solo se ordenan por fecha o prioridad manual.

Este motor garantiza que:
- Los **INCIDENTES** siempre tengan más peso.
- Los tickets **ganen prioridad con el tiempo**.
- El orden sea **justo, automático y dinámico**.
- La paginación sea **eficiente**, incluso con miles de registros.

---

## 🧠 Lógica de Prioridad

La prioridad final se calcula en la base de datos con la siguiente fórmula:

```sql
prioridad_final =
    prioridad_por_tipo
    + prioridad_manual
    + horas_transcurridas_desde_creacion
