package com.example.questease.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.questease.model.BDD.ChoseATrouverPrixJuste;

public class ChoseATrouverPrixJusteHandler extends MyDatabaseHelper {
    private SQLiteOpenHelper dbHelper;

    public ChoseATrouverPrixJusteHandler(Context context) {
        super(context);
        dbHelper = new MyDatabaseHelper(context);
    }

    public int addMoteChose(ChoseATrouverPrixJuste chose) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_CHOSE, chose.getId());
        values.put(KEY_NOM, chose.getNom());
        values.put(KEY_PRIX, chose.getValeur());
        values.put(KEY_PATH_TO_PICTURE, chose.getCheminImage());
        long insertId = db.insert(TABLE_CHOSE, null, values);
        db.close();
        return (int) insertId;
    }

    public ChoseATrouverPrixJuste getChose(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHOSE, new String[]{KEY_ID_CHOSE,
                        KEY_NOM, KEY_PRIX, KEY_PATH_TO_PICTURE}, KEY_ID_CHOSE + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new ChoseATrouverPrixJuste(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getInt(3));

    }

    /**
     * supprime une chose à trouver
     *
     * @param chose
     */
    public void deleteChose(ChoseATrouverPrixJuste chose) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_CHOSE, KEY_ID_CHOSE + " = ?",
                new String[]{String.valueOf(chose.getId())});
        db.close();
    }


    public int updateChose(ChoseATrouverPrixJuste chose) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, chose.getNom());
        values.put(KEY_PRIX, chose.getValeur());
        values.put(KEY_PATH_TO_PICTURE, chose.getCheminImage());
        long updateId = db.update(TABLE_CHOSE, values,
                KEY_ID_CHOSE + " = ?",
                new String[]{String.valueOf(chose.getId())});
        db.close();
        return (int) updateId;
    }


}
