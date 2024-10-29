solo hay un commit porque este ejercicio lo hice en otro repositorio y lo unico que he hecho aqui es moverlo a este nuevo repositorio e implementarle el firebase, el otro repositorio donde están los commits es examen1eventos

https://github.com/ffernandoss/ejercicio1e.git

# Ejercicio1e

Esta es una aplicación de Android que demuestra el uso de Firebase Firestore para gestionar una lista de tareas. La aplicación permite a los usuarios añadir tareas, marcarlas como hechas y eliminarlas. También soporta cambiar entre los idiomas español e inglés.

## Características

- Añadir nuevas tareas
- Marcar tareas como hechas
- Eliminar tareas
- Cambiar entre los idiomas español e inglés
- Almacenar tareas en Firebase Firestore

## Uso

1. **Ejecuta la aplicación en un dispositivo Android o emulador.**

2. **Añadir una nueva tarea:**
    - Ingresa el nombre de la tarea en el campo de texto.
    - Haz clic en el botón "Añadir tarea".

3. **Marcar una tarea como hecha:**
    - Haz clic en una tarea en la lista de tareas pendientes.
    - Haz clic en el botón "Hecha".

4. **Eliminar una tarea:**
    - Haz clic en una tarea en la lista de tareas pendientes o hechas.
    - Haz clic en el botón "Borrar".

5. **Cambiar idioma:**
    - Haz clic en el botón "Seleccionar idioma" en la pantalla de bienvenida para cambiar entre español e inglés.

## Estructura del proyecto

- `MainActivity.kt`: La actividad principal que muestra la pantalla de bienvenida y maneja la selección de idioma.
- `TaskListActivity.kt`: La actividad que muestra la lista de tareas y maneja las operaciones de tareas.
- `ui/theme`: Contiene el tema y el estilo de la aplicación.
