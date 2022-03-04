package com.configuration;

import java.util.Map;

public class JsonProp {
    private Map<String, PropData> properties;

    public void setProperties(Map<String, PropData> properties) {
        this.properties = properties;
    }

    public Map<String, PropData> getProperties() {
        return properties;
    }

    public PropData getDataByName(String name) throws NoSuchFieldException{
        if (properties.containsKey(name))
            return properties.get(name);
        else throw new NoSuchFieldException("Could not find " + name + " field in json properties config file");
    }
}
