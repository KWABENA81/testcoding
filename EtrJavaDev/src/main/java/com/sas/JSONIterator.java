package com.sas;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class JSONIterator {
    private Map<Integer, Object> locationsMap;

    public JSONIterator() {
        locationsMap = new TreeMap<>();
    }

    public void parseJSONObject(JSONObject jsonObj) {
        Iterator<String> iterator = jsonObj.keySet().iterator();
        iterator.forEachRemaining(key -> {
            parseValues(key, jsonObj.get(key));
        });
    }

    private void parseValues(String key, Object value) {
        if (value instanceof JSONArray) {
           
            parseJSONArray(key, (JSONArray) value);
        } else if (value instanceof JSONObject) {
            System.out.println(key+"\tparseValues "+value);
            parseJSONObject((JSONObject) value);
        }
        try {
            Integer locationId = Integer.parseInt(key);
            locationsMap.put(locationId, value);
        } catch (NumberFormatException ex) {
            //ex.printStackTrace();
        }
    }

    private void parseJSONArray(String key, JSONArray jsonArray) {
        Iterator<Object> arrayIterator = jsonArray.iterator();
        arrayIterator.forEachRemaining(elem -> {
            //System.out.println(key+"\tparseJSONArray "+elem);
            parseValues(key, elem);
        });
    }

    public Map<Integer, Object> getLocationsMap() {
        return locationsMap;
    }
}
