package com.desofcc.appsqlrepaso

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteTransactionListener
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.desofcc.appsqlrepaso.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Paso 2
    //Colocamos las variables de la para la conxcion a la BD
    lateinit var binding: ActivityMainBinding
    lateinit var comentariosDBHelper: MiSQLiteHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        //Esta variables es mara econder los resultados de la BD
        var resultadoVisible = false

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

//=================================================================================== 19/06/2025
        //Colocamos la instancia de la BD cro
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Creamos el contexto de la BD
        comentariosDBHelper = MiSQLiteHelper(this)

//=================================================================================== 19/06/2025

    //PROGRAMAMOS LAS RESPUESTAS DE LOS BTNS

     //RESPUESTA BTN GUARDAR
     binding.btGuardar.setOnClickListener{
         if (binding.etTema.text.isNotBlank() &&
            binding.etDescripcion.text.isNotBlank()){

             comentariosDBHelper.agregarDatos(
                 binding.etTema.text.toString(),
                 binding.etDescripcion.text.toString()
             )

                //Limpiamos los campos de texto despues de guardar
                binding.etTema.text.clear()
                binding.etDescripcion.text.clear()

                //Mensaje emergente de registro exitoso
                Toast.makeText(this,"Comentario Guardado.",Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this,"Por favor complete los compos!!!",Toast.LENGTH_SHORT).show()
         }

     }// Fin del metodo guardar

//=================================================================================== 21/06/2025
        //RESPUESTA BTN CONSULTAR<
        binding.btConsultar.setOnClickListener {
            if (!resultadoVisible) {
                // Mostrar resultados
                binding.tvRespuesta.text = ""
                val db: SQLiteDatabase = comentariosDBHelper.readableDatabase
                val cursor = db.rawQuery("SELECT * FROM tbl_comentarios ORDER BY id DESC", null)
                if (cursor.moveToFirst()) {
                    do {
                        binding.tvRespuesta.append("ID:   ${cursor.getInt(0)}\n")
                        binding.tvRespuesta.append("Tema: ${cursor.getString(1)}\n")
                        binding.tvRespuesta.append("Descripción:\n\n  ⁜${cursor.getString(2)}\n")
                        binding.tvRespuesta.append("______________________________\n\n")
                    } while (cursor.moveToNext())
                }
                cursor.close()
                binding.btConsultar.text = "Ocultar"
                resultadoVisible = true
            } else {
                // Ocultar resultados
                binding.tvRespuesta.text = "Resultados . . ."
                binding.btConsultar.text = "Listado"
                resultadoVisible = false
            }
        }



//=================================================================================== 20/06/2025
    //RESPUESTA BTN MODIFICAR
    binding.btModificar.setOnClickListener {
        if (binding.etTema.text.isNotBlank() &&
            binding.etDescripcion.text.isNotBlank()){

            comentariosDBHelper.modificarDatos(binding.etId.text.toString().toInt(),
                binding.etTema.text.toString(),
                binding.etDescripcion.text.toString()
            )

            //Limpiamos los campos de texto despues de guardar
            binding.etTema.text.clear()
            binding.etDescripcion.text.clear()

            //Mensaje emergente de registro exitoso
            Toast.makeText(this,"Se modificado.",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se pudo modificar!!!", Toast.LENGTH_SHORT).show()
        }
    }

//=================================================================================== 21/06/2025
        //RESPUESTA BTN BUSCAR
        binding.btBuscarID.setOnClickListener {
            val idTexto = binding.etId.text.toString()
            if(idTexto.isNotBlank()){
                val cursor = comentariosDBHelper.obtenerComentarioPorId(idTexto.toInt())
                if(cursor.moveToFirst()){
                    //Retornamos los datos tema y descripcion, desde la consulta de Obtener por ID en el MiSQLiteHelper
                    val tema = cursor.getString(0)
                    val descripcion = cursor.getString(1)

                    binding.etTema.setText(tema)
                    binding.etDescripcion.setText(descripcion)

                    Toast.makeText(this,"Datos cargados para modificar!",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"No se encontró el ID", Toast.LENGTH_SHORT).show()
                }
                //Cerramos el cursor
                cursor.close()
            }else{
                Toast.makeText(this,"Ingrese su ID",Toast.LENGTH_SHORT).show()
            }
        }



//=================================================================================== 20/06/2025
    //RESPUESTA BTN ELIMINAR
    binding.btEliminar.setOnClickListener {
        var cantidad = 0
        if(binding.etId.text.isNotBlank()){
            cantidad = comentariosDBHelper.borrarDatos(binding.etId.text.toString().toInt())
            binding.etId.text.clear()
            Toast.makeText(this,"Tema eliminado.",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"No se elimino!!!",Toast.LENGTH_SHORT).show()
        }
    }

//=================================================================================== 21/06/2025
        //RESPUESTA BTN LIMPIAR
        binding.btLimpiar.setOnClickListener {
            binding.etId.text.clear()
            binding.etTema.text.clear()
            binding.etDescripcion.text.clear()

            Toast.makeText(this,"Campos vacíos.",Toast.LENGTH_SHORT).show()
        }



//=================================================================================== 21/06/2025
        //Funcionalidad de Expansion de formulario
        var expansionFormulario = false

        binding.btnExpandir.setOnClickListener {
            if (!expansionFormulario) {
                // Expandir: altura al máximo
                binding.etDescripcion.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.btnExpandir.text = "Reducir"
            } else {
                // Reducir: volver a altura original
                binding.etDescripcion.layoutParams.height = (245 * resources.displayMetrics.density).toInt()
                binding.btnExpandir.text = "Expandir"
            }
            binding.etDescripcion.requestLayout()
            expansionFormulario = !expansionFormulario
        }

//===================================================================================
    // MAÑANA SERÁ MEJOR. . .

//=================================================================================== 19/06/2025

//RESPUESTA BTN SALIR
        binding.btSalir.setOnClickListener {
            finish()
        }


    }//Fin del onCreate = funcion de arranque


}//Fin de MainActivity