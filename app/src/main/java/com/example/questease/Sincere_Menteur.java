package com.example.questease;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Sincere_Menteur extends AppCompatActivity {

    private int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = 2;

        if (id==2) {
            partie2enigme();
        }
        }
    private void partie2enigme() {
        TextView tv1= findViewById(R.id.textView4);
        TextView tv2= findViewById(R.id.textView5);
        TextView tv3= findViewById(R.id.textView6);
        TextView tv4= findViewById(R.id.textView7);

        tv1.setText("Henri");
        tv2.setText("Jeanne");
        tv3.setText("Marie ment, mais je ne sais pas pour Jacque");
        tv4.setText("Jacque est sincère et Marie ment");

    }
}



