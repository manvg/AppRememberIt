package com.example.apprememberit.ViewModel

data class Usuario(
    val nombre: String = "",
    val email: String = "",
    val contrasena: String = ""
){
    // Constructor vacío requerido por Firebase
    constructor() : this("", "", "")
}
