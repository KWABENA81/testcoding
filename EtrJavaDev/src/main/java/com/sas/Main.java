package com.sas;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        readLocations();
    }

    private static void readLocations() {
        String fileString = null;
        String jsonFile = "/home/sask/Documents/codes/etrCode/EtrJavaDev/src/main/resources/interchanges.json";
        try {
            readJSON(jsonFile);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        URL resource = Main.class.getClassLoader().getResource("interchanges.json"/*jsonFile*/);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(resource.toURI()));
//            System.out.println(" 35 STR:  " + Paths.get(resource.toURI()).toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        fileString = new String(bytes);

//        if (fileString != null)
//            print(fileString);

        LocationNode locationNode0 =
                createLocationNode(1, "QEW", 43.484361d, -79.766037d);
        LocationNode locationNode2 =
                createLocationNode(2, "Dundas Street", 43.383554d, -79.833478d);
        computeDistanceAndCharge(locationNode0, locationNode2);
    }

    private static void readJSON(String filename) throws URISyntaxException {
        URL resource = Main.class.getClassLoader().getResource("interchanges.json");
        Path paths = Paths.get(resource.toURI()).toAbsolutePath();

        try (FileReader reader = new FileReader(/*filename*/paths.toFile())) {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);


           System.out.println(" 62 \n"+jsonObject.keySet()+"  \n" + jsonObject.values());
            System.out.println("\n\n 63 \n"+jsonObject.entrySet());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void print(String fileString) {
        System.out.println("jason file: " + fileString);
    }

    public static void computeDistanceAndCharge(LocationNode locationNode1, LocationNode locationNode2) {
        print(locationNode1, locationNode2);
    }

    private static void print(LocationNode locationNode1, LocationNode locationNode2) {
        System.out.println(locationNode2.toString());
        System.out.println(locationNode1.toString());
    }

    public static LocationNode createLocationNode(int id, String name, double lat, double lng) {
        return new LocationNode(name, id, Double.toString(lat), Double.toString(lng));
    }
}