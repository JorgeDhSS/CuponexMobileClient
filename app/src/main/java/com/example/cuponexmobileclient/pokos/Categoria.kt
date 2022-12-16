package com.example.cuponexmobileclient.pokos

class Categoria {
    var id:Int = 0
    lateinit var nombre:String

    public fun Categoria(id:Int, nombre:String) {
        this.id = id;
        this.nombre = nombre;
    }

    public fun Categoria() {
    }
}