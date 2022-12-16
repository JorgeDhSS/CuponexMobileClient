package com.example.cuponexmobileclient

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.cuponexmobileclient.pokos.Promocion

class PromocionesAdapter: RecyclerView.Adapter<PromocionesAdapter.ViewHolder>() {
    lateinit var promociones : ArrayList<Promocion>
    var context: Context? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.card_layout_promociones, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.nombre.text = promociones.get(p1).nombre
        p0.vigencia.text = promociones.get(p1).fechaIni+"-"+promociones.get(p1).fechaFin
        p0.empresa.text = promociones.get(p1).empresa
        if(promociones.get(p1).tipo == 0)
        {
            p0.tipo.text = "Descuento"
        }
        else
        {
            p0.tipo.text = "Costo rebajado"
        }

        p0.button.setOnClickListener {
            val irPromociones = Intent(context, PromocionDetailActivity::class.java).apply {
                putExtra("idPromocion",promociones.get(p1).id)
            }
            context?.startActivity(irPromociones)
        }
    }

    override fun getItemCount(): Int {
        return promociones!!.count()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var button: Button
        var nombre : TextView
        var vigencia : TextView
        var empresa : TextView
        var tipo : TextView
        init {
            button = itemView.findViewById(R.id.btnPromocion)
            nombre = itemView.findViewById(R.id.name_promocion_card)
            vigencia = itemView.findViewById(R.id.vigencia_promocion_card)
            empresa = itemView.findViewById(R.id.empresa_promocion_card)
            tipo = itemView.findViewById(R.id.tipo_promocion_card)
        }
    }

}