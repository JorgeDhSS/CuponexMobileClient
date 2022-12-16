package com.example.cuponexmobileclient
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cuponexmobileclient.R.id.btnIngresar
import com.example.cuponexmobileclient.pokos.Respuesta
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnIniciarSesion : Button
    private lateinit var btnRegistro : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnIniciarSesion = findViewById(R.id.btnIngresar)
        etEmail = findViewById(R.id.emailLoginText)
        etPassword = findViewById(R.id.passwordLoginText)
        btnIniciarSesion.setOnClickListener {
            validarCampos()
        }
        btnRegistro = findViewById(R.id.btnRegistro)
        btnRegistro.setOnClickListener {
            mostrarAlerta("Redirigiendo al registro")
            val irRegistro = Intent(this@LoginActivity, RegistrerActivity::class.java)
            startActivity(irRegistro)
            finish()
        }
    }

    private fun validarCampos()
    {
        val usuarioTxt = etEmail.text.toString()
        val passwordTxt = etPassword.text.toString()
        var valido = true
        if(usuarioTxt.isEmpty())
        {
            valido = false
            etEmail.setError("Campo usuario requerido")
        }
        if(passwordTxt.isEmpty())
        {
            valido = false
            etPassword.setError("Campo contraseña requerido")
        }

        if (valido)
            enviarCredenciales(usuarioTxt, passwordTxt)
    }
    private fun enviarCredenciales(mail : String, password : String)
    {
        Ion.getDefault(this@LoginActivity).conscryptMiddleware.enable(false)
        //consumo
        Ion.with(this@LoginActivity).load("POST", Utils.URL.toString()+"login")
            .setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("correo", mail)
            .setBodyParameter("password", password)
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
                Utils.idUser = respuestaWs.user!!.id!!
                val irPrincipal = Intent(this@LoginActivity, CategoriesActivity::class.java)
                startActivity(irPrincipal)
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
        Toast.makeText(this@LoginActivity, mensaje,Toast.LENGTH_LONG).show()
    }
}