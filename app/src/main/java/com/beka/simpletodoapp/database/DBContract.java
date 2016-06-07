package com.beka.simpletodoapp.database;


import android.provider.BaseColumns;

public class DBContract {
    public static final String DB_NAME = "com.beka.simpletodoapp.db";
    public static final int DB_VERSION = 1;

    public class TodoEntry implements BaseColumns{
        public static final String TABLE = "todo";
        public static final String COL_TITLE = "title";
    }
}
