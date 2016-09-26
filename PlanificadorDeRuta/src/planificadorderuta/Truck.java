/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planificadorderuta;

import java.util.ArrayList;

/**
 *
 * @author DDiscua
 */
public class Truck {

    private ArrayList<City> Cities;
    private double Distance;

    public Truck() {
        this.Cities = new ArrayList();
        this.Distance=0.0;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double Distance) {
        this.Distance += Distance;
    }

    
    public ArrayList<City> getCities() {
        return Cities;
    }

    public void setCities(City City) {
        this.Cities.add(City);
    }

}
