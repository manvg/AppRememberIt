package com.example.apprememberit.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.apprememberit.Recordatorio

class RecordatorioViewModel : ViewModel() {
    var recordatorios = mutableStateListOf(
        Recordatorio("Toma de Medicamento", "Ibuprofeno", "13/09/2023", "08:00", "usuario1@gmail.com"),
        Recordatorio("Cita Médica", "Consulta Dr. Pérez", "14/09/2023", "10:00", "usuario1@gmail.com")
    )

    fun agregarRecordatorio(recordatorio: Recordatorio) {
        recordatorios.add(recordatorio)
    }

    fun eliminarRecordatorio(recordatorio: Recordatorio) {
        recordatorios.remove(recordatorio)
    }

    fun actualizarRecordatorio(recordatorioAntiguo: Recordatorio, recordatorioNuevo: Recordatorio) {
        val index = recordatorios.indexOf(recordatorioAntiguo)
        if (index != -1) {
            recordatorios[index] = recordatorioNuevo
        }
    }
}
