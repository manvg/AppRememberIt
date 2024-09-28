package com.example.apprememberit.ViewModel

data class UsuarioSesion (
    var nombre: String = "",
    var email: String = "",
    var contrasena: String = "",
    var isActive: Boolean = false
){
    constructor() : this("", "", "", false)
}