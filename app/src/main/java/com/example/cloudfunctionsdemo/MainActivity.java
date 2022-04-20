package com.example.cloudfunctionsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FCMSend";
    EditText title, body;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://push-notification-demo-f2d39-default-rtdb.firebaseio.com");
        DatabaseReference dbTokens = firebaseDatabase.getReference("Tokens");

        registerDeviceToken(dbTokens);

        title = findViewById(R.id.txt_title);
        body = findViewById(R.id.txt_body);
        send = findViewById(R.id.btn_send);

        send.setOnClickListener(view -> {

            if (!title.getText().toString().isEmpty() && !body.getText().toString().isEmpty()) {
                sendNotification(dbTokens);
            } else {
                Toast.makeText(this, "Enter data", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void registerDeviceToken(DatabaseReference dbTokens) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    String token = task.getResult();


                    dbTokens.child(token).setValue(token);
                });

    }

    private void sendNotification(DatabaseReference dbTokens) {

        dbTokens.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String token = dataSnapshot.getKey();

                        Log.d(TAG, "onDataChange: " + token);

                        FCMSend.pushNotification(
                                getApplicationContext(),
                                token,
                                title.getText().toString(),
                                body.getText().toString()
                        );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}