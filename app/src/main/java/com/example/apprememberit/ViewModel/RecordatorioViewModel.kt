package com.example.apprememberit.ViewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class RecordatorioViewModel(context: Context) : ViewModel() {

    private val prefs: SharedPreferences = context.getSharedPreferences("datosApp", Context.MODE_PRIVATE)
    private val gson = Gson()

    var recordatorios = mutableStateListOf<Recordatorio>()
        private set

    init {
        cargarRecordatorios()
    }

    private fun cargarRecordatorios() {
        val recordatoriosJson = prefs.getString("recordatorios", null)
        if (!recordatoriosJson.isNullOrEmpty()) {
            val type = object : TypeToken<List<Recordatorio>>() {}.type
            val listaRecordatorios: List<Recordatorio> = gson.fromJson(recordatoriosJson, type)
            recordatorios.addAll(listaRecordatorios)
        }
    }

    private fun guardarRecordatorios() {
        val editor = prefs.edit()
        val recordatoriosJson = gson.toJson(recordatorios)
        editor.putString("recordatorios", recordatoriosJson)
        editor.apply()
    }

    fun agregarRecordatorio(recordatorio: Recordatorio) {
        recordatorios.add(recordatorio)
        guardarRecordatorios()
    }

    fun eliminarRecordatorio(recordatorio: Recordatorio) {
        recordatorios.remove(recordatorio)
        guardarRecordatorios()
    }

    fun actualizarRecordatorio(recordatorioAntiguo: Recordatorio, recordatorioNuevo: Recordatorio) {
        val index = recordatorios.indexOf(recordatorioAntiguo)
        if (index != -1) {
            recordatorios[index] = recordatorioNuevo
            guardarRecordatorios()
        }
    }

    fun obtenerRecordatoriosPorEmail(email: String?): List<Recordatorio> {
        return if (email.isNullOrEmpty()) {
            recordatorios.filter { it.emailUsuario.isNullOrEmpty() }
        } else {
            recordatorios.filter { it.emailUsuario == email }
        }
    }

    fun generarId(): String {
        return UUID.randomUUID().toString()
    }

    fun obtenerRecordatoriosSinCuenta(): List<Recordatorio> {
        return recordatorios.filter { it.emailUsuario.isNullOrEmpty() }
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