package mx.itson.pockettracker.persistence

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Clase encargada de gestionar la creación y actualización de la base de datos local SQLite.
 * Hereda de [SQLiteOpenHelper] para facilitar el manejo del ciclo de vida de la base de datos.
 *
 * @param context El contexto de la aplicación.
 * @param name Nombre del archivo de la base de datos.
 * @param factory Fábrica opcional para crear cursores.
 * @param version Número de versión de la base de datos.
 */
class GastosDB(context: Context, name : String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    /**
     * Se ejecuta cuando la base de datos es creada por primera vez.
     * Aquí se definen las tablas del esquema.
     *
     * @param db La base de datos que se está creando.
     */
    override fun onCreate(db: SQLiteDatabase) {
        try{
            // Ejecución de la sentencia SQL para crear la tabla 'Gasto'
            db.execSQL(
                "CREATE TABLE Gasto(id INTEGER PRIMARY KEY AUTOINCREMENT, producto TEXT, precio REAL, tienda TEXT, fecha TEXT)"
            )
        } catch (ex: Exception){
            Log.e("GastosDB.onCreate", "Error al crear la tabla: ${ex.message}")
        }
    }

    /**
     * Se ejecuta cuando es necesario actualizar la estructura de la base de datos (versión superior).
     *
     * @param db La base de datos.
     * @param oldVersion La versión anterior.
     * @param newVersion La versión nueva.
     */
    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Implementación futura para migraciones de esquema
    }
}