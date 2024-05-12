package com.example.gazorajelento;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gazorajelento.model.UserDetails;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    private SharedPreferences sharedPreferences;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

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

        String eMail = sharedPreferences.getString("username", "");
        String passWord = sharedPreferences.getString("password", "");

        email.setText(eMail);
        password.setText(passWord);
        passwordConfirm.setText(passWord);

        mobileSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mobileOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mobileSpinner.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void registration(View view) {
        String usernameText;
        String passwordText;
        String passwordConfirmText;
        String emailText;
        String mobileText;
        String mobileType;
        String addressText;
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);

        try {
            usernameText = username.getText().toString();
            passwordText = password.getText().toString();
            passwordConfirmText = passwordConfirm.getText().toString();
            emailText = email.getText().toString();
            mobileText = mobile.getText().toString();
            mobileType = mobileSpinner.getSelectedItem().toString();
            addressText = address.getText().toString();
        } catch (Exception e) {
            Log.d(LOG_TAG, "Valami nem sikerült! ");
            return;
        }

        if (usernameText.isEmpty() ||
                passwordText.isEmpty() ||
                passwordConfirmText.isEmpty() ||
                emailText.isEmpty() ||
                mobileText.isEmpty() ||
                mobileType.isEmpty() ||
                addressText.isEmpty()) {
            builder.setMessage("Kérlek add meg az adataid!");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }

        if (passwordText.equals(passwordConfirmText)) {
            Log.i(LOG_TAG, "Regisztrált: " + usernameText + ", jelszó: " + passwordText + ", email: " + emailText + ", telefonszám: " + mobileText + ", telefontípus: " + mobileType + ", cím: " + addressText);
        } else {
            builder.setMessage("Nem egyenlő a jelszó és a jelszó megerősítése!");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a jelszó megerősítése!");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                builder.setMessage("Sikeres regisztráció! ");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Log.d(LOG_TAG, "A bejelentkezett felhasználó: " + task.getResult().toString());
                DocumentReference document = firebaseFirestore.collection("users")
                        .document(task.getResult().getUser().getUid());

                UserDetails userDetails = new UserDetails(usernameText, passwordText, emailText, mobileText, addressText);
                document.set(userDetails).addOnCompleteListener(this::successfullyRegistration);
            } else {
                String whyNot;
                if (passwordText.length() < 6) {
                    whyNot = "Túl rövid jelszó (min. 6 karakter)!";
                } else {
                    whyNot = "Ilyen email címmel már van regisztrálva valaki!";
                }
                builder.setMessage("Sajnos nem sikerült a regisztráció! " + whyNot);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Log.d(LOG_TAG, "TASK: " + task.getException());
                Log.d(LOG_TAG, "Sajnos nem jött létre új felhasználó");
            }
        });

    }

    private void successfullyRegistration(Task<Void> task) {
        if (task.isSuccessful()) {
            Log.d(LOG_TAG, "Sikerült egy új felhasználót létrehozni");

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String channelId = "registration_channel";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Registration Notifications",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("Sikeres regisztráció")
                    .setContentText("Sikeresen regisztráltál az alkalmazásban!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationManager.notify(0, builder.build());

            setIntentToProfilePage();
        }
    }

    private void setIntentToProfilePage() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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