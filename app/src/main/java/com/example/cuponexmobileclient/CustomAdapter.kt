package com.example.cuponexmobileclient

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.cuponexmobileclient.pokos.Categoria


class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    lateinit var titles : ArrayList<Categoria>
    var context: Context? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.card_layout, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemCategoria.text = titles.get(p1).nombre
        p0.itemImageButton.setOnClickListener {
            val irPromociones = Intent(context, PromocionesActivity::class.java).apply {
                putExtra("idCategoria",titles.get(p1).id)
            }

            context?.startActivity(irPromociones)
        }
    }

    override fun getItemCount(): Int {
        return titles!!.count()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var itemImageButton: ImageButton
        var itemCategoria : TextView
        init {
            itemImageButton = itemView.findViewById(R.id.image_view_card)
            itemCategoria = itemView.findViewById(R.id.id_textCategory)
        }
    }

}