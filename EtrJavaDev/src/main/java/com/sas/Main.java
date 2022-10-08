package com.sas;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        readLocations();
    }

    private static void readLocations() {
        // String fileString = null;
        String jsonFile = "/home/sask/Documents/codes/etrCode/EtrJavaDev/src/main/resources/interchanges.json";
        try {
            readJSON(jsonFile);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

//        URL resource = Main.class.getClassLoader().getResource("interchanges.json"/*jsonFile*/);
//        byte[] bytes = new byte[0];
//        try {
//            bytes = Files.readAllBytes(Paths.get(resource.toURI()));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }

    private static void readJSON(String filename) throws URISyntaxException {
        URL resource = Main.class.getClassLoader().getResource("interchanges.json");
        Path paths = Paths.get(resource.toURI()).toAbsolutePath();

        try (FileReader reader = new FileReader(/*filename*/paths.toFile())) {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.keySet().iterator().forEachRemaining(element -> {
                handle(jsonObject, element);
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void handle(JSONObject jsonObj, Object element) {
        Object ll = jsonObj.get(element);
        JSONIterator jsonIterater = new JSONIterator();
        jsonIterater.iterateJSONObject(jsonObj);
        Map<Integer, Object> hm = jsonIterater.getObjMap();
        for (Integer key : hm.keySet()) {

            //System.out.println(key + "\t" + hm.get(key));
            Object value = hm.get(key);
        }
    }

//        public static LocationNode createLocationNode ( int id, String name,double lat, double lng){
//            return new LocationNode(name, id, Double.toString(lat), Double.toString(lng));
//        }
}