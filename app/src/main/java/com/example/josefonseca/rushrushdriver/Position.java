package com.example.josefonseca.rushrushdriver;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by JCCA on 10/11/16.
 */

public class Position {

    public Double lat;
    public Double lng;

    public Position(LatLng coords){

        this.lat=coords.latitude;
        this.lng=coords.longitude;
    }

}
