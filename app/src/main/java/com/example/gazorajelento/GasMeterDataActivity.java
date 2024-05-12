package com.example.gazorajelento;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gazorajelento.model.GasMeterData;
import com.example.gazorajelento.model.GasMeterInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GasMeterDataActivity extends AppCompatActivity {

    private static final String LOG_TAG = GasMeterDataActivity.class.getName();

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private GasMeterDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_data);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (firebaseUser != null) {
            loadGasMeterData();
        } else {
            Log.d(LOG_TAG, "Nem beléptetett felhasználót észleltünk");
            finish();
        }
    }

    private void loadGasMeterData() {
        DocumentReference document = firebaseFirestore.collection("datas").document(firebaseUser.getUid());
        document.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                GasMeterData gasMeterData = task.getResult().toObject(GasMeterData.class);
                if (gasMeterData != null) {
                    List<GasMeterInfo> gasMeterInfoList = gasMeterData.getData();
                    adapter = new GasMeterDataAdapter(gasMeterInfoList);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }
}
