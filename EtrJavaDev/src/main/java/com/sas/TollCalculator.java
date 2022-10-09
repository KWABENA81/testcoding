package com.sas;

import com.sas.core.JSONHandler;
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
import java.util.Scanner;

public class TollCalculator {
    private static Map<Integer, Object> handlerLocationsMap;

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
            menuPrompt();
        } catch (Exception e) {

            printout("Program cannot be run with no args");
        }
    }

    private static void menuPrompt() throws IOException {
        long pid = 0l;
        Scanner inputScanner=new Scanner(System.in);
        while (true) {
            printout("Please enter Trip start location & destination:\n(Comma Separated)");
            String inputString = inputScanner.nextLine();
            printout(inputString);
            pid = checkpid();
        }
    }

    private static long checkpid() throws IOException {
        long pid = 0l;//process.pid();
        Runtime.getRuntime().exec("kill " + pid);
        return pid;
    }

    private static void printout(String string) {
        System.out.println(string);
    }

    private static void readJsonFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            jsonObject.keySet().iterator().forEachRemaining(element -> {
                handle(jsonObject);
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void handle(JSONObject jsonObj) {
        //
        JSONHandler jsonHandler = new JSONHandler();
        jsonHandler.parseJSONObject(jsonObj);
        handlerLocationsMap = jsonHandler.getLocationsMap();
    }

}