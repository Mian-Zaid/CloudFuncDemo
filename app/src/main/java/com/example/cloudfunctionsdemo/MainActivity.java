package com.example.cloudfunctionsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //subscribe to topic for receiving notification
        FirebaseMessaging firebaseMessaging= FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("new-user");
    }
}