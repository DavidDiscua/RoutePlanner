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
public class Distance {

    private Double Distance;

    public Distance(Double Distance) {
        this.Distance = Distance;
    }

    public Double getDistance() {
        return Distance;
    }

    public void setDistance(Double Distance) {
        this.Distance = Distance;
    }

    @Override
    public String toString() {
        
        return Double.toString(Distance);
    }
}
