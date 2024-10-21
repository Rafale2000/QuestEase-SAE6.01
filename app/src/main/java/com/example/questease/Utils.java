package com.example.questease;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import java.util.ArrayList;

public class Utils  {

    @SuppressLint("Range")
    public static void applyTheme(Activity activity) {
        DatabaseHelper db = new DatabaseHelper(activity);
        SQLiteDatabase dbb = db.getReadableDatabase();
        Cursor cursor = db.getValue(dbb);
        @SuppressLint("Range") String daltonisme = "";
        @SuppressLint("Range") int cecite = 0;
        @SuppressLint("Range") int assistanceVocale = 0;
        @SuppressLint("Range") int visionPeripherique = 0;
        @SuppressLint("Range") int diplopie = 0;
        @SuppressLint("Range") int myopie = 0;
        @SuppressLint("Range") int visionCentraleReduite = 0;
        @SuppressLint("Range") int albinisme = 0;
        if (cursor.moveToFirst()) {
            // Récupération des différentes valeurs
            daltonisme = cursor.getString(cursor.getColumnIndex("daltonisme"));
            cecite = cursor.getInt(cursor.getColumnIndex("cecite"));
            assistanceVocale = cursor.getInt(cursor.getColumnIndex("assistance_vocale"));
            visionPeripherique = cursor.getInt(cursor.getColumnIndex("vision_peripherique"));
            diplopie = cursor.getInt(cursor.getColumnIndex("diplopie"));
            myopie = cursor.getInt(cursor.getColumnIndex("myopie"));
            visionCentraleReduite = cursor.getInt(cursor.getColumnIndex("vision_centrale_reduite"));
            albinisme = cursor.getInt(cursor.getColumnIndex("albinisme"));
        }
        if (daltonisme == "Deutéranomalie") {
            activity.setTheme(R.style.Theme_Questease_deuteranomalie);
        } else if (daltonisme == "Deutéranopie") {
            activity.setTheme(R.style.Theme_Questease_Deuteranopie);
        } else if (daltonisme == "Protanomalie") {
            activity.setTheme(R.style.Theme_Questease_Protanomalie);
        } else if (daltonisme == "Protanopie") {
            activity.setTheme(R.style.Theme_Questease_Protanopie);
        }
        if (cecite == 1) {

        }
        if (assistanceVocale == 1) {

        }
        if (visionPeripherique == 1) {

        }
        if (diplopie == 1) {

        }
        if (myopie == 1) {

        }
        if (visionCentraleReduite == 1) {

        }
        if (albinisme == 1){
            
        }


    }
}
