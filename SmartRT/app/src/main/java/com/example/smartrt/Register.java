package com.example.smartrt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText mFullname,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar mprogressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullname   =findViewById(R.id.fullName);
        mEmail      =findViewById(R.id.email);
        mPassword   =findViewById(R.id.password);
        mPhone      =findViewById(R.id.phone);
        mRegisterBtn=findViewById(R.id.registerBtn);
        mLoginBtn   =findViewById(R.id.textlogin); //text login chuyen qua login

        fAuth       =FirebaseAuth.getInstance();
        mprogressBar =findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Login.class));      // chuyen sang main
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1=mEmail.getText().toString().trim();
                String password1 = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email1)){
                    mEmail.setError("Email Is Required");
                    return;
                }
                if(TextUtils.isEmpty(password1)){
                    mPassword.setError("Password Is Required");
                    return;
                }
                if(password1.length() < 6){
                    mPassword.setError("Password Must Be >=6 Charaters");
                    return;
                }
                mprogressBar.setVisibility(View.VISIBLE);
                // dang nhap vao firebase
                fAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }else {
                            Toast.makeText(Register.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));  // chuyen qua login
            }
        });

    }
}