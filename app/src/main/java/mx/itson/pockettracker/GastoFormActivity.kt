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

class GastoFormActivity : AppCompatActivity() {
    private lateinit var etConcepto: EditText
    private lateinit var etMonto: EditText
    private lateinit var etFecha: EditText
    private lateinit var etCategoria: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnSeleccionarFecha: Button



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gasto_form)

        etConcepto= findViewById(R.id.etConcepto)
        etMonto= findViewById(R.id.etMonto)
        etFecha= findViewById(R.id.etFecha)
        etCategoria= findViewById(R.id.etCategoria)
        btnGuardar= findViewById(R.id.bntGuardar)
        btnSeleccionarFecha= findViewById(R.id.btnSeleccionarFecha)

        btnSeleccionarFecha.setOnClickListener {
            mostrarDatePicker()
        }

        btnGuardar.setOnClickListener {
            ValidarYGuardar()
        }
    }

    private fun mostrarDatePicker(){
        val calendar = Calendar.getInstance()
        val anio = calendar.get(Calendar.YEAR)
        val mes = calendar.get(Calendar.MONTH)
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
            etFecha.setText(fechaSeleccionada)
        }, anio, mes, dia)

        datePickerDialog.show()


    }
    private fun ValidarYGuardar() {
        val concepto = etConcepto.text.toString().trim()
        val montoStr = etMonto.text.toString().trim()
        val fecha = etFecha.text.toString().trim()
        val categoria = etCategoria.text.toString().trim()

        if (concepto.isEmpty() || montoStr.isEmpty() || fecha.isEmpty() || categoria.isEmpty()) {
            vibrar(TipoVibracion.ERROR_CAMPOS)
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show()
            return
        }

        val monto = montoStr.toDoubleOrNull() ?: 0.0
        if (monto > 1000) {
            vibrar(TipoVibracion.ALERTA_PRECIO_ALTO)
            Toast.makeText(this, getString(R.string.alerta_gasto_elevado), Toast.LENGTH_SHORT).show()
        }

        val resultado = Gasto().save(this, concepto, monto, categoria, fecha)

        if (resultado) {
            vibrar(TipoVibracion.EXITO)
            Toast.makeText(this, getString(R.string.exito_guardado), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun vibrar(tipo: TipoVibracion) {
        val vibrator = if (VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("Deprecation")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
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

    enum class TipoVibracion { EXITO, ERROR_CAMPOS, ALERTA_PRECIO_ALTO }
}
