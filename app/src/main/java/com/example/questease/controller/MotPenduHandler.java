package com.example.questease.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.questease.model.bdd.MotPendu;

public class MotPenduHandler extends MyDatabaseHelper {
    private SQLiteOpenHelper dbHelper;

    public MotPenduHandler(Context context) {
        super(context);
        dbHelper = new MyDatabaseHelper(context);
    }

    public int addMotPendu(MotPendu motPendu) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_PENDU, motPendu.getId());
        values.put(KEY_MOT, motPendu.getMot());
        values.put(KEY_DIFF, motPendu.getDiff());
        values.put(KEY_ID_INDICE, motPendu.getIdIndice());
        long insertId = db.insert(TABLE_MOTCRYPTEX, null, values);
        db.close();
        return (int) insertId;
    }

    public MotPendu getMotPendu(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOTPENDU, new String[]{KEY_ID_PENDU,
                        KEY_MOT, KEY_DIFF, KEY_ID_INDICE}, KEY_ID_PENDU + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        return new MotPendu(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2), cursor.getInt(3));

    }

    /**
     * supprime une chose à trouver
     *
     * @param motPendu
     */
    public void deleteMotPendu(MotPendu motPendu) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_MOTPENDU, KEY_ID_PENDU + " = ?",
                new String[]{String.valueOf(motPendu.getId())});
        db.close();
    }


    public int updatePendu(MotPendu motPendu) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_PENDU, motPendu.getMot());
        values.put(KEY_DIFF, motPendu.getDiff());
        values.put(KEY_ID_INDICE, motPendu.getIdIndice());
        long updateId = db.update(TABLE_MOTPENDU, values,
                KEY_ID_PENDU + " = ?",
                new String[]{String.valueOf(motPendu.getId())});
        db.close();
        return (int) updateId;
    }


}
