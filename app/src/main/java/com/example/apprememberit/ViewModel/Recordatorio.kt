package com.example.apprememberit.ViewModel

data class Recordatorio(
    val id: String? = null,  // Agrega el campo id
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val hora: String,
    val emailUsuario: String
) {
    constructor() : this(null,"", "", "", "", "")
}
