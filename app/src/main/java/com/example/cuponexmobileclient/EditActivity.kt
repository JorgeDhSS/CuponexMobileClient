package com.example.cuponexmobileclient

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.widget.*
import com.example.cuponexmobileclient.pokos.Respuesta
import com.example.cuponexmobileclient.pokos.UserApp
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class EditActivity : AppCompatActivity() {
    var user : UserApp? = null
    lateinit var nombre : EditText
    lateinit var apellidoP : EditText
    lateinit var apellidoM : EditText
    lateinit var telefono : EditText
    lateinit var calle : EditText
    lateinit var numero : EditText
    lateinit var fechaNac : EditText
    lateinit var password : EditText
    private lateinit var btnSendData : Button
    lateinit var imgFoto : ImageView
    lateinit var statusPhoto: TextView
    lateinit var buttonPhoto : Button
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        nombre = findViewById(R.id.etNombreEdit)
        apellidoP = findViewById(R.id.etApellidoPEdit)
        apellidoM = findViewById(R.id.etApellidoMEdit)
        telefono = findViewById(R.id.etPhoneEdit)
        calle = findViewById(R.id.etCalleEdit)
        numero = findViewById(R.id.etNumEdit)
        fechaNac = findViewById(R.id.etbirthDateEdit)
        password = findViewById(R.id.etPasswordEdit)
        imgFoto = findViewById(R.id.imageViewUser)
        statusPhoto = findViewById(R.id.statusPhoto)
        descargarDatos()
        btnSendData = findViewById(R.id.btnSendEdit)
        btnSendData.setOnClickListener {
            enviarDatos()
        }

        descargarFoto()
        buttonPhoto = findViewById(R.id.takePhoto);
        buttonPhoto.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imgFoto.setImageURI(imageUri)
            var cr : ContentResolver = this.getContentResolver();
            try {
                var bitmap : Bitmap  = BitmapFactory.decodeStream(cr.openInputStream(imageUri!!))
                val stream = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArray: ByteArray = stream.toByteArray()
                bitmap.recycle()
                enviarFoto(byteArray)
            } catch (e : FileNotFoundException) {
                mostrarAlerta("Error al obtener la imagen de la galeria")
            }
        }
    }
    private fun enviarFoto(img : ByteArray)
    {
        Ion.with(this@EditActivity).load("POST", Utils.URL.toString()+"user/subirFoto/${Utils.idUser}")
            .setHeader("Content-Type","application/octet-stream")
            .setByteArrayBody(img)
            .asString()
            .setCallback { e, result ->
                if(e != null)
                {
                    e.printStackTrace()
                    mostrarAlerta("Error en la conexión, intente de nuevo")
                }
                else
                {
                    validarFoto(result)
                }
            }
    }
    private fun enviarDatos()
    {
        Ion.with(this@EditActivity).load("POST", Utils.URL.toString()+"user/update")
            .setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("nombre", nombre.text.toString())
            .setBodyParameter("apellidoP", apellidoP.text.toString())
            .setBodyParameter("apellidoM", apellidoM.text.toString())
            .setBodyParameter("tel", telefono.text.toString())
            .setBodyParameter("password", password.text.toString())
            .setBodyParameter("calle", calle.text.toString())
            .setBodyParameter("numero", numero.text.toString())
            .setBodyParameter("fechaNac", fechaNac.text.toString())
            .setBodyParameter("id", Utils.idUser.toString())
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
                val irCategorias = Intent(this@EditActivity, CategoriesActivity::class.java)
                startActivity(irCategorias)
                finish()
            }
            else
            {
                mostrarAlerta(respuestaWs.mensaje)
            }
        }
    }
    private fun validarFoto(respuesta : String)
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
                val reload = Intent(this@EditActivity, EditActivity::class.java)
                startActivity(reload)
                finish()
            }
            else
            {
                mostrarAlerta(respuestaWs.mensaje)
                print(respuestaWs.mensaje)
            }
        }
    }
    private fun descargarDatos()
    {
        Ion.with(this@EditActivity)
            .load("GET", Utils.URL+"user/byId/${Utils.idUser}")
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
        user = gson.fromJson(respuestaJson, UserApp::class.java)
        print(user.toString())
        cargarDatos()
    }
    private fun cargarDatos()
    {
        if(user != null)
        {
            nombre.setText(user!!.nombre)
            apellidoP.setText(user!!.apellidoP)
            apellidoM.setText(user!!.apellidoM)
            telefono.setText(user!!.tel)
            calle.setText(user!!.calle)
            numero.setText(user!!.numero.toString())
            fechaNac.setText(user!!.fechaNac)
            password.setText(user!!.password)
        }
        mostrarAlerta("Datos cargados")

    }
    private fun descargarFoto()
    {
        Ion.with(this@EditActivity)
            .load("GET", Utils.URL+"user/getPhoto/${Utils.idUser}")
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
        val gson = Gson();
        val infoFoto = gson.fromJson(informacion, UserApp::class.java)
        try
        {
            val imgByte = Base64.decode(infoFoto.foto, Base64.DEFAULT)
            val imgBitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
            imgFoto.setImageBitmap(imgBitmap)
            statusPhoto.text = "Esta es tu foto actual, puedes cambiarla"
        }
        catch (e : java.lang.NullPointerException)
        {
            statusPhoto.text = "Agrega una foto"
        }


    }
    private fun mostrarAlerta(mensaje: String)
    {
        Toast.makeText(this@EditActivity, mensaje, Toast.LENGTH_LONG).show()
    }
}