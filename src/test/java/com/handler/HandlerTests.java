package com.handler;

import com.configuration.Config;
import com.configuration.JsonProp;
import com.configuration.PropData;
import com.configuration.TableStruct;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class HandlerTests {
    private String c1 = "id_test";
    private String c2 = "name_test";
    private String c3 = "mfa_test";
    private String c4 = "name_and_role";

    private Config config;

    public HandlerTests(){
        config = createConfigFile();
    }

    @Test
    private void test1(){

    }

    //region init config file
    private Config createConfigFile(){
        Config config = new Config();
        // set table struct
        TableStruct tableStruct = new TableStruct();
        Map<Integer, TableStruct.ColumnData> columnDataMap = new HashMap<>();
        columnDataMap.put(1, new TableStruct.ColumnData(c1, new ArrayList<String>(){{add(propEnum.id.getName());}}));
        columnDataMap.put(2, new TableStruct.ColumnData(c1, new ArrayList<String>(){{
            add(propEnum.firstName.getName()); add(propEnum.lastName.getName());}}));
        columnDataMap.put(3, new TableStruct.ColumnData(c1, new ArrayList<String>(){{add(propEnum.mfa.getName());}}));
        columnDataMap.put(4, new TableStruct.ColumnData(c1, new ArrayList<String>(){{
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
        data ("evidence_data");

        private final String name;
        private propEnum(String name){
            this.name = name;
        }
        
        private String getName(){
            return this.name;
        }
    }

    //endregion
}
