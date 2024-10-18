package com.example.questease;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.database.Cursor;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button jouer = findViewById(R.id.Jouer);
        Button paramètres = findViewById(R.id.Paramètres);


        dbHelper = new DatabaseHelper(this);



        
        
        
        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Searchlobby.class);
                startActivity(intent);
            }
        });

        paramètres.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Parametres.class);
                startActivity(intent);
            }
        });


    }
    private void initializedatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        if (isTableEmpty(db, "parametres")) {
            ContentValues values = new ContentValues();
            values.put("daltonisme", 0);
            values.put("cecite", 0);
            values.put("assistance_vocale", 0);
            values.put("vision_peripherique", 0);
            values.put("diplopie", 0);
            values.put("myopie", 0);
            values.put("vision_centrale_reduite", 0);
            values.put("albinisme", 0);
            db.insert("parametres", null, values);
        } else {
            Log.d("Database", "La table n'est pas vide, aucune initialisation nécessaire.");
        }
    }

    private boolean isTableEmpty(SQLiteDatabase db, String tableName) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count == 0;
            }
        } catch (Exception e) {
            Log.e("Database", "Erreur lors de la vérification de la table", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }


}