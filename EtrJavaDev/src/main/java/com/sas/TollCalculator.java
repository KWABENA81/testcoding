package com.sas;

import com.sas.core.JSONHandler;
import com.sas.model.LocationNode;
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
import java.util.*;
import java.util.stream.Collectors;

public class TollCalculator {
    private static Map<Integer, Object> handlerLocationsMap;
    private static Set<LocationNode> locations;
    private static Scanner inputScanner;
    private static String exitLocationName;
    private static String startLocationName;

    public static void main(String... args) throws URISyntaxException, IOException {
        readLocations(args);
        inputScanner = new Scanner(System.in);
        menuPrompt();
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
                //  retrieve file from resource folder   
                URL resource = TollCalculator.class.getClassLoader().getResource("interchanges.json");
                Path paths = Paths.get(resource.toURI()).toAbsolutePath();

                readJsonFile(paths.toFile());
            }
        } catch (Exception e) {
            printout("Program cannot be run with no args");
        }
    }

    private static void menuPrompt() throws IOException {
        String str = "______________________\nToll calculator prompt.\n" + "Select one:\nN - Name Search:\n" + "S - Start Entrance:\nX - End Exit:\nF - Finish: \n";

        long pid = 0L;
        String options = "Exit";
        String inputString = null;

        boolean canExit = !options.equalsIgnoreCase("EXIT");

        while (!canExit) {
            printout(str);
            inputString = inputScanner.nextLine();
            // printout(inputString);
            switch (inputString) {
                case "N":
                case "n":
                    optionSearch();
                    break;
                case "X":
                case "x":
                    optionHwyExit();
                    break;
                case "S":
                case "s":
                    optionHwyStart();
                    break;
                case "F":
                case "f":
                    //canExit = true;
                    //exit(0);
            }
            //  pid = checkpid();

//                   List<String> slist = locations.stream().map(l -> l.getLocationName())
//                           .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
//            slist.forEach((l -> printout(l)));
            // printout(obj.toString());
            //;entrySet()){
            //obj=list.
            // }//stream().flatmap(LocationNode::getLocationName).
            // collect(Collectors.toList());
            canExit = false;
        }
    }

    private static void optionHwyStart() {
        printout("\nEnter HWY Start: ");
        String startString = inputScanner.nextLine();
        Optional<String> locationOptional = locations.stream().map(l -> l.getLocationName())
                .filter(s -> s.equalsIgnoreCase(startString)).findFirst();
        startLocationName = (locationOptional.isPresent()) ? locationOptional.get() : null;
        if (startLocationName == null) {
            optionSearch();
        } else {
            //printout(startLocationName);
            optionHwyExit();
        }
    }

    private static void optionSearch() {
        exitLocationName = null;
        startLocationName = null;
        printout("Enter Name to Search: ");
        String searchString = inputScanner.nextLine();
        if (searchString.length() < 3) return;
        List<String> slist = locations.stream().map(l -> l.getLocationName())
                .filter(s -> s.toLowerCase().contains(searchString.toLowerCase()))
                .sorted(Comparator.naturalOrder()).collect(Collectors.toList());

        if (slist.isEmpty()) {
            optionSearch();
        } else {
            slist.forEach(x -> printout(x));
            optionHwyStart();
        }
    }

    private static void optionHwyExit() {
        printout("\nEnter HWY Exit: ");
        String exitString = inputScanner.nextLine();
        Optional<String> locNode = locations.stream().map(l -> l.getLocationName())
                .filter(s -> s.equalsIgnoreCase(exitString)).findFirst();
        exitLocationName = (locNode.isPresent()) ? locNode.get() : null;
        if (exitLocationName == null) {
            optionHwyExit();
        } else {
            //printout(exitLocationName);
            optionTollCompute();
        }
    }

    private static void optionTollCompute() {
        printout("\nHwy Entrance: "+startLocationName);
        printout("Hwy Exit:\t"+exitLocationName);
        printout("Distance:\t");
        printout("Cost:\t\t");
    }

    private static long checkpid() throws IOException {
        long pid = 0L;//process.pid();
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
        locations = jsonHandler.getLocationNodes();
    }

}