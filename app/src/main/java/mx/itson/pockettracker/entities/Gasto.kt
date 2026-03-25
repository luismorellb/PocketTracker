package mx.itson.pockettracker.entities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import mx.itson.pockettracker.persistence.GastosDB

/**
 * Entidad que representa un registro de gasto en la aplicación.
 * Proporciona métodos para la persistencia de datos utilizando SQLite.
 */
class Gasto {

    /** Identificador único del registro en la base de datos */
    var id = 0
    /** Nombre o descripción del producto o servicio adquirido */
    var producto: String = ""
    /** Monto monetario del gasto realizado */
    var precio: Double = 0.0
    /** Nombre del establecimiento o categoría del gasto (mapeado a 'tienda' en la base de datos) */
    var tienda: String = ""
    /** Fecha en que se realizó el gasto (formato sugerido: DD/MM/AAAA) */
    var fecha: String = ""

    /**
     * Constructor vacío para inicialización básica.
     */
    constructor()

    /**
     * Constructor con parámetros para crear una instancia completa de un Gasto.
     * 
     * @param id Identificador del registro.
     * @param producto Descripción del gasto.
     * @param precio Costo del gasto.
     * @param tienda Lugar o categoría.
     * @param fecha Fecha del gasto.
     */
    constructor(id: Int, producto: String, precio: Double, tienda: String, fecha: String){
        this.id = id
        this.producto = producto
        this.precio = precio
        this.tienda = tienda
        this.fecha = fecha
    }

    /**
     * Guarda un registro de gasto en la base de datos local.
     * 
     * @param context Contexto de la aplicación para acceder al helper de la base de datos.
     * @param producto Concepto del gasto a guardar.
     * @param precio Monto del gasto a guardar.
     * @param tienda Establecimiento o categoría a guardar.
     * @param fecha Fecha del registro a guardar.
     * @return [Boolean] true si el registro se insertó correctamente, false en caso de error.
     */
    fun save(context: Context, producto: String, precio: Double, tienda: String, fecha: String): Boolean {
        return try {
            val gastosDB = GastosDB(context, "pockettracker.db", null, 1)
            val db: SQLiteDatabase = gastosDB.writableDatabase

            val values = ContentValues()
            values.put("producto", producto)
            values.put("precio", precio)
            values.put("tienda", tienda)
            values.put("fecha", fecha)

            val result = db.insert("Gasto", null, values)
            db.close()
            result != -1L

        } catch (ex: Exception) {
            Log.e("Gasto.save", "Error al guardar el gasto: ${ex.message}")
            false
        }
    }

    /**
     * Obtiene todos los registros de gastos almacenados en la base de datos.
     * 
     * @param context Contexto de la aplicación para acceder a la base de datos.
     * @return [List] Una lista de objetos [Gasto] con la información recuperada.
     */
    fun getAll(context: Context): List<Gasto> {
        val gastos: MutableList<Gasto> = ArrayList()
        try{
            val gastosDB = GastosDB(context, "pockettracker.db", null, 1)
            val db: SQLiteDatabase = gastosDB.readableDatabase
            val resultSet = db.rawQuery("SELECT id, producto, precio, tienda, fecha FROM Gasto", null)

            while(resultSet.moveToNext()) {
                val gasto = Gasto(
                    resultSet.getInt(0),
                    resultSet.getString(1),
                    resultSet.getDouble(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
                )
                gastos.add(gasto)
            }
            db.close()

        } catch (ex: Exception) {
            Log.e("Gasto.getAll", "Error al obtener los gastos: ${ex.message}")
        }
        return gastos
    }
}