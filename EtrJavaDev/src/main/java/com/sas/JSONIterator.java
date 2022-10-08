package com.sas;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class JSONIterator {
    private Map<Integer, Object> objMap;

    public JSONIterator() {
        objMap = new TreeMap<>();
    }

    public void iterateJSONObject(JSONObject jsonObj) {
        Iterator<String> iterator = jsonObj.keySet().iterator();
        iterator.forEachRemaining(key -> {
            Object value = jsonObj.get(key);
            iterateValues(key, value);
        });
    }

    private boolean isNumericKey(String key) {
        if (key == null) return false;
        try {
            Integer nkey = Integer.parseInt(key);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private void iterateValues(String key, Object value) {
        if (value instanceof JSONArray) {
            iterateJSONArray(key, (JSONArray) value);
        } else if (value instanceof JSONObject) {
            iterateJSONObject((JSONObject) value);
        }
        if (isNumericKey(key)) {
            Integer nkey = Integer.parseInt(key);
            System.out.println(value);
            objMap.put(nkey, value);
        }
    }

    private void iterateJSONArray(String key, JSONArray jsonArray) {
        Iterator<Object> arrayIterator = jsonArray.iterator();
        arrayIterator.forEachRemaining(elem -> {
            iterateValues(key, elem);
        });
    }

    public Map<Integer, Object> getObjMap() {
        return objMap;
    }
}
