package com.example.gazorajelento;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gazorajelento.model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private EditText username;
    private EditText password;
    private EditText email;
    private EditText mobile;
    private EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);

        if (firebaseUser != null) {
            loadUserDetails();
        } else {
            Log.d(LOG_TAG, "Valami hiba van!");
            finish();
        }

        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this::updateUserProfile);
    }

    public void loadUserDetails() {
        DocumentReference document = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        document.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserDetails userDetails = task.getResult().toObject(UserDetails.class);
                if (userDetails != null) {
                    username.setText(userDetails.getUsername());
                    password.setText(userDetails.getPassword());
                    email.setText(userDetails.getEmail());
                    mobile.setText(userDetails.getMobile());
                    address.setText(userDetails.getAddress());
                }
            }
        });
    }

    public void loadUserDetailsPlease(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setMessage("Értékek visszaállítva a legutóbbi mentésre");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        loadUserDetails();
    }

    public void updateUserProfile(View view) {
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        String emailText = email.getText().toString();
        String mobileText = mobile.getText().toString();
        String addressText = address.getText().toString();

        UserDetails userDetails = new UserDetails(usernameText, passwordText, emailText, mobileText, addressText);
        DocumentReference document = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        document.set(userDetails);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setMessage("Sikeres módosítás!  ");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void moveToDictation(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, IndexPageActivity.class);
            startActivity(intent);
        }, 1000);
    }


    public void confirmDeletion(View view) {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        view.startAnimation(rotate);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Biztos akarod törölni? Ha igen, üsd be, hogy 'BIZTOS'!");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String confirmation = input.getText().toString();
            if ("BIZTOS".equals(confirmation)) {
                deleteAccount();
            }
        });
        builder.setNegativeButton("Mégse", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Felhasználói fiók törölve.");
                        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
