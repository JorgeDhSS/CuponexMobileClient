package com.example.cuponexmobileclient

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.cuponexmobileclient.pokos.Promocion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion

class PromocionDetailActivity : AppCompatActivity() {
    var promo : Promocion? = null
    lateinit var txtNombre : TextView
    lateinit var txtDes : TextView
    lateinit var txtVig : TextView
    lateinit var txtRestri : TextView
    lateinit var txtTipo : TextView
    lateinit var txtValor : TextView
    lateinit var imgFoto : ImageView
    private lateinit var editDatos : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promocion_detail)

        txtNombre = findViewById(R.id.nombre_promo_detail)
        txtDes = findViewById(R.id.descrip_promo_detail)
        txtVig = findViewById(R.id.vigencia_promo_detail)
        txtRestri = findViewById(R.id.restri_promo_detail)
        txtTipo = findViewById(R.id.tipo_promo_detail)
        txtValor = findViewById(R.id.valor_promo_detail)
        imgFoto = findViewById(R.id.photo_promo_detail)
        descargarDetalles(intent.getIntExtra("idPromocion", 0))
        descargarFoto(intent.getIntExtra("idPromocion", 0))

        editDatos = findViewById(R.id.button3)
        editDatos.setOnClickListener {
            mostrarAlerta("Redirigiendo")
            val irEdit = Intent(this@PromocionDetailActivity, EditActivity::class.java)
            startActivity(irEdit)
            finish()
        }
    }
    private fun descargarDetalles(idPromo: Int)
    {
        if(idPromo != 0) {
            Ion.with(this@PromocionDetailActivity)
                .load("GET", Utils.URL + "promocion/details/"+idPromo)
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
        promo = gson.fromJson(respuestaJson, Promocion::class.java)
        cargarDatos()
    }
    private fun cargarDatos()
    {
        txtNombre.text = promo!!.nombre
        txtDes.text = promo!!.descripcion
        txtVig.text = promo!!.fechaIni+"-"+promo!!.fechaFin
        txtRestri.text = promo!!.restricciones
        if(promo!!.tipo == 0)
        {
            txtTipo.text = "Descuento"
            txtValor.text = promo!!.porcentaje.toString()
        }
        else
        {
            txtTipo.text = "Costo rebajado"
            txtValor.text = promo!!.costo.toString()
        }

    }
    private fun mostrarAlerta(mensaje: String)
    {
        Toast.makeText(this@PromocionDetailActivity, mensaje, Toast.LENGTH_LONG).show()
    }
    private fun descargarFoto(id : Int)
    {
        Ion.with(this@PromocionDetailActivity)
            .load("GET", Utils.URL+"promocion/getPhoto/${id}")
            .asString()
            .setCallback { e, result ->
                if(e!=null)
                {
                    mostrarAlerta(e.message!!);
                }
                else
                {
                    mostrarFoto(result)
                }
            }
    }
    private fun mostrarFoto(informacion : String)
    {
        print(informacion)
        val gson = Gson();
        val infoFoto = gson.fromJson(informacion, Promocion::class.java)
        val imgByte = Base64.decode(infoFoto.foto, Base64.DEFAULT)
        val imgBitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
        imgFoto.setImageBitmap(imgBitmap)
    }
}