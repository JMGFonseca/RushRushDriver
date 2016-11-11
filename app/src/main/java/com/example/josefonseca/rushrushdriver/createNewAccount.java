package com.example.josefonseca.rushrushdriver;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by JCCA on 12/10/16.
 */

public class createNewAccount extends Activity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText passRegs;
    private EditText emailRegs;
    private EditText postalAddReg;
    private EditText userNRegs;
    private Driver driver;
    private LatLng position;

    private DatabaseReference myRef;

    public FirebaseUser Fdriver;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myRef= FirebaseDatabase.getInstance().getReference();

        passRegs=(EditText) findViewById(R.id.passReg);
        emailRegs=(EditText) findViewById(R.id.emailReg);
        userNRegs=(EditText) findViewById(R.id.userNReg);
        postalAddReg=(EditText) findViewById(R.id.postalAddReg);

        mAuth=FirebaseAuth.getInstance();


        final Button button = (Button) findViewById(R.id.createAcc);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Creates account through Firebase
                createnewAcc(userNRegs.getText().toString().trim(), emailRegs.getText().toString().trim(),postalAddReg.getText().toString().trim(),passRegs.getText().toString().trim());
            }
        });
    }

    public void createnewAcc(final String username, final String email, final String address, final String pass){

        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(createNewAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(createNewAccount.this,"Driver Account created successfully!" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        if (!task.isSuccessful()) {
                            Toast.makeText(createNewAccount.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            driver=new Driver(username, email, address, 0,null,"off", new LatLng(0.0,0.0),0);
                            Fdriver= FirebaseAuth.getInstance().getCurrentUser();
                            myRef=FirebaseDatabase.getInstance().getReference("/Drivers/" + Fdriver.getUid());
                            myRef.setValue(driver);
                            startActivity(new Intent(createNewAccount.this, noPackagesActivity.class));
                            finish();

                        }
                    }
                });

    }

}
