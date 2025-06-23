package com.desofcc.appsqlrepaso

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Colocamos la conexcion a la BD y creamos la tabla.
class MiSQLiteHelper (context: Context):SQLiteOpenHelper(context,"comentarios.db",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        //CREAMOS LA TBL
        val ordenCreacion = """
            CREATE TABLE IF NOT EXISTS tbl_comentarios(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tema         TEXT,
                descripcion  TEXT
            
            ) 
        """.trimIndent()

        db!!.execSQL(ordenCreacion)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val ordenBorrar = "DROP TABLE IF EXISTS tbl_comentarios"
        db!!.execSQL(ordenBorrar)
        onCreate(db)
    }
    //=================================================================================== 19/06/2025
    //OPERACIONES_____________>>>

    //AGREGAR
    fun agregarDatos(tema: String, descripcion: String) {
        val datos = ContentValues()
        datos.put("tema", tema)
        datos.put("descripcion", descripcion)

        //Guardamos los datos con un writableDatabase
        val db = this.writableDatabase
        db.insert("tbl_comentarios", null, datos)
        db.close()

    }

    //=================================================================================== 19/06/2025
    //ELIMINAR
    fun borrarDatos(id: Int): Int {
        //Creamos un Arreglo para recibir un entero
        val arreglo = arrayOf(id.toString())
        val db = this.writableDatabase
        val borrados = db.delete("tbl_comentarios", "id=?", arreglo)
        //Manera 2 de poder eliminar:
        // db.execSQL("delete from tbl_comentarios where id=?,arreglo)

        db.close()
        return borrados
    }

    //=================================================================================== 19/06/2025
    //MODIFICAR
    fun modificarDatos(id: Int, tema: String, descripcion: String) {
        //Creamos un arreglo para recibir un entero
        val arreglo = arrayOf(id.toString())
        val datos = ContentValues()
        datos.put("tema", tema)
        datos.put("descripcion", descripcion)


    val db = this.writableDatabase
    db.update("tbl_comentarios",datos,"id=?",arreglo)
    db.close()

    }

    //=================================================================================== 19/06/2025
    //BUSCAR
    fun obtenerComentarioPorId(id: Int): Cursor{
        val db = this.readableDatabase
        return db.rawQuery("SELECT tema, descripcion FROM tbl_comentarios WHERE id=?", arrayOf(id.toString()))
    }

    // AL colocar en el consulta tema y descripcion se toman los indices desde el 0 en adelante
    // donde tema pasa a ser el 0 y descripcion el 1.
    // asi se los llamara en el RESPUESTA BTN BUSCAR  en el MainAcivity.

    //=================================================================================== 20/06/2025
    //LA FUNCION CONSULTAR ESTA EN EL MainActivity.kt

}//FIN de la clase principal














