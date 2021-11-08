package com.example.smartrt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreteBtn,forgotpassword;
    FirebaseAuth fAuth;
    ProgressBar mprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail      =findViewById(R.id.email);
        mPassword   =findViewById(R.id.password);
        mprogressBar =findViewById(R.id.progressBar);
        fAuth       =FirebaseAuth.getInstance();
        mLoginBtn   =findViewById(R.id.loginBtn);
        mCreteBtn   =findViewById(R.id.textlogin);
        forgotpassword=findViewById(R.id.forgotpasstext);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password Is Required.");
                    return;
                }
                if(password.length()<6){
                    mPassword.setError("Password Must be >= 6 Character");
                    return;
                }
                mprogressBar.setVisibility(View.VISIBLE);
                // xac thuc authenti cate
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Login.this, "Error!!Unsuccessfully" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        mCreteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));// chuyen qua login
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail =new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ex mail send reset
                        String mail3=resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail3).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Check Your Email To Reset Pass.",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Check Your Email To Reset Pass." + e.getMessage() ,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //exit
                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }
}