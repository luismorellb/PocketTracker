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

/**
 * Adaptador personalizado para la visualización de objetos [Gasto] en una lista ([ListView]).
 * Este adaptador se encarga de convertir la lista de datos en elementos visuales individuales.
 *
 * @param context El contexto de la aplicación para inflar el diseño XML.
 * @param gastos La lista de gastos que se desea mostrar.
 */
class GastoAdapter(
    context: Context,
    gastos: List<Gasto>
) : BaseAdapter() {

    private var context: Context = context
    private var gastosList: List<Gasto> = gastos

    /**
     * Obtiene el número total de gastos en la lista.
     * @return [Int] El tamaño total de la colección de datos.
     */
    override fun getCount(): Int {
        return gastosList.size
    }

    /**
     * Devuelve el objeto [Gasto] en una posición específica.
     * @param position El índice del elemento en la lista.
     * @return El objeto de tipo [Gasto].
     */
    override fun getItem(position: Int): Any {
        return gastosList[position]
    }

    /**
     * Obtiene el ID del elemento en la posición dada. En este caso no se utiliza un ID específico.
     * @param position El índice del elemento.
     * @return [Long] Siempre retorna 0.
     */
    override fun getItemId(position: Int): Long {
        return 0
    }

    /**
     * Genera la vista para cada fila de la lista de gastos.
     * Infla el diseño XML individual y lo llena con los datos del objeto [Gasto] correspondiente.
     *
     * @param position La posición del elemento en la lista.
     * @param convertView La vista reutilizable que el sistema Android puede proporcionar.
     * @param parent El padre al que eventualmente se adjuntará esta vista.
     * @return [View] La vista del elemento de la lista configurada.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Se infla el diseño de item personalizado para cada gasto
        val element = LayoutInflater.from(context).inflate(R.layout.gasto_item, null)

        try {
            val gasto = getItem(position) as Gasto

            // Se vinculan los datos del gasto con los elementos del diseño XML
            val txtProducto: TextView = element.findViewById(R.id.gasto_producto)
            txtProducto.text = gasto.producto

            val txtPrecio: TextView = element.findViewById(R.id.gasto_precio)
            txtPrecio.text = "$${gasto.precio}"

            val txtTienda: TextView = element.findViewById(R.id.gasto_tienda)
            txtTienda.text = gasto.tienda

            val txtFecha: TextView = element.findViewById(R.id.gasto_fecha)
            txtFecha.text = gasto.fecha

        } catch (ex: Exception) {
            Log.e("GastoAdapter.getView", "Error mostrando el gasto en la lista: ${ex.message}")
        }

        return element
    }
}