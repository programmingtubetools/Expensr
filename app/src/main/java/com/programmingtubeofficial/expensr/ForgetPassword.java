package com.programmingtubeofficial.expensr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private final String emailPattern = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.]{0,1}[a-zA-Z]+";
    TextView txtForgetEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // back button
        findViewById(R.id.imgBackForget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), login.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
            }
        });
        txtForgetEmail = ((TextView)findViewById(R.id.txtForgetEmail));
        findViewById(R.id.cardForget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = txtForgetEmail.getText().toString();
                if(!checkEmail(email)){
                    Toast.makeText(ForgetPassword.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isComplete()){
                                        Toast.makeText(ForgetPassword.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                        ((TextView)findViewById(R.id.txtForgetEmail)).setText("");
                                    }
                                }
                            });
                }
            }
        });
    }

    boolean checkEmail(String email){
        return email.matches(emailPattern);
    }
}