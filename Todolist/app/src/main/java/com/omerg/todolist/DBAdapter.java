package com.omerg.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBAdapter {

    private static final String DATABASE_NAME = " todolist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ITEM = "items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASKNAME = "taskname";
    public static final String COLUMN_EXPDATE = "expdate";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_STATUS = "status";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter (Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(
                        "CREATE TABLE " + TABLE_ITEM + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_TASKNAME + " TEXT NOT NULL,"
                        + COLUMN_EXPDATE + " TEXT NOT NULL,"
                        + COLUMN_DESCRIPTION + " TEXT NOT NULL,"
                        + COLUMN_PRIORITY + " INTEGER,"
                        + COLUMN_STATUS + " INTEGER);"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
            onCreate(db);
        }
    }

    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public boolean deleteTask(int id) {
        return db.delete(TABLE_ITEM, COLUMN_ID + "=" + id, null) > 0;
    }

    public Cursor getAllitemsInOrder() {
        String query = "SELECT * FROM " + TABLE_ITEM + " ORDER BY " + COLUMN_STATUS + " ASC, " +COLUMN_PRIORITY + " ASC, " + COLUMN_EXPDATE + " ASC LIMIT 100";
        return db.rawQuery(query, new String[] {});
    }
    public boolean saveTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, task.getId());
        values.put(COLUMN_TASKNAME, task.getTaskName());
        values.put(COLUMN_EXPDATE, task.getExpDate());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
/*        values.put(COLUMN_PRIORITY, task.getPriority().ordinal());*/
        values.put(COLUMN_STATUS, task.getStatus().ordinal());

        try {
            db.replace(TABLE_ITEM, null, values);
            return  true;
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return false;
    }
}
