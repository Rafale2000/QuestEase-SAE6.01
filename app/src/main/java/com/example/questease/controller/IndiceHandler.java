package com.example.questease.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.questease.model.bdd.Indice;


public class IndiceHandler extends MyDatabaseHelper {
    private SQLiteOpenHelper dbHelper;

    public IndiceHandler(Context context) {
        super(context);
        dbHelper = new MyDatabaseHelper(context);
    }

    public int addindice(Indice indice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_INDICE, indice.getId());
        values.put(KEY_PATH_SON, indice.getHint());

        long insertId = db.insert(TABLE_INDICE, null, values);
        db.close();
        return (int) insertId;
    }

    public Indice getIndice(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INDICE, new String[]{KEY_ID_INDICE,
                        KEY_PATH_SON}, KEY_ID_INDICE + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        return new Indice(cursor.getInt(0), cursor.getString(1));

    }

    /**
     * supprime une chose à trouver
     *
     * @param indice
     */
    public void deleteIndice(Indice indice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_INDICE, KEY_ID_INDICE + " = ?",
                new String[]{String.valueOf(indice.getId())});
        db.close();
    }


    public int updateIndice(Indice indice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_INDICE, indice.getId());
        values.put(KEY_PATH_SON, indice.getHint());
        long updateId = db.update(TABLE_INDICE, values,
                KEY_ID_INDICE + " = ?",
                new String[]{String.valueOf(indice.getId())});
        db.close();
        return (int) updateId;
    }

}
