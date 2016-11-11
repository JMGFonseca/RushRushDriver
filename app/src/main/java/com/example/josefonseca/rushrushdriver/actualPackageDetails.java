package com.example.josefonseca.rushrushdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class actualPackageDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static DatabaseReference myRef;
    public static ArrayList<Package> packDetails;

    public static TextView packAddress;
    public static  TextView packDriver;
    public static  TextView packOwner;
    public static TextView packName;

    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_package_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myRef= FirebaseDatabase.getInstance().getReference("Drivers/" + Login.user.getUid() + "/actualPack");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        packDetails=new ArrayList<Package>();

        getPackageDetails();

        packAddress=(TextView)findViewById(R.id.packAddress);
        packName=(TextView) findViewById(R.id.packName);
        packOwner=(TextView) findViewById(R.id.packOwner);

    }


    public void getPackageDetails(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Package> arrayTemp= new ArrayList<Package>();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String name= data.child("name").getValue().toString();
                    String address=data.child("address").getValue().toString();
                    String owner= data.child("owner").getValue().toString();

                    Package temp = new Package(name, Login.user.getUid(),address,owner);
                    arrayTemp.add(temp);
                }
                getDetails(arrayTemp);
                rateDriverToTree(arrayTemp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void rateDriverToTree(ArrayList<Package> pack){
        final String owner = pack.get(0).owner;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Users/"+owner+"/driverToRate/" + Login.user.getUid() +"/name").setValue(Login.user.getUid());
                startActivity(new Intent(actualPackageDetails.this, noPackagesActivity.class));
            }
        });

    }

    public static void getDetails(ArrayList<Package> pack) {

        packDetails = pack;
        packAddress.setText(packDetails.get(0).address);
        packName.setText(packDetails.get(0).name);
        packOwner.setText(packDetails.get(0).owner);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actual_package_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            startActivity(new Intent(actualPackageDetails.this, MapsActivity.class));
        } else if (id == R.id.nav_pending) {
            startActivity(new Intent(actualPackageDetails.this, noPackagesActivity.class));
        } else if (id == R.id.nav_accepted) {
            startActivity(new Intent(actualPackageDetails.this, actualPackageDetails.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
