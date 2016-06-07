package com.beka.simpletodoapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.beka.simpletodoapp.database.DBContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_item:
                createAlertDialog().show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    //создает диалог для добавления новой заметки
    private AlertDialog createAlertDialog() {
        final EditText editTextItem = new EditText(this);
        return new AlertDialog.Builder(this)
                .setTitle("Добавление новой заметки")
                .setMessage("Выведите текст заметки")
                .setView(editTextItem)
                .setPositiveButton("Добавить",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String item = String.valueOf(editTextItem.getText());
                                //Добавляем новую запись в БД
                                insertIntoDB(item);
                                updateUI();
                            }
                        })
                .setNegativeButton("Отмена", null)
                .create();
    }
    //Добавляем новую запись в БД
    private void insertIntoDB(String item){
        SQLiteDatabase db = mDbHelper.getWritebleDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.TodoEntry.COL_TITLE, item);
        db.insertWithOnConflict(DBContract.TodoEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    private void updateUI(){
        //список для существующих заметок
        List<String> itemList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.TodoEntry.TABLE,
                new String[]{DBContract.TodoEntry._ID,
                    DBContract.TodoEntry.COL_TITLE},
                null, null, null, null, null);
        //считываем все заметки из таблицы в список
        
    }
}
