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

    public ColumnData getColumnById(int uid){
        return this.columns.get(uid);
    }

    public static class ColumnData {
        private int uid;
        private String name;
        private List<String> jsonkeys;

        public void setJsonkeys(List<String> jsonkeys) {
            this.jsonkeys = jsonkeys;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public List<String> getJsonkeys() {
            return jsonkeys;
        }
    }
}
