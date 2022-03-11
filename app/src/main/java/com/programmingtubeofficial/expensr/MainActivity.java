 package com.programmingtubeofficial.expensr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

 public class MainActivity extends AppCompatActivity {

     private FirebaseAuth mAuth;
     private Class nextPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check if any user is logged in
        mAuth = FirebaseAuth.getInstance();
        nextPage = login.class;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            nextPage = dashboard.class;
        }
        //updateUI(currentUser);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(MainActivity.this, nextPage);
            startActivity(intent);
            finish();
        },1500);
    }

     private void openDashboard() {
        Intent dashboardIntent = new Intent(getApplicationContext(), dashboard.class);
        //dashboardIntent.putExtra("User", currentUser);
        startActivity(dashboardIntent);
     }
 }