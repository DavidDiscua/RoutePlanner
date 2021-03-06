/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planificadorderuta;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.jgrapht.graph.WeightedMultigraph;

/**
 *
 * @author DDiscua
 */
public class PlanificadorDeRuta {

    /**
     * David Discua 11321009
     */
    private static ArrayList<String> Points;
    private static int TrucksNumber;
    private static City Origin;
    private static int EachTruck;
    private static double Residuo;
    private static ArrayList<Truck> Trucks;//Control del número de camiones
    private static ArrayList<String> Distances;
    private static WeightedMultigraph<City, Distance> Map;
    private static ArrayList<Distance> DISTANCIAS;
    private static String FilePath;

    public static void main(String[] args) {

  
        LoadAll();
        //DrawGraph();
        //End Setting Up
    }

    public static void LoadAll() {
        TrucksNumber = 0;//Número de camiones
        Points = new ArrayList();//La Lista de puntos para calcular distancia Geodesica 
        Map = new WeightedMultigraph<City, Distance>(Distance.class);;//El grafo que moldea las ciudades
        Distances = new ArrayList();
        //Setting Up
        Points = LoadPoints(Points);
        EachTruck = Points.size() / TrucksNumber;
        Residuo = (Points.size() % TrucksNumber);
        Trucks = new ArrayList();
        DISTANCIAS = new ArrayList();
        System.out.println("Numbers Of Trucks: " + TrucksNumber);
        System.out.println("Each Truck: " + EachTruck);
        System.out.println("Residuo : " + Residuo);
        BuildGrah();
        setRutes();
    }

    private static void BuildGrah() {

        String SplitCity[] = null;
        City CityTemp = null;
        for (int i = 0; i < Points.size(); i++) {
            SplitCity = Points.get(i).split(",");
            CityTemp = new City(Integer.parseInt(SplitCity[0]), Integer.parseInt(SplitCity[1]), (i + 1));
            Map.addVertex(CityTemp);
            if (i == 0) {
                Origin = CityTemp;
            }
        }
        //Set up Vertex
        Object[] Vertices = Map.vertexSet().toArray();
        for (int i = 0; i < Vertices.length; i++) {

            for (int j = 0; j < Vertices.length; j++) {
                // control++;
                double distance = (CalculateDistance(((City) Vertices[i]).getCoordenateInX(), ((City) Vertices[i]).getCoordenateInY(), ((City) Vertices[j]).getCoordenateInX(), ((City) Vertices[j]).getCoordenateInY()));
                Distance D = new Distance(distance);
                String CityOne = ((City) Vertices[i]).toString();
                String CityTwo = ((City) Vertices[j]).toString();
                if (!CityOne.equalsIgnoreCase(CityTwo)) {
                    Map.addEdge(((City) Vertices[i]), ((City) Vertices[j]), D);
                    DISTANCIAS.add(D);

                }
            }
        }
        System.out.println("Nodes Count :" + Map.vertexSet().size());
    }

    private static double CalculateDistance(double PointA_X, double PointA_Y, double PointB_X, double PointB_Y) {

        return Point2D.distance(PointA_X, PointA_Y, PointB_X, PointB_Y);
    }

