package com.example.gazorajelento;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    private SharedPreferences sharedPreferences;

    EditText username;
    EditText password;
    EditText passwordConfirm;
    EditText email;
    EditText mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        Bundle bundle = getIntent().getExtras();
        int secret_key = bundle != null ? bundle.getInt("SECRET_KEY") : 0;

        if (secret_key != 99) {
            finish();
        }

        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerPassword);
        passwordConfirm = findViewById(R.id.registerPasswordConfirm);
        email = findViewById(R.id.registerEmail);
        mobile = findViewById(R.id.registerMobile);
        sharedPreferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        String userName = sharedPreferences.getString("username","");
        String passWord = sharedPreferences.getString("password","");

        username.setText(userName);
        password.setText(passWord);
        passwordConfirm.setText(passWord);




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void registration(View view) {
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        String passwordConfirmText = passwordConfirm.getText().toString();
        String emailText = email.getText().toString();
        String mobileText = mobile.getText().toString();

        if (passwordText.equals(passwordConfirmText)) {
            Log.i(LOG_TAG, "Regisztrált: " + usernameText + ", jelszó: " + passwordText + ", email: " + emailText + ", mobil: " + mobileText);
        } else {
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a jelszó megerősítése!");
        }
    }

    public void cancelOut(View view) {
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }
}