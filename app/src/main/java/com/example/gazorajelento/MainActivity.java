package com.example.gazorajelento;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    EditText usernameInput;
    EditText passowordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        usernameInput = findViewById(R.id.usernameInput);
        passowordInput = findViewById(R.id.passwordInput);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View view) {
        String username = usernameInput.getText().toString().trim();
        String password = passowordInput.getText().toString().trim();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        if (username.isEmpty() || password.isEmpty()) {
            builder.setMessage("Legyszi toltsd ki az adatokat");
            builder.setTitle("Heeeeee");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Log.i(LOG_TAG, "User or Pass Field is null");
            return;
        } else if (usernameInput.getText().toString().contains(" ")
                    || passowordInput.getText().toString().contains(" ")) {
            builder.setMessage("Legkozelebb ne hasznalj szokozoket!");
            builder.setTitle("Eriggy mar innen");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Log.i(LOG_TAG, "Contained space");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder("Bejelentkezett: ")
                .append(username)
                .append(", ")
                .append(password);
        builder.setMessage("Kiraaaly vagy!");
        builder.setTitle("Siker!");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Log.i(LOG_TAG, stringBuilder.toString());
    }
}