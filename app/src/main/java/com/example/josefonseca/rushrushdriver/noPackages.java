package com.example.josefonseca.rushrushdriver;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JCCA on 18/10/16.
 */

public class noPackages extends AppCompatActivity {

    public FloatingActionButton add;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private  FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nopackages);

        add = (FloatingActionButton) findViewById(R.id.addPackage);

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Adds +1 to the package counter on user's firebase profile
                addNewPackage();

            }
        });
    }

    public void addNewPackage(){

    }

    @Override
    protected void onDestroy(){
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user!=null)
                    user.getUid();
            }
        };

        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("Drivers/" + user.getUid() + "/status/");
        myRef.setValue("off");
    }
}
