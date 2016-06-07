package com.beka.simpletodoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by otk_prog on 02.06.2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context){
        super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
    }
    // метод для создания простой таблицы с 2 столбцами в БД
    @Override
    public void onCreate(SQLiteDatabase db) {
        //запрос на создание таблицы в БД
        String createTable = "CREATE TABLE " +
                DBContract.TodoEntry.TABLE +"(" +
                DBContract.TodoEntry._ID +
                "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.TodoEntry.COL_TITLE +
                " TEXT NOT NULL);";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TodoEntry.TABLE);
        onCreate(db);
    }
}
