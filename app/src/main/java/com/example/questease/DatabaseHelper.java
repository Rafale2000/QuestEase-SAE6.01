package com.example.questease;

import android.content.Context;
import android.database.Cursor;
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
                "daltonisme TEXT, "+
                "cecite INTEGER,"+
                "assistance_vocale INTEGER,"+
                "vision_peripherique INTEGER,"+
                "diplopie INTEGER,"+
                "myopie INTEGER,"+
                "vision_centrale_reduite INTEGER,"+
                "albinisme INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprime l'ancienne table si elle existe et recrée la nouvelle
        db.execSQL("DROP TABLE IF EXISTS my_table");
        onCreate(db);
    }

    public Cursor getValue(SQLiteDatabase db){
        // Exécute la requête SQL pour récupérer toutes les valeurs de la table
        System.out.println(db.rawQuery("SELECT * FROM parametre " , null));;
        return db.rawQuery("SELECT * FROM parametre " , null);
    }
}


