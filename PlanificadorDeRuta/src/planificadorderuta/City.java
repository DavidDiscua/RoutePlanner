/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planificadorderuta;

/**
 *
 * @author DDiscua
 */
public class City {

    private double CoordenateInX;
    private double CoordenateInY;
    private int City_Id;
    private boolean Marcked;

    public City(){}
    public City(double CoordenateInX, double CoordenateInY, int City_Id) {
        this.CoordenateInX = CoordenateInX;
        this.CoordenateInY = CoordenateInY;
        this.City_Id = City_Id;
        this.Marcked=false;
    }

    public double getCoordenateInX() {
        return CoordenateInX;
    }

    public void setCoordenateInX(double CoordenateInX) {
        this.CoordenateInX = CoordenateInX;
    }

    public double getCoordenateInY() {
        return CoordenateInY;
    }

    public void setCoordenateInY(double CoordenateInY) {
        this.CoordenateInY = CoordenateInY;
    }

    public int getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(int City_Id) {
        this.City_Id = City_Id;
    }

    public boolean isMarcked() {
        return Marcked;
    }

    public void setMarcked(boolean Marcked) {
        this.Marcked = Marcked;
    }
    
    
    

    @Override
    public String toString() {
        return "City Id : "+City_Id+"  X: "+CoordenateInX+"  Y:  "+CoordenateInY;
    }
}
