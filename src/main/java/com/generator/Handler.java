package com.generator;

import com.configuration.Config;
import com.configuration.JsonProp;
import com.configuration.PropData;
import com.configuration.TableStruct;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;

@SpringBootApplication
public class Handler {

    /***
     * receive the json event from the server, build the response table and return it as string
     * @param jsonEvent event received from the server
     * @param config configuration file
     * @return table in json format
     */
    public String handleEvent(String jsonEvent, Config config) throws Exception{
        JSONObject jsonObject = new JSONObject(jsonEvent);
        // check if the json event contains array of object or only one object
        if (jsonObject.has("data"))
            return buildTable(jsonObject.getJSONArray("data"), config);
        else return buildTable(jsonObject, config);
    }

    private String buildTable(JSONArray jsonEvent, Config config) throws Exception {
        // build the table as json array object
        JSONArray table = new JSONArray();
        // iterate on all the objects received anf build the appropriate table row
        for (int i = 0; i < jsonEvent.length(); i++){
            JSONObject jsonObject = jsonEvent.getJSONObject(i);
            table.put(buildTableRow(jsonObject, config));
        }
        return table.toString();
    }

    private String buildTable(JSONObject jsonobj, Config config) throws Exception {
        // build one table row and return it as string
        return buildTableRow(jsonobj, config).toString();
    }

    private JSONObject buildTableRow(JSONObject jsonEvent, Config config) throws Exception {
        // create json object who represent the row matches to the current object
        JSONObject item = new JSONObject();
        // set json object fields so the columns will maintain order
        setJsonOrder(item);
        // get table representation object from the config file
        TableStruct tableStruct = config.getTable();
        // go through all columns in the table to add the value from the json file
        for (int i = 1; i <= tableStruct.getColumns().size(); i++){
            // get the column data object
            TableStruct.ColumnData columnData = tableStruct.getColumnById(i);
            // search for the property value in the receiving json event
            String value = getPropertyValue(jsonEvent, config.getJson(), columnData.getJsonkeys());
            // add the property to table
            item.put(columnData.getName(), value);
        }
        return item;
    }

    // find the property value from the json file according to the configuration file
    private String getPropertyValue(JSONObject jsonObj, JsonProp property, List<String> jsonKeys) throws Exception {
        // add delimiter in case there is more than one field to add
        StringJoiner joiner = new StringJoiner(" ");
        // go through all json keys to find them in the json file
        for (String key: jsonKeys){
            // get the property data
            PropData propData = property.getDataByName(key);
            // build the property path according to configuration
            String path = getPath(key, propData, property);
            // get the property value according to the path that was built
            String value = JsonPath.read(jsonObj.toString(), path).toString();
            joiner.add(value);
        }
        return joiner.toString();
    }

    /***
     * build the property path in the json file, so it will be possible to get the value
     * @param key the property name
     * @param propData the current property data
     * @param property configuration file
     * @return the path to the property value in the json
     */
    private String getPath(String key, PropData propData, JsonProp property) throws Exception {
        String noneStr = "none";
        String delimiter = ".";
        // the property is in the first level of the json
        if (propData.getParent().equals(noneStr)) return key;
        else {
            // build property path by the config file, add every level to the path list
            List<String> path = new ArrayList<>();
            do {
                // first add the parent of the property to path
                StringBuilder builder = new StringBuilder();
                builder.append(propData.getParent());
                // if the property is in array add the location
                if (propData.getIndex() != null)
                    builder.append("[").append(propData.getIndex()).append("]");
                path.add(0, builder.toString());
                // continue to the parent property of the current property
                propData = property.getDataByName(propData.getParent());
            } while (!propData.getParent().equals(noneStr));
            // add to the end of the path the name of the prop
            path.add(key);
            return String.join(delimiter, path);
        }
    }

    // set the json object fields so every item that will be added to ths object will be added to the end of it
    private void setJsonOrder(JSONObject jsonObject) throws NoSuchFieldException, IllegalAccessException {
        Field changeMap = jsonObject.getClass().getDeclaredField("map");
        changeMap.setAccessible(true);
        changeMap.set(jsonObject, new LinkedHashMap<>());
    }

}
