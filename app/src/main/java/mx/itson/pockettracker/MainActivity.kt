package mx.itson.pockettracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Actividad principal que sirve como punto de entrada de la aplicación.
 * Proporciona una interfaz con botones para navegar hacia el formulario de registro
 * de gastos o hacia la lista de gastos guardados.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Se ejecuta al iniciar la actividad. Configura la vista principal,
     * habilita el diseño edge-to-edge y establece los listeners de los botones.
     * 
     * @param savedInstanceState Estado guardado de la actividad, si existe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // Ajuste de padding para las barras del sistema (edge-to-edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegistrarGasto = findViewById<Button>(R.id.btnRegistrarGasto)
        val btnVerLista = findViewById<Button>(R.id.btnVerLista)

        // Navega a GastoFormActivity para agregar un nuevo registro
        btnRegistrarGasto.setOnClickListener {
            val intent = Intent(this, GastoFormActivity::class.java)
            startActivity(intent)
        }

        // Navega a GastoListActivity para visualizar los registros existentes
        btnVerLista.setOnClickListener {
            val intent = Intent(this, GastoListActivity::class.java)
            startActivity(intent)
        }
    }
}