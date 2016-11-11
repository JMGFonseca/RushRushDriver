package com.example.josefonseca.rushrushdriver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class noPackagesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ListView pendingPackages;
    public static ArrayList<String> arrayPacks;
    public static Context tempContext;
    public static ArrayList<Package> packDetails;

    public FirebaseUser userLogged;
    public DatabaseReference myRef;
    public DatabaseReference getUserName;

    public static String name;
    public static String owner;
    public static String address;
    public static String driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_packages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pendingPackages = (ListView) findViewById(R.id.pendingPackages);

        userLogged = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("NotDesignatedPackages/");
        getUserName = FirebaseDatabase.getInstance().getReference("Users/" + userLogged.getUid());

        noPackagesActivity.setTempContext(this);

        arrayPacks = new ArrayList<String>();

        //Entra na funçao para preencher o arraylist

        populateListView();

        pendingPackages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final String selected = pendingPackages.getItemAtPosition(position).toString();
                //Ir para a lista de detalhes do pacote
                for(int i=0;i<packDetails.size();i++){
                    if(packDetails.get(i).name==selected){
                        name=packDetails.get(i).name;
                        owner=packDetails.get(i).owner;
                        address=packDetails.get(i).address;
                        final int finalI = i;
                        new AlertDialog.Builder(noPackagesActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Package")
                                .setMessage("Are you sure you want to be the driver of this package?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseDatabase.getInstance().getReference("Users/" + owner + "/packages/" +name+"/driver").setValue(userLogged.getUid());
                                        FirebaseDatabase.getInstance().getReference("NotDesignatedPackages/" + name).removeValue();
                                        FirebaseDatabase.getInstance().getReference("PackagesWithDriver/" + name).setValue(packDetails.get(finalI));
                                        FirebaseDatabase.getInstance().getReference("Drivers/" + userLogged.getUid()+ "/actualPack/" + name).setValue(packDetails.get(finalI));
                                        FirebaseDatabase.getInstance().getReference("PackagesWithDriver/" + name +"/driver").setValue(packDetails.get(finalI).driver);
                                        startActivity(new Intent(noPackagesActivity.this,MapsActivity.class));
                                    }
                                }).setNegativeButton("No", null)
                                        .show();

                    }
                }

            }
        });

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
        getMenuInflater().inflate(R.menu.no_packages, menu);
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
            startActivity(new Intent(noPackagesActivity.this, MapsActivity.class));
        } else if (id == R.id.nav_pending) {
            startActivity(new Intent(noPackagesActivity.this, noPackagesActivity.class));
        } else if (id == R.id.nav_accepted) {
            startActivity(new Intent(noPackagesActivity.this, actualPackageDetails.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void change(ArrayList<String> name) {
        arrayPacks = name;
        ArrayAdapter<String> array = new ArrayAdapter<String>(tempContext, android.R.layout.simple_expandable_list_item_1, arrayPacks);
        pendingPackages.setAdapter(array);
    }

    public static void getDetails(ArrayList<Package> pack) {
        packDetails = pack;

    }

    public static void setTempContext(Context e) {
        tempContext = e;
    }

    //Populates list view with packages from the user (designated or not)
    public void populateListView() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> arrayNoDriver = new ArrayList<String>();
                ArrayList<Package> arrayPackages = new ArrayList<Package>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String name = data.child("name").getValue().toString();
                    String address = data.child("address").getValue().toString();
                    String owner = data.child("owner").getValue().toString();

                    Package pack = new Package(name, null, address, owner);
                    arrayNoDriver.add(pack.name);
                    arrayPackages.add(pack);

                }
                noPackagesActivity.change(arrayNoDriver);
                noPackagesActivity.getDetails(arrayPackages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
}
