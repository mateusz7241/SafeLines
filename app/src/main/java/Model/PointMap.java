package Model;

import com.google.firebase.database.DatabaseReference;

public class PointMap {

    public String name;
    public double latitude,longitude; //szerokosc, dlugosc

    public PointMap(){

    }


    public PointMap(String name,double latitude,double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PointMap(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString(){
        return this.name + ":" + "\n" + "Szerokość: "+ latitude + "\n" +"Długość: " + longitude;
    }
}
