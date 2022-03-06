package com.handler;

import com.configuration.Config;
import com.configuration.JsonProp;
import com.configuration.PropData;
import com.configuration.TableStruct;
import com.generator.Handler;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class HandlerTests {

    // region columns
    private static final String c1 = "id_test";
    private static final String c2 = "name_test";
    private static final String c3 = "mfa_test";
    private static final String c4 = "name_and_role";
    // endregion

    //region file paths
    private static String jsonsPath = "src/test/java/jsons/";
    private static String json1 = "jsonTest.json";
    private static String json2 = "jsonTest2.json";
    private static String json3 = "jsonTest3.json";
    private static String jsonArray = "jsonArrayTest.json";
    //endregion

    private static Config config;
    private static Handler handler;

    @BeforeClass
    public static void setUp(){
        config = createConfigFile();
        handler = new Handler();
    }

    @Test
    public void testTableRow1(){
        assertJsonRow(json1);
    }

    @Test
    public void testTableRow2(){
        assertJsonRow(json2);
    }

    @Test
    public void testTableRow3(){
        assertJsonRow(json3);
    }

    @Test
    public void testTableArray(){
        try {
            String jsonFileTest = loadJsonFile(jsonArray);
            String result = handler.handleEvent(jsonFileTest, config);
            String expected = createTableJson(new JSONObject(jsonFileTest).getJSONArray("data"));
            Assert.assertTrue("the returned table does not match the expected table." +
                    " actual: " + result +"\nexpcted: " + expected, result.equals(expected));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private void assertJsonRow(String jsonFile){
        try {
            String jsonFileTest = loadJsonFile(jsonFile);
            String result = handler.handleEvent(jsonFileTest, config);
            String expected = createTableRowJson(jsonFileTest).toString();
            Assert.assertTrue("the returned table does not match the expected table." +
                    " actual: " + result +"\nexpcted: " + expected, result.equals(expected));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private String loadJsonFile(String jsonFilePath) throws IOException, JSONException {
        String content = new String(Files.readAllBytes(Paths.get(jsonsPath + jsonFilePath)));
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.toString();
    }

    //region init config file
    private static Config createConfigFile(){
        Config config = new Config();
        // set table struct
        TableStruct tableStruct = new TableStruct();
        Map<Integer, TableStruct.ColumnData> columnDataMap = new HashMap<>();
        columnDataMap.put(1, new TableStruct.ColumnData(c1, new ArrayList<String>(){{add(propEnum.id.getName());}}));
        columnDataMap.put(2, new TableStruct.ColumnData(c2, new ArrayList<String>(){{
            add(propEnum.firstName.getName()); add(propEnum.lastName.getName());}}));
        columnDataMap.put(3, new TableStruct.ColumnData(c3, new ArrayList<String>(){{add(propEnum.mfa.getName());}}));
        columnDataMap.put(4, new TableStruct.ColumnData(c4, new ArrayList<String>(){{
            add(propEnum.loginName.getName());add(propEnum.role.getName());}}));
        tableStruct.setColumns(columnDataMap);
        config.setTable(tableStruct);
        // set json properties
        JsonProp jsonProp = new JsonProp();
        Map<String, PropData> propertiesMap = new HashMap<>();
        propertiesMap.put(propEnum.id.getName(), new PropData(propEnum.userDetails.getName()));
        propertiesMap.put(propEnum.firstName.getName(), new PropData(propEnum.userDetails.getName()));
        propertiesMap.put(propEnum.lastName.getName(), new PropData(propEnum.userDetails.getName()));
        propertiesMap.put(propEnum.mfa.getName(), new PropData(propEnum.security.getName()));
        propertiesMap.put(propEnum.loginName.getName(), new PropData(propEnum.data.getName(), "0"));
        propertiesMap.put(propEnum.role.getName(), new PropData(propEnum.data.getName(), "0"));
        propertiesMap.put(propEnum.security.getName(), new PropData(propEnum.data.getName(), "0"));
        propertiesMap.put(propEnum.userDetails.getName(), new PropData(propEnum.data.getName(), "0"));
        propertiesMap.put(propEnum.data.getName(), new PropData(propEnum.none.getName()));
        jsonProp.setProperties(propertiesMap);
        config.setJson(jsonProp);
        return config;
    }

    private enum propEnum {
        id ("id"),
        firstName ("first_name"),
        lastName ("last_name"),
        mfa ("mfa_enabled"),
        loginName ("login_name"),
        role ("role"),
        userDetails ("user-details"),
        security ("security"),
        data ("evidence_data"),
        none ("none");

        private final String name;
        private propEnum(String name){
            this.name = name;
        }
        
        private String getName(){
            return this.name;
        }
    }

    //endregion

    private static JSONObject createTableRowJson(String jsonTest) throws JSONException {
        JSONObject tableRow = new JSONObject();
        tableRow.put(c1, JsonPath.read(jsonTest, "evidence_data[0].user-details.id").toString());
        String name = JsonPath.read(jsonTest, "evidence_data[0].user-details.first_name") + " " +
                JsonPath.read(jsonTest, "evidence_data[0].user-details.last_name");
        tableRow.put(c2, name);
        tableRow.put(c3, JsonPath.read(jsonTest, "evidence_data[0].security.mfa_enabled").toString());
        String nameAndRole = JsonPath.read(jsonTest, "evidence_data[0].login_name") + " " +
                JsonPath.read(jsonTest, "evidence_data[0].role");
        tableRow.put(c4, nameAndRole);
        return tableRow;
    }

    private static String createTableJson(JSONArray jsonTest) throws JSONException {
        JSONArray table = new JSONArray();
        // iterate on all the objects received anf build the appropriate table row
        for (int i = 0; i < jsonTest.length(); i++){
            JSONObject jsonObject = jsonTest.getJSONObject(i);
            table.put(createTableRowJson(jsonObject.toString()));
        }
        return table.toString();
    }
}
