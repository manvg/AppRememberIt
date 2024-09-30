package com.example.apprememberit.ViewModel

data class Usuario(
    val nombre: String = "",
    val email: String = "",
    val contrasena: String = ""
){
    constructor() : this("", "", "")
}
