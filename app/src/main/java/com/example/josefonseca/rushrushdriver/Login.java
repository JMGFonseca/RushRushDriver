package com.example.josefonseca.rushrushdriver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private EditText mailLogin;
    private EditText passLogin;

    public DatabaseReference myRef;

    public static FirebaseUser user;

    public Driver driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        myRef= FirebaseDatabase.getInstance().getReference();

        mailLogin = (EditText) findViewById(R.id.mailLogin);
        passLogin = (EditText) findViewById(R.id.passLogin);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("User", "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d("User", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        final Button button = (Button) findViewById(R.id.loginBut);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform Login on click (checks if data input is correct through Firebase)
                signIn();

            }
        });


        final Button createNew = (Button) findViewById(R.id.createNew);
        createNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform Login on click (checks if data input is correct through Firebase)
                Intent k = new Intent(Login.this, createNewAccount.class);
                startActivity(k);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void signIn() {
        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(mailLogin.getText().toString(), passLogin.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("UserPassEmail", "Sign In successful" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Login.this, noPackagesActivity.class));
                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("UserPassEmail", "signInWithEmail", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}

