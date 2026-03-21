package mx.itson.pockettracker.persistence

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import javax.net.SocketFactory

class GastosDB(context: Context, name : String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        try{
            db.execSQL(
                "CREATE TABLE Gasto(id INTEGER PRIMARY KEY AUTOINCREMENT, producto TEXT, precio REAL, tienda TEXT, fecha TEXT)"
            )
        } catch (ex: Exception){
            Log.e("Error al crear la base de datos", ex.message.toString())
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }
}