package com.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "structure")
// A structure for holding the configuration file, spring loads the configuration yaml file to this object
public class Config {
    private JsonProp json;
    private TableStruct table;

    public void setJson(JsonProp json) {
        this.json = json;
    }

    public void setTable(TableStruct table) {
        this.table = table;
    }

    public JsonProp getJson() {
        return json;
    }

    public TableStruct getTable() {
        return table;
    }
}
