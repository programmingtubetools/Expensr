package com.programmingtubeofficial.expensr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private final String emailPattern = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.]{0,1}[a-zA-Z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            openDashboard();
        }
        progressDialog = new ProgressDialog(this);
        TextView signUp = findViewById(R.id.lblSignup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), register.class);
                startActivity(registerIntent);
            }
        });

        CardView btnLogin = findViewById(R.id.cardLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    private void doLogin(){
        EditText emailParent = findViewById(R.id.txtEmail);
        EditText passwordParent = findViewById(R.id.txtPassword);
        String email = emailParent.getText().toString();
        String password = passwordParent.getText().toString();
        if(email.isEmpty()){
            emailParent.setError("Please enter the email");
            emailParent.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordParent.setError("Please enter the password");
            passwordParent.requestFocus();
            return;
        }
        if(!email.matches(emailPattern)){
            emailParent.setError("Invalid Email");
            emailParent.requestFocus();
            return;
        }

        progressDialog.setMessage("Logging in. Please wait...");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Logged in successfully.", Toast.LENGTH_SHORT).show();
                    openDashboard();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Logged in failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openDashboard(){
        Intent dashboardIntent = new Intent(getApplicationContext(), dashboard.class);
        dashboardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dashboardIntent);
        finish();
    }
}