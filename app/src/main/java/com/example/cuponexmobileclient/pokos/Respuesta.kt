package com.example.cuponexmobileclient.pokos

class Respuesta {
    var error:Boolean = false
    lateinit var mensaje:String
    var user: UserApp? = null
    public fun Respuesta(error:Boolean, mensaje:String, user:UserApp) {
        this.error = error;
        this.mensaje = mensaje;
        this.user = user;
    }

    public fun Respuesta() {
    }
}