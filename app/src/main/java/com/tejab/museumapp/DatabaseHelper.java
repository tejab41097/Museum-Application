package com.tejab.museumapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DatabaseHelper extends SQLiteOpenHelper {

    String DB_PATH = null;
    private static String DB_NAME = "INFO.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    public Cursor getInfo(String name) {
        this.getReadableDatabase();
        String[] b={name};
        Cursor c= myDataBase.query("INFO",null,"title like ?",b,null,null,null);
        return c;
    }

    public Cursor nextEntry(String name){
        this.getReadableDatabase();
        String[] b={name};
        int id=0;
        Cursor c= myDataBase.query("INFO",null,"title like ?",b,null,null,null);
        if(c.moveToFirst()) {
            id = c.getInt(4) + 1;
        }
            if (id > 6) {
                id = 1;
            }
            String[] id1 = {"" + id};
            Cursor c1 = myDataBase.query("INFO", null, "id like ?", id1, null, null, null);

        return c1;
    }
    public Cursor preEntry(String name){
        this.getReadableDatabase();
        String[] b={name};
        int id=0;
        Cursor c= myDataBase.query("INFO",null,"title like ?",b,null,null,null);
        if(c.moveToFirst()) {
            id = c.getInt(4) -1;
        }
        if (id < 1) {
            id = 6;
        }
        String[] id1 = {"" + id};
        Cursor c1 = myDataBase.query("INFO", null, "id like ?", id1, null, null, null);

        return c1;
    }
}