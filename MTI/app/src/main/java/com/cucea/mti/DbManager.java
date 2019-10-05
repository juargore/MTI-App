package com.cucea.mti;

/**
 * Created by Arturo on 3/19/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Arturo on 2/26/2017.
 */


//Esta clase se encarga del CRUD
public class DbManager {

    private static final String TABLE_NAME = "mensajes";

    ///Listamos los nombres de los campos
    static final String CN_ID = "id";
    static final String CN_TITULO = "titulo";
    static final String CN_DESCRIPCION = "descripcion";
    static final String CN_FECHA = "fecha";

    //Ahora creamos la sentencia SQL para creacion de tabla
    //Los tipos de datos en SQLite son: null, integer, real, text, blob

    //create table alumnos(
    //                      _id integer primary key autoincrement,
    //                      nombre text not null,
    //                      edad integer not null,
    //                      fecha_nacimiento text not null,
    //                      foto blob);

    //Que traducido a codigo Java, seria asi:
    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ( "
            + CN_ID + " integer primary key autoincrement,"
            + CN_TITULO + " text not null,"
            + CN_DESCRIPCION + " text not null,"
            + CN_FECHA + " text not null);";


    private DbHelper helper;
    private SQLiteDatabase db;

    public DbManager(Context context) {

        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
    }

    //Se saca este metodo, porque es un codigo que se usara en muchas ocasiones y no es bueno estar repitiendo
    //el codigo "insertar()"...
    public ContentValues generarContentValues(String titulo, String descripcion, String fecha){
        ContentValues valores = new ContentValues();
        valores.put(CN_TITULO, titulo);
        valores.put(CN_DESCRIPCION, descripcion);
        valores.put(CN_FECHA, fecha);

        return valores;
    }

    //Este metodo es para insertar con ContentValues, es preferible porque es un metodo propio de Android
    public void insertarRegistro(String titulo, String descripcion, String fecha){
        //db.insert(TABLA, NullColumnHack, ContentValues);
        db.insert(TABLE_NAME, null, generarContentValues(titulo, descripcion, fecha));
    }

    //Este metodo es para insertar con sentencia SQL. Es valido, pero mejor usar ContentValues
    /*public void insertarConSQL(String nombre, int edad, String fecha_nacimiento, byte[] foto){

        //INSERT INTO alumnos VALUES (null, 'Pedro', 23, 11/05/1991, [384059803968])
        db.execSQL("insert into "+TABLE_NAME+" values (null,'"+nombre+"',"+edad+",'"+fecha_nacimiento+"',"+foto+")");
    }*/

    public void eliminarRegistro(String id){
        //db.delete(Tabla, Clausula Where, Argumentos Where)
        db.delete(TABLE_NAME, CN_ID + "=?", new String[]{id});
        //NOTA: El ultimo parametro es un vector, porque asi como pudiera ser solo un registro, tambien
        //podriamos eliminar varios registros de un jalon, como se ve en el metodo eliminarMultiples()
    }

    /*public void eliminarMultiples(String nom1, String nom2){
        db.delete(TABLE_NAME, CN_TITULO + "IN (?,?)", new String[]{nom1, nom2});
    }*/

    /*public void modificar(String id, String nombre, int edad, String fecha_nacimiento, byte[] foto){
        //db.update(TABLA, ContentValues, Clausula Where, Argumentos Where)
        db.update(TABLE_NAME, generarContentValues(nombre, edad, fecha_nacimiento, foto), CN_ID + "=?", new String[]{id});
    }*/

    public Cursor cargarAllRegistros(){
        String[] columnas = new String[]{CN_ID, CN_TITULO, CN_DESCRIPCION, CN_FECHA};
        //db.query(TABLA, COLUMNAS, SELECCION, SELECTION_ARGS, GROUP BY, HAVING, ORDER BY)
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);
    }

    /*public Cursor buscar(String nombre){
        String[] columnas = new String[]{CN_ID,CN_TITULO,CN_DESCRIPCION,CN_FECHA,CN_FOTO};
        return db.query(TABLE_NAME, columnas, CN_TITULO + "=?", new String[]{nombre}, null, null, null);
    }*/

    /*public Cursor traerRegistro(String id){
        String[] columnas = new String[]{CN_ID,CN_TITULO,CN_DESCRIPCION,CN_FECHA};
        return db.query(TABLE_NAME, columnas, CN_ID + "=?", new String[]{id}, null, null, null);
    }*/
}