    private static ArrayList<String> LoadPoints(ArrayList<String> Points) {

        boolean getTrucksNumber = false;
        try (BufferedReader br = new BufferedReader(new FileReader("./src/Resources/entrada2.txt"))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                //  System.out.println(sCurrentLine);
                if (getTrucksNumber == false) {
                    TrucksNumber = Integer.parseInt(sCurrentLine);
                    getTrucksNumber = true;
                } else {

                    Points.add(sCurrentLine);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Points;
    }

    private static void setRutes() {

        //Sort Distance per node and then take Numbers of truck Nodes.
        //TrucksNumber;
        //Residuo;
        //Trucks;
        City CurrentNode = null;
        Set<Distance> Aristas = null;
        TreeSet SorterEdges = new TreeSet();
        Object[] Vertices = Map.vertexSet().toArray();
        Truck TruckTemp = null;
        int CitiesCount = 0;
        int couterMark = 0;
        int couterMark2 = 0;
        for (int i = 0; i < Vertices.length; i++) {
            CurrentNode = (City) Vertices[i];
            if (!CurrentNode.isMarcked()) {
                couterMark++;
                CurrentNode.setMarcked(true);
                Aristas = Map.edgesOf(CurrentNode);//Get all Edges from that node
                //sort
                SorterEdges = setTree(Aristas);//in a Tree
                TruckTemp = new Truck();
                City TargetCity = null;
                CitiesCount = 0;
                for (Object obj : SorterEdges) {

                    Distance D = SearchInstance(obj);
                    TargetCity = (City) Map.getEdgeTarget(D);

                    if (!TargetCity.isMarcked()) {
                        // System.out.println("Target City:" + TargetCity);
                        TruckTemp.setCities(TargetCity);
                        TruckTemp.setDistance((double) obj);
                        TargetCity.setMarcked(true);
                        //  System.out.println("Ciudades: " + TruckTemp.getCities().size());
                        CitiesCount++;
                        if (CitiesCount == (EachTruck + Residuo)) {
                            Residuo = 0;
                            break;
                        }

                    } else {
                        // System.out.println("CITY VISITADA: " + TargetCity.toString());
                        //  System.out.println("Target City Visitada");
                    }
                }
                if (!TruckTemp.getCities().isEmpty()) {
                    TruckTemp.setDistance(getPartialDistance(TruckTemp.getCities()));
                    Trucks.add(TruckTemp);
                }
            } else {
                //System.out.println("El nodo ya está marcado");
                couterMark2++;

            }
        }
        System.out.println("Marcados : " + couterMark2);
        System.out.println("No estaba marcados: " + couterMark);

        FindIndex(Trucks);
    }

    private static void FindIndex(ArrayList<Truck> Trucks) {
        ArrayList<String> Formato = new ArrayList();
        ArrayList<String> Formato2 = new ArrayList();
        City TargetCity = null;
        ArrayList<City> cities = new ArrayList();
        for (int i = 0; i < Trucks.size(); i++) {
            cities = Trucks.get(i).getCities();
            Formato.add(Integer.toString(cities.size()));
            for (int j = 0; j < cities.size(); j++) {
                String cor = (int) cities.get(j).getCoordenateInX() + "," + (int) cities.get(j).getCoordenateInY();
                for (int k = 0; k < Points.size(); k++) {
                    if (Points.get(k).equalsIgnoreCase(cor)) {
                        Formato.add(Integer.toString(k));
                    }
                }
            }
        }
        for (int i = 0; i < Trucks.size(); i++) {

            Formato2.add("Route " + i + ": " + Trucks.get(i).getDistance());
        }
        SaveFile(Formato, "salida");
        SaveFile(Formato2, "calculos");
    }

    private static void SaveFile(ArrayList<String> Lista, String name) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(name + ".txt"), "utf-8"));

            for (int i = 0; i < Lista.size(); i++) {
                writer.write((String) Lista.get(i) + "\n");
            }
            writer.close();

        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/
            }
        }

    }

    private static void DrawGraph() {

        VisualizationImageServer vs = new VisualizationImageServer(new ISOMLayout((Graph) Map), new Dimension(1024, 800));
        vs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vs.getRenderingHints().remove(
                RenderingHints.KEY_ANTIALIASING);

        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.getContentPane().add(vs);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static boolean sameChars(String firstStr, String secondStr) {
        char[] first = firstStr.toCharArray();
        char[] second = secondStr.toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);
        return Arrays.equals(first, second);
    }

    public static TreeSet setTree(Set<Distance> Aristas) {
        TreeSet SorterEdges = new TreeSet();
        for (Object obj : Aristas) {
            SorterEdges.add(new Double(obj.toString()));
        }
        return SorterEdges;
    }

    public static Distance SearchInstance(Object obj) {

        for (int j = 0; j < DISTANCIAS.size(); j++) {
            if (obj.toString().equalsIgnoreCase(DISTANCIAS.get(j).toString())) {
                return DISTANCIAS.get(j);

            }
        }
        return null;
    }

    public static double getPartialDistance(ArrayList<City> Cities) {
        double PartialDistance = 0.0;
        Distance distance = null;
        for (int i = 0; i < Cities.size(); i++) {

            if ((i + 1) < Cities.size()) {

                if (!Cities.get(i).toString().equalsIgnoreCase(Cities.get(i + 1).toString())) {
                    distance = Map.getEdge(Cities.get(i), Cities.get(i + 1));//Obtener la distancia entre una ciudad y la otra
                    PartialDistance += distance.getDistance();
                }

            } else//Ultima Ciudad
             if (!Cities.get(i).toString().equalsIgnoreCase(Origin.toString())) {
                    distance = Map.getEdge(Cities.get(i), Origin);
                    PartialDistance += distance.getDistance();
                }

        }

        return PartialDistance;
    }

}
