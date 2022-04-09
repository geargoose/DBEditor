package com.egorzaev.dbeditor;

class DbModel {
    String name;
    String path;
    String type;
    String table;

    public DbModel(String name, String address, String type, String table) {
        this.name = name;
        this.path = address;
        this.type = type;
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
