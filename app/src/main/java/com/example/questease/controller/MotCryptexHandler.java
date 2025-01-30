package com.example.questease.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.questease.model.bdd.MotCryptex;;

public class MotCryptexHandler extends MyDatabaseHelper {
    private SQLiteOpenHelper dbHelper;

    public MotCryptexHandler(Context context) {
        super(context);
        dbHelper = new MyDatabaseHelper(context);
    }

    public int addMotCryptex(MotCryptex motCryptex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_INDICE, motCryptex.getId());
        values.put(KEY_MOT, motCryptex.getMot());
        values.put(KEY_DIFF, motCryptex.getDiff());
        values.put(KEY_ID_INDICE, motCryptex.getIdIndice());
        long insertId = db.insert(TABLE_MOTCRYPTEX, null, values);
        db.close();
        return (int) insertId;
    }

    public MotCryptex getCryptex(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOTCRYPTEX, new String[]{KEY_ID_CRYPTEX,
                        KEY_MOT, KEY_DIFF, KEY_ID_INDICE}, KEY_ID_CRYPTEX + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        return new MotCryptex(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2), cursor.getInt(3));

    }

    /**
     * supprime une chose à trouver
     *
     * @param motCryptex
     */
    public void deleteCryptex(MotCryptex motCryptex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_MOTCRYPTEX, KEY_ID_CRYPTEX + " = ?",
                new String[]{String.valueOf(motCryptex.getId())});
        db.close();
    }


    public int updateCryptex(MotCryptex motCryptex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MOT, motCryptex.getMot());
        values.put(KEY_DIFF, motCryptex.getDiff());
        values.put(KEY_ID_INDICE, motCryptex.getIdIndice());
        long updateId = db.update(TABLE_CHOSE, values,
                KEY_ID_CRYPTEX + " = ?",
                new String[]{String.valueOf(motCryptex.getId())});
        db.close();
        return (int) updateId;
    }


}
