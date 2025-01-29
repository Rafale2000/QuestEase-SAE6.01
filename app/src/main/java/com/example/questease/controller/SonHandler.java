package com.example.questease.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.questease.model.BDD.Son;

public class SonHandler extends MyDatabaseHelper {
    private SQLiteOpenHelper dbHelper;

    public SonHandler(Context context) {
        super(context);
        dbHelper = new MyDatabaseHelper(context);
    }

    public int addSon(Son son) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_PENDU, son.getId());
        values.put(KEY_PATH_SON, son.getSon());
        values.put(KEY_ID_INDICE, son.getIdIndice());

        long insertId = db.insert(TABLE_SON, null, values);
        db.close();
        return (int) insertId;
    }

    public Son getSon(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SON, new String[]{KEY_ID_SON,
                        KEY_PATH_SON, KEY_ID_INDICE}, KEY_ID_SON + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        return new Son(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));

    }

    /**
     * supprime une chose à trouver
     *
     * @param son
     */
    public void deleteMotSon(Son son) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_SON, KEY_ID_SON + " = ?",
                new String[]{String.valueOf(son.getId())});
        db.close();
    }


    public int updateSon(Son son) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_SON, son.getId());
        values.put(KEY_PATH_SON, son.getSon());
        values.put(KEY_ID_INDICE, son.getIdIndice());
        long updateId = db.update(TABLE_SON, values,
                KEY_ID_SON + " = ?",
                new String[]{String.valueOf(son.getId())});
        db.close();
        return (int) updateId;
    }

}
