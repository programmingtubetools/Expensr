 package com.programmingtubeofficial.expensr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

 public class MainActivity extends AppCompatActivity {

     private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check if any user is logged in
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            openDashboard();
        }
        //updateUI(currentUser);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this, login.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }

     private void openDashboard() {
        Intent dashboardIntent = new Intent(getApplicationContext(), dashboard.class);
        //dashboardIntent.putExtra("User", currentUser);
        startActivity(dashboardIntent);
     }
 }