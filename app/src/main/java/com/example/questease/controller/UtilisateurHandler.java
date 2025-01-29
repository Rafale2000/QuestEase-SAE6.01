package com.example.questease.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.questease.model.BDD.Utilisateur;

public class UtilisateurHandler extends MyDatabaseHelper {

    private SQLiteOpenHelper dbHelper;


    public UtilisateurHandler(Context context) {
        super(context);
        dbHelper = new MyDatabaseHelper(context);
    }

    public int addUtilisateur(Utilisateur U) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IDUSER, U.getId());
        values.put(KEY_NOMUSER, U.getUsername());
        values.put(KEY_XP, U.getXp());
        values.put(KEY_RES, U.getResultatPasse());
        values.put(KEY_EMAIL, U.getEmail());

        long insertId = db.insert(TABLE_UTILISATEUR, null, values);

        return (int) insertId;
    }

    public Utilisateur getUtilisateur(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_UTILISATEUR, new String[]{KEY_IDUSER,
                        KEY_NOM, KEY_XP, KEY_RES, KEY_EMAIL}, KEY_IDUSER + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        return new Utilisateur(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2), cursor.getString(3),
                cursor.getString(4));

    }

    /**
     * supprime une chose à trouver
     *
     * @param Util
     */
    public void deleteUtil(Utilisateur Util) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_UTILISATEUR, KEY_IDUSER + " = ?",
                new String[]{String.valueOf(Util.getId())});
        db.close();
    }

    public int updateUtil(Utilisateur Util) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, Util.getUsername());
        values.put(KEY_XP, Util.getXp());
        values.put(KEY_RES, Util.getResultatPasse());
        values.put(KEY_EMAIL, Util.getEmail());
        long updateId = db.update(TABLE_UTILISATEUR, values,
                KEY_IDUSER + " = ?",
                new String[]{String.valueOf(Util.getId())});
        db.close();
        return (int) updateId;
    }
}
