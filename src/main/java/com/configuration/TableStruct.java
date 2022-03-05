package com.configuration;

import java.util.List;
import java.util.Map;

public class TableStruct {

    private Map<Integer, ColumnData> columns;

    public void setColumns(Map<Integer, ColumnData> columns) {
        this.columns = columns;
    }

    public Map<Integer, ColumnData> getColumns() {
        return columns;
    }

    public ColumnData getColumnById(int id){
        return this.columns.get(id);
    }

    public static class ColumnData {
        private String name;
        private List<String> jsonkeys;

        public ColumnData(){}

        public ColumnData(String name, List<String> jsonkeys){
            this.name = name;
            this.jsonkeys = jsonkeys;
        }

        public void setJsonkeys(List<String> jsonkeys) {
            this.jsonkeys = jsonkeys;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<String> getJsonkeys() {
            return jsonkeys;
        }
    }
}
