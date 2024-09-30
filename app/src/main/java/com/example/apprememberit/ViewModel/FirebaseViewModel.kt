package com.example.apprememberit.ViewModel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class FirebaseViewModel(context: Context) : ViewModel() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("recordatorios")
    var recordatorios = mutableStateListOf<Recordatorio>()
        private set

    init {
        cargarRecordatorios()
    }

    private fun cargarRecordatorios() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recordatorios.clear()
                for (recordatorioSnapshot in snapshot.children) {
                    val recordatorio = recordatorioSnapshot.getValue(Recordatorio::class.java)
                    recordatorio?.let {
                        recordatorios.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun generarId(): String {
        return database.push().key ?: ""
    }

    fun agregarRecordatorio(recordatorio: Recordatorio, onResult: (Boolean, String) -> Unit) {
        val key = database.push().key
        if (key != null) {
            val recordatorioConId = recordatorio.copy(id = key)
            database.child(key).setValue(recordatorioConId).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Recordatorio agregado exitosamente")
                } else {
                    onResult(false, "Error al agregar el recordatorio")
                }
            }.addOnFailureListener {
                onResult(false, "Error al agregar el recordatorio: ${it.message}")
            }
        }
    }

    fun eliminarRecordatorio(recordatorio: Recordatorio, onResult: (Boolean, String) -> Unit) {
        // Asegurarse de que el recordatorio tiene un id válido
        if (recordatorio.id.isNullOrEmpty()) {
            onResult(false, "Error: El recordatorio no tiene un ID válido")
            return
        }

        // Referenciar el nodo basado en el id del recordatorio
        val ref = database.child(recordatorio.id!!)
        ref.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, "Recordatorio eliminado exitosamente")
            } else {
                onResult(false, "Error al eliminar el recordatorio")
            }
        }.addOnFailureListener {
            onResult(false, "Error al eliminar el recordatorio: ${it.message}")
        }
    }


    fun actualizarRecordatorio(recordatorioAntiguo: Recordatorio, recordatorioNuevo: Recordatorio, onResult: (Boolean, String) -> Unit) {
        if (recordatorioAntiguo.id.isNullOrEmpty()) {
            onResult(false, "No se puede actualizar: el recordatorio no tiene un ID válido")
            return
        }

        // Buscar el recordatorio por su ID en lugar de por el título
        val ref = database.child(recordatorioAntiguo.id!!)
        ref.setValue(recordatorioNuevo).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, "Recordatorio actualizado exitosamente")
            } else {
                onResult(false, "Error al actualizar el recordatorio")
            }
        }.addOnFailureListener {
            onResult(false, "Error al actualizar el recordatorio: ${it.message}")
        }
    }


    fun obtenerRecordatoriosPorEmail(email: String?, onResult: (List<Recordatorio>, String?) -> Unit) {
        val recordatoriosFiltrados = mutableListOf<Recordatorio>()
        val query = if (email.isNullOrEmpty()) {
            database.orderByChild("emailUsuario").equalTo(null)
        } else {
            database.orderByChild("emailUsuario").equalTo(email)
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (recordatorioSnapshot in snapshot.children) {
                    val recordatorio = recordatorioSnapshot.getValue(Recordatorio::class.java)
                    recordatorio?.let {
                        recordatoriosFiltrados.add(it)
                    }
                }
                onResult(recordatoriosFiltrados, null) // Éxito
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(emptyList(), "Error al obtener los recordatorios: ${error.message}")
            }
        })
    }

    fun obtenerUsuarioPorEmail(email: String, onResult: (Usuario?, String?) -> Unit) {
        // Inicializa la base de datos
        val database = FirebaseDatabase.getInstance().getReference("usuarios")

        // Crea la consulta para buscar al usuario por su email
        val query = database.orderByChild("email").equalTo(email)

        // Ejecuta la consulta
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Si el snapshot existe, se encontró un usuario
                if (snapshot.exists()) {
                    for (usuarioSnapshot in snapshot.children) {
                        val usuario = usuarioSnapshot.getValue(Usuario::class.java)
                        usuario?.let {
                            onResult(it, null) // Éxito: se encontró el usuario
                            return
                        }
                    }
                } else {
                    // No se encontró ningún usuario con ese email
                    onResult(null, "No se encontró ningún usuario con el email $email")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(null, "Error al obtener el usuario: ${error.message}")
            }
        })
    }



    // Lista de categorías de recordatorios
    val categorias = listOf(
        "Seleccione",
        "Medicamentos",
        "Citas Médicas",
        "Actividades Físicas",
        "Reuniones",
        "Pagos",
        "Eventos",
        "Compras",
        "Tareas del Hogar"
    )
}