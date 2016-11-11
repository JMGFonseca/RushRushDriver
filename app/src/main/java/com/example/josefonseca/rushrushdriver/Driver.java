package com.example.josefonseca.rushrushdriver;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by JCCA on 27/10/16.
 */

public class Driver {

    public  String name;
    public String email;
    public String location;
    public double rating;
    public int numberPacks;
    public String status;
    public LatLng position;
    public Package actualPack;
    public int evaluations;


    public Driver(String name, String email, String location, double rating, Package actualPack, String status, LatLng position, int evaluations){
        this.name=name;
        this.email=email;
        this.location=location;
        this.rating=rating;
        this.status=status;
        this.position=position;
        this.actualPack=actualPack;
        this.evaluations=evaluations;
    }
}
