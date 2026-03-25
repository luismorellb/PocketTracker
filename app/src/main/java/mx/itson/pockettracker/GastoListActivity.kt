package mx.itson.pockettracker

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.itson.pockettracker.adapters.GastoAdapter
import mx.itson.pockettracker.entities.Gasto

/**
 * Actividad que visualiza el historial de gastos registrados.
 * Recupera los datos de la base de datos SQLite y muestra la suma total.
 */
class GastoListActivity : AppCompatActivity() {

    private var listGastos: ListView? = null
    private val context = this

    /**
     * Inicializa la interfaz y carga los datos desde la persistencia al crearse la actividad.
     * 
     * @param savedInstanceState Estado previo guardado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gasto_list)
        
        // Configuración de barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listGastos = findViewById(R.id.list_gastos)
        loadGastos()
    }

    /**
     * Obtiene todos los gastos del modelo [Gasto], los vincula al adaptador de la lista
     * y actualiza el encabezado con el monto total acumulado.
     */
    fun loadGastos() {
        // Recuperación de datos desde SQLite
        val gastos = Gasto().getAll(this)
        
        // Vinculación al adaptador del ListView
        listGastos?.adapter = GastoAdapter(context, gastos)

        // Cálculo dinámico de la suma total
        val total = gastos.sumOf { it.precio }
        
        // Actualización de la UI con el total formateado
        val tvTotal = findViewById<TextView>(R.id.tv_total)
        tvTotal.text = getString(R.string.label_total, "%.2f".format(total))
    }
}