package mx.itson.pockettracker.entities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import mx.itson.pockettracker.persistence.GastosDB

class Gasto {

    var id = 0
    var producto: String = ""
    var precio: Double = 0.0
    var tienda: String = ""
    var fecha: String = ""

    constructor()

    constructor(id: Int, producto: String, precio: Double, tienda: String, fecha: String){
        this.id = id
        this.producto = producto
        this.precio = precio
        this.tienda = tienda
        this.fecha = fecha
    }

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
            Log.e("Error al guardar el gasto", ex.message.toString())
            false
        }

    }

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
            Log.e("Error al obtener los gastos", ex.message.toString())
        }
        return gastos
    }

}