package com.example.questease;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrouveLeSon extends Theme {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trouve_le_son);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        showTutorialPopup();
    }
    public void showTutorialPopup() {
        View blurBackground = findViewById(R.id.blur_background);
        blurBackground.setVisibility(View.VISIBLE);
        Dialog tutorialDialog = new Dialog(this);
        tutorialDialog.setContentView(R.layout.popuprules_trouveleson);
        tutorialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FloatingActionButton closeButton = tutorialDialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            tutorialDialog.dismiss();
            blurBackground.setVisibility(View.GONE);
        });
        tutorialDialog.show();
    }

}