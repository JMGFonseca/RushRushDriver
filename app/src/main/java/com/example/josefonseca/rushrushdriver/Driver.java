package com.example.josefonseca.rushrushdriver;

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


    public Driver(String name, String email, String location, double rating, int numberPacks, String status){
        this.name=name;
        this.email=email;
        this.location=location;
        this.rating=rating;
        this.numberPacks=numberPacks;
        this.status=status;
    }
}
