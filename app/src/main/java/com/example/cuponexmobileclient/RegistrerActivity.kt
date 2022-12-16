package com.example.cuponexmobileclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cuponexmobileclient.pokos.Respuesta
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

class RegistrerActivity : AppCompatActivity() {
    lateinit var nombre : EditText
    lateinit var apellidoP : EditText
    lateinit var apellidoM : EditText
    lateinit var telefono : EditText
    lateinit var correo : EditText
    lateinit var calle : EditText
    lateinit var numero : EditText
    lateinit var fechaNac : EditText
    lateinit var password : EditText
    private lateinit var btnSendData : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrer)
        nombre = findViewById(R.id.etNombreReg)
        apellidoP = findViewById(R.id.etApellidoPReg)
        apellidoM = findViewById(R.id.etApellidoMReg)
        telefono = findViewById(R.id.etPhoneReg)
        correo = findViewById(R.id.etMailReg)
        calle = findViewById(R.id.etCalleReg)
        numero = findViewById(R.id.etNumReg)
        fechaNac = findViewById(R.id.etbirthDateReg)
        password = findViewById(R.id.etPasswordReg)
        btnSendData = findViewById(R.id.btnSendReg)
        btnSendData.setOnClickListener {
            enviarDatos()
        }
    }
    private fun enviarDatos()
    {
        Ion.with(this@RegistrerActivity).load("POST", Utils.URL.toString()+"user/create")
            .setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("nombre", nombre.text.toString())
            .setBodyParameter("apellidoP", apellidoP.text.toString())
            .setBodyParameter("apellidoM", apellidoM.text.toString())
            .setBodyParameter("tel", telefono.text.toString())
            .setBodyParameter("correo", correo.text.toString())
            .setBodyParameter("password", password.text.toString())
            .setBodyParameter("calle", calle.text.toString())
            .setBodyParameter("numero", numero.text.toString())
            .setBodyParameter("fechaNac", fechaNac.text.toString())
            .asString()
            .setCallback { e, result ->
                if(e != null)
                {
                    e.printStackTrace()
                    mostrarAlerta("Error en la conexión, intente de nuevo")
                }
                else
                {
                    validarResultadosPeticiones(result)
                }
            }
    }

    private fun validarResultadosPeticiones(respuesta : String)
    {
        if(respuesta == null || respuesta.isEmpty())
        {
            mostrarAlerta("Por el momento no hay servicio disponible")
        }
        else
        {
            val gson = Gson();
            val respuestaWs = gson.fromJson(respuesta, Respuesta::class.java);
            if(!respuestaWs.error !!) //forzar a obtener un valor aun cuanod haya un null pointer exception
            {
                mostrarAlerta(respuestaWs.mensaje)
                val irLogin = Intent(this@RegistrerActivity, LoginActivity::class.java)
                startActivity(irLogin)
                finish()
            }
            else
            {
                mostrarAlerta(respuestaWs.mensaje)
            }
        }
    }

    private fun mostrarAlerta(mensaje: String)
    {
        Toast.makeText(this@RegistrerActivity, mensaje, Toast.LENGTH_LONG).show()
    }
}