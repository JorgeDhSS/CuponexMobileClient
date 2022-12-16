package com.example.cuponexmobileclient

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.example.cuponexmobileclient.pokos.Categoria
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion


class CategoriesActivity : AppCompatActivity() {
    var listaCategorias = ArrayList<Categoria>()
    var recyclerview: RecyclerView? = null
    private lateinit var editDatos : Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        descargarCategorias()
        recyclerview = findViewById<RecyclerView>(R.id.recyclerviewCategories)
        editDatos = findViewById(R.id.button3)
        editDatos.setOnClickListener {
            mostrarAlerta("Redirigiendo")
            val irEdit = Intent(this@CategoriesActivity, EditActivity::class.java)
            startActivity(irEdit)
            finish()
        }
    }
    private fun descargarCategorias()
    {
        Ion.with(this@CategoriesActivity)
            .load("GET", Utils.URL+"categories")
            .asString()
            .setCallback { e, result ->
                if(e!=null)
                {
                    mostrarAlerta(e.message!!);
                }
                else
                {
                    convertirJson(result)
                }
            }
    }
    private fun convertirJson(respuestaJson : String)
    {
        val gson = Gson()
        val typeCat = object : TypeToken<ArrayList<Categoria>>(){}.type
        listaCategorias = gson.fromJson(respuestaJson, typeCat)
        cargarCategorias()
    }
    private fun cargarCategorias()
    {
        val adaptador = CustomAdapter()
        adaptador.context = this@CategoriesActivity
        adaptador.titles = listaCategorias
        recyclerview?.layoutManager = LinearLayoutManager(this)
        recyclerview?.adapter = adaptador
        mostrarAlerta("categorias cargadas")

    }
    private fun mostrarAlerta(mensaje: String)
    {
        Toast.makeText(this@CategoriesActivity, mensaje, Toast.LENGTH_LONG).show()
    }
}