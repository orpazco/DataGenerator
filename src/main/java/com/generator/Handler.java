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

    public String handleEvent(String jsonEvent, Config config) throws Exception{
        JSONObject jsonObject = new JSONObject(jsonEvent);
        if (jsonObject.has("data"))
            return buildTable(jsonObject.getJSONArray("data"), config);
        else return buildTable(jsonObject, config);
    }

    private String buildTable(JSONArray jsonEvent, Config config) throws Exception {
        JSONArray table = new JSONArray();
        for (int i = 0; i < jsonEvent.length(); i++){
            JSONObject jsonObject = jsonEvent.getJSONObject(i);
            table.put(buildTableRow(jsonObject, config));
        }
        return table.toString();
    }

    private String buildTable(JSONObject jsonobj, Config config) throws Exception {
        return buildTableRow(jsonobj, config).toString();
    }

    private JSONObject buildTableRow(JSONObject jsonEvent, Config config) throws Exception {
        JSONObject item = new JSONObject();
        setJsonOrder(item);
        TableStruct tableStruct = config.getTable();
        for (int i = 1; i <= tableStruct.getColumns().size(); i++){
            TableStruct.ColumnData columnData = tableStruct.getColumnById(i);
            String value = getPropertyValue(jsonEvent, config.getJson(), columnData.getJsonkeys());
            item.put(columnData.getName(), value);
        }
        return item;
    }

    // find the wanted value from the json file
    private String getPropertyValue(JSONObject jsonObj, JsonProp jsonConfig, List<String> jsonKeys) throws Exception {
        StringJoiner joiner = new StringJoiner(" ");
        for (String key: jsonKeys){
            PropData propData = jsonConfig.getDataByName(key);
            String path = getPath(key, propData, jsonConfig);
            joiner.add(JsonPath.read(jsonObj.toString(), path).toString());
        }
        return joiner.toString();
    }

    private String getPath(String key, PropData propData, JsonProp jsonConfig) throws Exception {
        if (propData.getParent().equals("none")) return key;
        else {
            // build property path by the config file
            List<String> path = new ArrayList<>();
            do {
                StringBuilder builder = new StringBuilder();
                builder.append(propData.getParent());
                if (propData.getIndex() != null)
                    builder.append("[").append(propData.getIndex()).append("]");
                path.add(0, builder.toString());
                propData = jsonConfig.getDataByName(propData.getParent());
            } while (!propData.getParent().equals("none"));
            path.add(key);
            return String.join(".", path);
        }
    }

    private void setJsonOrder(JSONObject jsonObject) throws NoSuchFieldException, IllegalAccessException {
        Field changeMap = jsonObject.getClass().getDeclaredField("map");
        changeMap.setAccessible(true);
        changeMap.set(jsonObject, new LinkedHashMap<>());
    }

}
