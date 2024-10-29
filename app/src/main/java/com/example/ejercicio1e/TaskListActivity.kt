package com.example.ejercicio1e

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ejercicio1e.ui.theme.Ejercicio1eTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TaskListActivity : ComponentActivity() {
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Obtiene el idioma seleccionado de la intención
        val language = intent.getStringExtra("LANGUAGE") ?: "es"
        setContent {
            Ejercicio1eTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Pasa el idioma seleccionado a la pantalla de la lista de tareas
                    TaskListScreen(modifier = Modifier.padding(innerPadding), language = language, db = db)
                }
            }
        }
    }
}

@Composable
fun TaskListScreen(modifier: Modifier = Modifier, language: String, db: FirebaseFirestore) {
    var taskName by remember { mutableStateOf("") }
    var tareasPendientes by remember { mutableStateOf(listOf<String>()) }
    var tareasHechas by remember { mutableStateOf(listOf<String>()) }
    var selectedTask by remember { mutableStateOf<String?>(null) }
    var selectedDoneTask by remember { mutableStateOf<String?>(null) }
    var showPendingTasks by remember { mutableStateOf(false) }
    var showDoneTasks by remember { mutableStateOf(false) }

    // Leer datos de Firestore
    LaunchedEffect(Unit) {
        db.collection("tasks").addSnapshotListener { snapshot, e ->
            if (e != null) return@addSnapshotListener
            if (snapshot != null && !snapshot.isEmpty) {
                val pendingTasks = mutableListOf<String>()
                val doneTasks = mutableListOf<String>()
                for (doc in snapshot.documents) {
                    val task = doc.getString("name") ?: ""
                    val isDone = doc.getBoolean("done") ?: false
                    if (isDone) {
                        doneTasks.add(task)
                    } else {
                        pendingTasks.add(task)
                    }
                }
                tareasPendientes = pendingTasks
                tareasHechas = doneTasks
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Botón para añadir una nueva tarea
        Button(onClick = {
            if (taskName.isNotBlank()) {
                val task = hashMapOf(
                    "name" to taskName,
                    "done" to false
                )
                db.collection("tasks").add(task)
                taskName = ""
            }
        }) {
            Text(text = if (language == "es") "Añadir tarea" else "Add task")
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Campo de texto para ingresar el nombre de la tarea
        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text(if (language == "es") "Nombre de la tarea" else "Task name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            // Botón para mostrar/ocultar las tareas pendientes
            Button(onClick = { showPendingTasks = !showPendingTasks }) {
                Text(text = if (language == "es") "Pendientes" else "Pending")
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Botón para mostrar/ocultar las tareas hechas
            Button(onClick = { showDoneTasks = !showDoneTasks }) {
                Text(text = if (language == "es") "Hechas" else "Done")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Lista de tareas pendientes
        if (showPendingTasks) {
            Text(text = if (language == "es") "Lista de tareas pendientes" else "Pending tasks list", fontWeight = FontWeight.Bold)
            LazyColumn {
                items(tareasPendientes) { tarea ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedTask = if (selectedTask == tarea) null else tarea
                            }
                    ) {
                        Text(text = tarea, modifier = Modifier.weight(1f))
                        if (selectedTask == tarea) {
                            // Botón para marcar la tarea como hecha
                            Button(onClick = {
                                db.collection("tasks").whereEqualTo("name", tarea).get().addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        db.collection("tasks").document(document.id).update("done", true)
                                    }
                                }
                                selectedTask = null
                            }) {
                                Text(if (language == "es") "Hecha" else "Done")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            // Botón para borrar la tarea
                            Button(onClick = {
                                db.collection("tasks").whereEqualTo("name", tarea).get().addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        db.collection("tasks").document(document.id).delete()
                                    }
                                }
                                selectedTask = null
                            }) {
                                Text(if (language == "es") "Borrar" else "Delete")
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Lista de tareas hechas
        if (showDoneTasks) {
            Text(text = if (language == "es") "Lista de tareas hechas" else "Done tasks list", fontWeight = FontWeight.Bold)
            LazyColumn {
                items(tareasHechas) { tarea ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedDoneTask = if (selectedDoneTask == tarea) null else tarea
                            }
                    ) {
                        Text(
                            text = tarea,
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.Green)
                        )
                        if (selectedDoneTask == tarea) {
                            // Botón para deshacer la tarea hecha
                            Button(onClick = {
                                db.collection("tasks").whereEqualTo("name", tarea).get().addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        db.collection("tasks").document(document.id).update("done", false)
                                    }
                                }
                                selectedDoneTask = null
                            }) {
                                Text(if (language == "es") "Deshacer" else "Undo")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            // Botón para borrar la tarea hecha
                            Button(onClick = {
                                db.collection("tasks").whereEqualTo("name", tarea).get().addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        db.collection("tasks").document(document.id).delete()
                                    }
                                }
                                selectedDoneTask = null
                            }) {
                                Text(if (language == "es") "Borrar" else "Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}