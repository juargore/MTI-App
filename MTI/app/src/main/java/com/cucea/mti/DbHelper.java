package com.cucea.mti;

/**
 * Created by Arturo on 3/19/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arturo on 2/26/2017.
 */

//Esta clase nos ayuda a Abrir la BD si existe, sino existe nos ayuda a crearla
//y permite actualizar el esquema de la BD
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "maestrias.sqlite"; //puede o no llevar el .sqlite
    private static final int DB_SCHEME_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    //Aqui se crean mediante sentencias SQL las tablas
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Despues de crear la sentencia SQL en DbManager, la llamamos aqui
        sqLiteDatabase.execSQL(DbManager.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
