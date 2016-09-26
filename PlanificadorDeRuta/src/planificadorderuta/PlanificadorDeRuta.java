/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planificadorderuta;

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
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
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
    //  private static UndirectedGraph<City, Distance> Map;
    private static int CityCounter, TrucksNumber;
    private static City Origin;
    private static int EachTruck;
    private static double Residuo;
    private static ArrayList<Truck> Trucks;//Control del número de camiones
    private static ArrayList<String> Distances;
    private static WeightedMultigraph<City, Distance> Map;
    private static Random R;
    private static ArrayList<Distance> DISTANCIAS;

    public static void main(String[] args) {

        TrucksNumber = 0;//Número de camiones
        CityCounter = 1;//El nombre que se le asignará a cada Ciudad
        Points = new ArrayList();//La Lista de puntos para calcular distancia Geodesica 
        Map = new WeightedMultigraph<City, Distance>(Distance.class);;//El grafo que moldea las ciudades
        R = new Random();
        // SimpleWeightedGraph<City, Distance> graph;graph = new SimpleWeightedGraph<City, Distance>(Distance.class);
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
        //  DrawGraph();
        setRutes();

        //End Setting Up
    }

    private static void BuildGrah() {

        String SplitCity[] = null;
        City CityTemp = null;
        for (int i = 0; i < Points.size(); i++) {
            SplitCity = Points.get(i).split(",");
            CityTemp = new City(Integer.parseInt(SplitCity[0]), Integer.parseInt(SplitCity[1]), CityCounter);
            Map.addVertex(CityTemp);
            CityCounter++;

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
                // if (!((City) Vertices[i]).toString().equalsIgnoreCase(((City) Vertices[j]).toString())) {
                // if (!sameChars(((City) Vertices[i]).toString(), ((City) Vertices[j]).toString())) 
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
        System.out.println("Edges Count :" + Map.edgeSet().size());
    }

    private static double CalculateDistance(double PointA_X, double PointA_Y, double PointB_X, double PointB_Y) {

        return Point2D.distance(PointA_X, PointA_Y, PointB_X, PointB_Y);
    }

    private static ArrayList<String> LoadPoints(ArrayList<String> Points) {

        boolean getTrucksNumber = false;
        try (BufferedReader br = new BufferedReader(new FileReader("./src/Resources/entrada5.txt"))) {

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
        ArrayList<String> AlreadyVisit = new ArrayList();
        for (int X = 0; X < TrucksNumber; X++) {

            for (int i = 0; i < Vertices.length; i++) {
                CurrentNode = (City) Vertices[i];
                if (!CurrentNode.isMarcked()) {

                    Aristas = Map.edgesOf(CurrentNode);//Get all Edges from that node
                    //sort
                    CurrentNode.setMarcked(true);
                    SorterEdges = setTree(Aristas);//in a Tree
                    TruckTemp = new Truck();
                    City TargetCity = null;
                    CitiesCount = 0;
                    for (Object obj : SorterEdges) {
                        Distance D = null;
                        for (int j = 0; j < DISTANCIAS.size(); j++) {
                            if (obj.toString().equalsIgnoreCase(DISTANCIAS.get(j).toString())) {
                                D = DISTANCIAS.get(j);
                                break;
                            }
                        }
                        TargetCity = (City) Map.getEdgeTarget(D);
                        //  if (!TargetCity.isMarcked()) {
                        TruckTemp.setCities(TargetCity);
                        TruckTemp.setDistance((double) obj);
                        TargetCity.setMarcked(true);
                        CitiesCount++;
                        if (CitiesCount == (EachTruck + Residuo)) {

                            Residuo = 0;
                            break;
                        }

                        //  }
                    }

                    if (!TruckTemp.getCities().isEmpty()) {
                        City CityTemp = TruckTemp.getCities().get(TruckTemp.getCities().size() - 1);

                        TruckTemp.setDistance((double) CalculateDistance(CityTemp.getCoordenateInX(), CityTemp.getCoordenateInY(), Origin.getCoordenateInX(), Origin.getCoordenateInY()));
                    }
                    if (CitiesCount == (EachTruck + Residuo)) {

                        Residuo = 0;
                        break;
                    }

                } else {
//                    System.out.println("El nodo ya está marcado");

                }
//fin de for

            }
            Trucks.add(TruckTemp);
        }

        FindIndex(Trucks);

    }

    private static void FindIndex(ArrayList<Truck> Trucks) {
        ArrayList<String> Formato = new ArrayList();
        ArrayList<String> Formato2 = new ArrayList();
        City TargetCity = null;
        ArrayList<City> cities = new ArrayList();
        System.out.println("CIUDADES:");
        for (int i = 0; i < Trucks.size(); i++) {
            cities = Trucks.get(i).getCities();
            Formato.add(Integer.toString(cities.size()));
            for (int j = 0; j < cities.size(); j++) {
                String cor = (int) cities.get(j).getCoordenateInX() + "," + (int) cities.get(j).getCoordenateInY();
                for (int k = 0; k < Points.size(); k++) {
                    if (Points.get(k).equalsIgnoreCase(cor)) {
                        System.out.println(k);
                        Formato.add(Integer.toString(k));
                    }
                }
            }
        }
        System.out.println("RUTAS:");
        for (int i = 0; i < Trucks.size(); i++) {

            Formato2.add("Route " + i + ": " + Trucks.get(i).getDistance());
            System.out.println("Route " + i + ": " + Trucks.get(i).getDistance());
        }
        SaveFile(Formato, "Salidas");
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

//        VisualizationImageServer vs = new VisualizationImageServer(new ISOMLayout(Map), new Dimension(1024, 800));
//        vs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
//        vs.getRenderingHints().remove(
//                RenderingHints.KEY_ANTIALIASING);
//
//        JFrame frame = new JFrame();
//        frame.setLocationRelativeTo(null);
//        frame.setResizable(true);
//        frame.getContentPane().add(vs);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
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
}
