package mx.itson.pockettracker.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import mx.itson.pockettracker.R
import mx.itson.pockettracker.entities.Gasto

class GastoAdapter(
    context: Context,
    gastos: List<Gasto>
) : BaseAdapter() {

    var context: Context = context
    var gastos: List<Gasto> = gastos
    var gastosList: List<Gasto> = gastos

    override fun getCount(): Int {
        return gastosList.size
    }

    override fun getItem(position: Int): Any {
        return gastosList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val element = LayoutInflater.from(context).inflate(R.layout.gasto_item, null)

        try {
            val gasto = getItem(position) as Gasto

            val txtProducto: TextView = element.findViewById(R.id.gasto_producto)
            txtProducto.text = gasto.producto

            val txtPrecio: TextView = element.findViewById(R.id.gasto_precio)
            txtPrecio.text = "$${gasto.precio}"

            val txtTienda: TextView = element.findViewById(R.id.gasto_tienda)
            txtTienda.text = gasto.tienda

            val txtFecha: TextView = element.findViewById(R.id.gasto_fecha)
            txtFecha.text = gasto.fecha

        } catch (ex: Exception) {
            Log.e("Error showing gasto", ex.message.toString())
        }

        return element
    }
}