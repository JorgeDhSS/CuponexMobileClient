package com.example.cuponexmobileclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import com.example.cuponexmobileclient.pokos.Categoria
import com.example.cuponexmobileclient.pokos.Promocion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion

class PromocionesActivity : AppCompatActivity() {
    var listaPromos = ArrayList<Promocion>()
    var recyclerview: RecyclerView? = null
    private lateinit var editDatos : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promociones)

        descargarPromociones(intent.getIntExtra("idCategoria", 0))
        recyclerview = findViewById<RecyclerView>(R.id.recyclerviewPromos)

        editDatos = findViewById(R.id.button3)
        editDatos.setOnClickListener {
            mostrarAlerta("Redirigiendo")
            val irEdit = Intent(this@PromocionesActivity, EditActivity::class.java)
            startActivity(irEdit)
            finish()
        }
    }
    private fun descargarPromociones(idCategoria: Int)
    {
        if(idCategoria != 0) {
            Ion.with(this@PromocionesActivity)
                .load("GET", Utils.URL + "promocion/byCategoria/${idCategoria}")
                .asString()
                .setCallback { e, result ->
                    if (e != null) {
                        mostrarAlerta(e.message!!);
                    } else {
                        convertirJson(result)
                    }
                }
        }
    }
    private fun convertirJson(respuestaJson : String)
    {
        val gson = Gson()
        val typePromo = object : TypeToken<ArrayList<Promocion>>(){}.type
        mostrarAlerta(respuestaJson+"OO")
        listaPromos = gson.fromJson(respuestaJson, typePromo)
        cargarPromos()
    }
    private fun cargarPromos()
    {
        val adaptador = PromocionesAdapter()
        adaptador.context = this@PromocionesActivity
        adaptador.promociones = listaPromos
        recyclerview?.layoutManager = LinearLayoutManager(this)
        recyclerview?.adapter = adaptador
        mostrarAlerta("Promociones cargadas")

    }
    private fun mostrarAlerta(mensaje: String)
    {
        Toast.makeText(this@PromocionesActivity, mensaje, Toast.LENGTH_LONG).show()
    }
}