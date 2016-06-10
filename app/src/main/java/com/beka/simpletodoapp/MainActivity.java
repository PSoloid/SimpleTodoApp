package com.beka.simpletodoapp;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.beka.simpletodoapp.database.DBContract;
import com.beka.simpletodoapp.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //объект для работы с базой данных
    private DataBaseHelper mDbHelper;

    private ArrayAdapter<String> mTodoAdapter;
    //объекты для работы с интерфейсом
    private ListView mItemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_main);

        mDbHelper = new DataBaseHelper(this);
        mItemsListView = (ListView) findViewById(R.id.list_todo);

        updateUI();
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
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

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
        List<String> itemsList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.TodoEntry.TABLE,
                new String[]{DBContract.TodoEntry._ID,
                    DBContract.TodoEntry.COL_TITLE},
                null, null, null, null, null);
        //считываем все заметки из таблицы в список
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex((DBContract.TodoEntry.COL_TITLE));
            itemsList.add(cursor.getString(idx));
        }
        //заполняем адаптер списком заметок из БД
        if (mTodoAdapter == null) {
            mTodoAdapter = new ArrayAdapter<>(this,
                    R.layout.item, R.id.title_item, itemsList);
            mItemsListView.setAdapter(mTodoAdapter);
        } else {
            mTodoAdapter.clear();
            mTodoAdapter.addAll(itemsList);
            mTodoAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }



    // Это метод для удаления заметки
    // он вызывается, когда пользователь нажимает на кнопку 'Сделано'
    public void deleteItem(View view) {
        View parent = (View) view.getParent();
        TextView itemTextView = (TextView) parent.findViewById(R.id.title_item);
        String item = String.valueOf(itemTextView.getText());

        //удаляемзаметку по тексту в TextView
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete(DBContract.TodoEntry.TABLE,
                DBContract.TodoEntry.COL_TITLE + " = ?",
                new String[]{item});
        db.close();
        //обновляем адаптер, отвечающий
        //за отображение заметок в интерфейсе
        updateUI();
    }
}
