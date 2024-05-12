package com.example.gazorajelento;

import android.content.Intent;
import android.os.Build;
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

import com.example.gazorajelento.model.GasMeterData;
import com.example.gazorajelento.model.GasMeterInfo;
import com.example.gazorajelento.model.UserDetails;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class IndexPageActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;

    private FirebaseFirestore firebaseFirestore;
    private static final String LOG_TAG = IndexPageActivity.class.getName();
    EditText gasMeterInfoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_index_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gasMeterInfoValue = findViewById(R.id.gasMeterInfoValue);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseFirestore = FirebaseFirestore.getInstance();
            Log.d(LOG_TAG, "Beléptetett felhasználót észleltünk");
            loadUserDetails();
        } else {
            Log.d(LOG_TAG, "Nem beléptetett felhasználót észleltünk");
            finish();
        }
    }

    private void loadUserDetails() {
        Log.d(LOG_TAG, "USERS UID: " + firebaseUser.getUid());
        Log.d(LOG_TAG, "COLLECTION: " + firebaseFirestore.collection("users"));
        DocumentReference document = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        document.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserDetails userDetails = task.getResult().toObject(UserDetails.class);
                if (userDetails != null) {
                    Log.d(LOG_TAG, userDetails.toString());
                }
            }
        });
    }

    public void createGasMeterInfo(View view) {
        Long currentAmount = Long.parseLong(gasMeterInfoValue.getText().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(IndexPageActivity.this);

        DocumentReference document = firebaseFirestore.collection("datas").document(firebaseUser.getUid());

        document.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(LOG_TAG, "Van taskunk nagyon jo");
                GasMeterData gasMeterData = task.getResult().toObject(GasMeterData.class);

                if (gasMeterData == null) {
                    gasMeterData = new GasMeterData();
                    gasMeterData.setData(new ArrayList<>());
                }

                if (gasMeterData.getData().isEmpty() || currentAmount > gasMeterData.getData().get(gasMeterData.getData().size() - 1).getCurrentAmount()) {
                    GasMeterInfo newGasMeterInfo;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        newGasMeterInfo = new GasMeterInfo(LocalDateTime.now().toString(), currentAmount);
                        Log.d(LOG_TAG, "Van infonk" + newGasMeterInfo);
                    } else {
                        newGasMeterInfo = null;
                    }

                    gasMeterData.getData().add(newGasMeterInfo);

                    document.set(gasMeterData).addOnCompleteListener(this::successfullyRegistration);
                    setIntentToAllDatas();
                } else {
                    builder.setMessage("Ez a diktált érték kisebb(vagy ugyanaz), mint a legutoljára bediktált! Visszafele megy az óra?");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Log.d(LOG_TAG, "Az új érték nem nagyobb, mint az utolsó");
                }
            }
        });
    }

    private void testing(Task<Void> task) {
        if (task.isSuccessful()) {
            Log.d(LOG_TAG, "Hurra");
        } else {
            Log.d(LOG_TAG, "Ganaj");
        }
    }

    private void successfullyRegistration(Task<Void> task) {
        if (task.isSuccessful()) {
            Log.d(LOG_TAG, "Sikerült egy gázóra leolvasást regisztrálnod.");
        }
    }

    private void setIntentToAllDatas() {
        Intent intent = new Intent(this, GasMeterDataActivity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}