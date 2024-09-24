package com.example.questease;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    //salut
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Récupérer les logos
        ImageView logo1 = findViewById(R.id.logo1);
        ImageView logo2 = findViewById(R.id.logo2);



        // Lancer MainActivity après 6 secondes
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);  // 6 secondes de délai avant de charger MainActivity
    }


}
