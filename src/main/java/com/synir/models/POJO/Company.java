package com.synir.models.POJO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Company {

    private StringProperty name;

    public Company(String name){
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

}