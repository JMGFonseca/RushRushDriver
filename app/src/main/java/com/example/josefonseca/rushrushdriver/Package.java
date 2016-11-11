package com.example.josefonseca.rushrushdriver;

/**
 * Created by JCCA on 31/10/16.
 */

public class Package {

    public String name;
    public String driver = null;
    public String address;
    public String owner;

    public Package(String name, String driver, String address, String owner) {
        this.name = name;
        this.driver = driver;
        this.address = address;
        this.owner = owner;
    }

    public Package() {
    }

    @Override
    public String toString() {
        return this.name;
    }
}
