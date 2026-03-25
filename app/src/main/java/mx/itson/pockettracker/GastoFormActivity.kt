package mx.itson.pockettracker

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.os.Build
import android.os.Build.VERSION
import android.os.Vibrator
import android.os.VibratorManager
import android.os.VibrationEffect
import mx.itson.pockettracker.entities.Gasto
import java.util.Calendar

/**
 * Actividad que presenta el formulario para registrar un nuevo gasto en la base de datos.
 * Incluye lógica de selección de fecha mediante [DatePickerDialog], validación de campos,
 * manejo de vibraciones táctiles y persistencia de datos.
 */
class GastoFormActivity : AppCompatActivity() {
    private lateinit var etConcepto: EditText
    private lateinit var etMonto: EditText
    private lateinit var etFecha: EditText
    private lateinit var etCategoria: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnSeleccionarFecha: Button

    /**
     * Inicializa la vista del formulario y vincula los componentes de la interfaz XML.
     * Configura los eventos de clic para la selección de fecha y el guardado de la información.
     *
     * @param savedInstanceState El estado previo guardado si la actividad fue reiniciada.
     */
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gasto_form)

        etConcepto= findViewById(R.id.etConcepto)
        etMonto= findViewById(R.id.etMonto)
        etFecha= findViewById(R.id.etFecha)
        etCategoria= findViewById(R.id.etCategoria)
        btnGuardar= findViewById(R.id.bntGuardar)
        btnSeleccionarFecha= findViewById(R.id.btnSeleccionarFecha)

        // Abre el selector de fecha al presionar el botón correspondiente
        btnSeleccionarFecha.setOnClickListener {
            mostrarDatePicker()
        }

        // Ejecuta la validación y guardado al presionar el botón Guardar
        btnGuardar.setOnClickListener {
            ValidarYGuardar()
        }
    }

    /**
     * Muestra un diálogo de selección de fecha ([DatePickerDialog]).
     * Al seleccionar una fecha, esta se formatea como DD/MM/AAAA y se asigna al campo de texto.
     */
    private fun mostrarDatePicker(){
        val calendar = Calendar.getInstance()
        val anio = calendar.get(Calendar.YEAR)
        val mes = calendar.get(Calendar.MONTH)
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        // Configuración y visualización del selector de fecha
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            // Formateo de la fecha (mes + 1 debido a que Calendar maneja meses de 0 a 11)
            val fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
            etFecha.setText(fechaSeleccionada)
        }, anio, mes, dia)

        datePickerDialog.show()
    }

    /**
     * Valida la información ingresada por el usuario en los campos del formulario.
     * Si los datos son válidos, utiliza el modelo [Gasto] para persistir la información en la base de datos.
     * Emite una alerta táctil y visual si el monto supera los $1000.
     */
    private fun ValidarYGuardar() {
        val concepto = etConcepto.text.toString().trim()
        val montoStr = etMonto.text.toString().trim()
        val fecha = etFecha.text.toString().trim()
        val categoria = etCategoria.text.toString().trim()

        // Verificación de campos obligatorios
        if (concepto.isEmpty() || montoStr.isEmpty() || fecha.isEmpty() || categoria.isEmpty()) {
            vibrar(TipoVibracion.ERROR_CAMPOS)
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show()
            return
        }

        val monto = montoStr.toDoubleOrNull() ?: 0.0
        
        // Alerta preventiva por gasto elevado
        if (monto > 1000) {
            vibrar(TipoVibracion.ALERTA_PRECIO_ALTO)
            Toast.makeText(this, getString(R.string.alerta_gasto_elevado), Toast.LENGTH_SHORT).show()
        }

        // Llamada al método de persistencia en la base de datos
        val resultado = Gasto().save(this, concepto, monto, categoria, fecha)

        if (resultado) {
            vibrar(TipoVibracion.EXITO)
            Toast.makeText(this, getString(R.string.exito_guardado), Toast.LENGTH_SHORT).show()
            finish() // Cierra la actividad tras el guardado exitoso
        } else {
            Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Ejecuta una respuesta táctil (vibración) basada en el tipo de evento ocurrido.
     * Gestiona la compatibilidad entre versiones modernas (SDK 31+) y antiguas de Android.
     * 
     * @param tipo El tipo de vibración a ejecutar (EXITO, ERROR_CAMPOS, ALERTA_PRECIO_ALTO).
     */
    private fun vibrar(tipo: TipoVibracion) {
        val vibrator = if (VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("Deprecation")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        // Patrón de vibración definido como {Retraso, Duración, Espera, Duración...}
        val patron = when (tipo) {
            TipoVibracion.EXITO -> longArrayOf(0, 100)
            TipoVibracion.ERROR_CAMPOS -> longArrayOf(0, 200, 100, 200)
            TipoVibracion.ALERTA_PRECIO_ALTO -> longArrayOf(0, 500)
        }

        if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(patron, -1))
        } else {
            @Suppress("Deprecation")
            vibrator.vibrate(patron, -1)
        }
    }

    /**
     * Enumeración que define los tipos de eventos que activan una respuesta táctil específica.
     */
    enum class TipoVibracion { EXITO, ERROR_CAMPOS, ALERTA_PRECIO_ALTO }
}