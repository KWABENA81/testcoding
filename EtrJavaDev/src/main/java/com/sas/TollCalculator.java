package com.sas;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TollCalculator {
    public static void main(String... args) throws URISyntaxException {
        readLocations(args);
    }

    private static void readLocations(String... args) throws URISyntaxException {
        try {
            boolean ideArgBool = false;
            if (args != null && !args[0].isEmpty()) {
                //  retrieve file from project root folder
                readJsonFile(new File(args[0]));

                ideArgBool = true;
            }
            if (!ideArgBool) {
                //  retrieve file from resource folder                readJSON();
                URL resource = TollCalculator.class.getClassLoader().getResource("interchanges.json");
                Path paths = Paths.get(resource.toURI()).toAbsolutePath();

                readJsonFile(paths.toFile());
            }
        } catch (Exception e) {
            System.out.println("Program cannot be run with no args");
        }
    }

    private static void readJsonFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
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
        Object elem = jsonObj.get(element);
        JSONHandler jsonHandler = new JSONHandler();
        jsonHandler.parseJSONObject(jsonObj);
        Map<Integer, Object> handlerLocationsMap = jsonHandler.getLocationsMap();

        System.out.println(handlerLocationsMap);
    }

}