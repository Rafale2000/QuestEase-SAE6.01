package View.questease;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questease.R;

import Service.TextViewUpdater;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class testSpring extends AppCompatActivity {

    private TextViewUpdater textViewUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_spring);

        // TextView setup and TextViewUpdater initialization
        TextView myTextView = findViewById(R.id.textView);
        textViewUpdater = new TextViewUpdater(this, myTextView);
        textViewUpdater.updateTextView(); // Call to update the TextView

    }
}