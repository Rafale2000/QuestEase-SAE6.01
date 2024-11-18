package View.questease;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.questease.R;
import com.google.android.material.button.MaterialButton;

public class TrouveLeSon2 extends Theme {
    private String rulestitle ="Règles du jeu";
    private String rulescontent  ="Le jeu est séparé en deux parties :\n\n - Le premier joueur est l'auditeur, il doit appuyer sur un bouton pour entendre un son et le reconnaître.\n\n - Le rédacteur recevra des messages de l'auditeur et devra entrer dans son terminal de quel élément provient le son.";
    private ViewGroup main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trouve_le_son2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        main = findViewById(R.id.main);
        showTutorialPopup(rulestitle,rulescontent,main);
        MaterialButton regles = findViewById(R.id.Regles);
        regles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTutorialPopup(rulestitle,rulescontent,main);
            }
        });
    }


}