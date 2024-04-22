package com.example.gazorajelento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    private SharedPreferences sharedPreferences;

    private FirebaseAuth firebaseAuth;

    EditText username;
    EditText password;
    EditText passwordConfirm;
    EditText email;
    EditText mobile;
    Spinner mobileSpinner;
    EditText address;


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
        mobileSpinner = findViewById(R.id.mobileSpinner);
        address = findViewById(R.id.registerAddress);
        sharedPreferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        String userName = sharedPreferences.getString("username","");
        String passWord = sharedPreferences.getString("password","");

        username.setText(userName);
        password.setText(passWord);
        passwordConfirm.setText(passWord);

        mobileSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mobileOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mobileSpinner.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();



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
        String mobileType = mobileSpinner.getSelectedItem().toString();
        String addressText = address.getText().toString();

        if (passwordText.equals(passwordConfirmText)) {
            Log.i(LOG_TAG, "Regisztrált: " + usernameText + ", jelszó: " + passwordText + ", email: " + emailText + ", telefonszám: " + mobileText + ", telefontípus: " + mobileType + ", cím: " + addressText);
        } else {
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a jelszó megerősítése!");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "Sikerült egy új felhasználót létrehozni");
                    setIntentToIndexPage();
                } else {
                    Log.d(LOG_TAG, "Sajnos nem jött létre új felhasználó");
                }
            }
        });


    }

    private void setIntentToIndexPage () {
        Intent intent = new Intent(this, IndexPageActivity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Log.i(LOG_TAG, selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}