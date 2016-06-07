package com.beka.simpletodoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

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
                            }
                        })
    }
}
