package com.example.questease;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nom de la base de données
    private static final String DATABASE_NAME = "my_database.db";
    // Version de la base de données (permet de gérer les mises à jour)
    private static final int DATABASE_VERSION = 1;

    // Constructeur de la classe
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Méthode appelée lors de la création de la base de données
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création des tables ici
        String CREATE_TABLE = "CREATE TABLE parametres (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "daltonisme TEXT, " +
                "cecite INTEGER," +
                "assistance_vocale INTEGER," +
                "vision_peripherique INTEGER," +
                "diplopie INTEGER," +
                "myopie INTEGER," +
                "vision_centrale_reduite INTEGER," +
                "albinisme INTEGER)";
        String tableSM = "CREATE TABLE jeuSM(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idJoueur INTEGER NOT NULL," +
                "reponse1 BOOLEAN NOT NULL," +
                "reponse2 BOOLEAN NOT NULL)";
        db.execSQL(CREATE_TABLE);
        db.execSQL(tableSM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprime l'ancienne table si elle existe et recrée la nouvelle
        db.execSQL("DROP TABLE IF EXISTS my_table");
        onCreate(db);
    }

    public void addReponseSM(SQLiteDatabase db, int id, boolean r1, boolean r2) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("reponse1", r1);
        values.put("reponse2", r2);
        db.insert("jeuSM", null, values);
    }
}


