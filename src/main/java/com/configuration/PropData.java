package com.configuration;

public class PropData {

    private String parent;
    private String index;

    public PropData(){}

    public PropData(String parent, String index){
        this.parent = parent;
        this.index = index;
    }

    public PropData(String parent){
        this.parent = parent;
    }

    public void setParent(String path) {
        this.parent = path;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }

    public String getParent() {
        return parent;
    }
}


