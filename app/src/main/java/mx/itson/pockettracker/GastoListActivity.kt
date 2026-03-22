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

class GastoListActivity : AppCompatActivity() {

    var listGastos: ListView? = null
    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gasto_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listGastos = findViewById(R.id.list_gastos)
        loadGastos()
    }

    fun loadGastos() {
        val gastos = Gasto().getAll(this)
        listGastos?.adapter = GastoAdapter(context, gastos)

        val total = gastos.sumOf { it.precio }
        val tvTotal = findViewById<TextView>(R.id.tv_total)
        tvTotal.text = "Total: $${"%.2f".format(total)}"
    }
}