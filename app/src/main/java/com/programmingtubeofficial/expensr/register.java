package com.programmingtubeofficial.expensr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class register extends AppCompatActivity {
    EditText txtName, txtEmail, txtPassword, txtCnfPassword;
    CardView btnRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    private final String emailPattern = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.]{0,1}[a-zA-Z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        btnRegister = findViewById(R.id.cardRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRegister();
            }
        });

        progressDialog = new ProgressDialog(this);

        ImageView lblBack = findViewById(R.id.imgBack);
        lblBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginPage();
            }
        });

        TextView lblLoginReg = findViewById(R.id.lblLoginReg);
        lblLoginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginPage();
            }
        });
    }

    private void performRegister() {
        txtName = findViewById(R.id.lblName);
        txtEmail = findViewById(R.id.txtEmailReg);
        txtPassword = findViewById(R.id.txtPasswordReg);
        txtCnfPassword = findViewById(R.id.txtCnfPasswordReg);
        EditText[] inputs = {txtName, txtEmail, txtPassword, txtCnfPassword};
        for (EditText input : inputs){
            if(input.getText().toString().isEmpty()){
                input.setError("Please fill the details");
                input.requestFocus();
                return;
            }
        }
        if(!txtEmail.getText().toString().matches(emailPattern)){
            txtEmail.setError("Invalid Email");
            txtEmail.requestFocus();
            return;
        }
        if(!txtPassword.getText().toString().equals(txtCnfPassword.getText().toString())) {
            txtCnfPassword.setError("Passwords doesn't match");
            txtCnfPassword.requestFocus();
            return;
        }
        if(txtPassword.getText().toString().length() < 6)
        {
            txtPassword.setError("Password length must be greater than 6");
            txtPassword.requestFocus();
            return;
        }
        progressDialog.setMessage("Registration under progress. Please wait...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(txtName.getText().toString()).build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseAuth.getInstance().signOut();
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Registered successfully.", Toast.LENGTH_SHORT).show();
                                    openLoginPage();
                                }
                            });
                }else{
                    Toast.makeText(getApplicationContext(), "Registration failed..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openLoginPage(){
        Intent loginIntent = new Intent(getApplicationContext(), login.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        finish();
    }
}