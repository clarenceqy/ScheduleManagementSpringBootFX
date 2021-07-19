package com.synir.models.POJO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class Timeblock {

    //time name i.e, 00:00-8:00
    private StringProperty timename;
    private ArrayList<StringProperty> location;

    public Timeblock(String timename){
       this(timename,31);
    }

    public Timeblock(String timename,int maxdays){
        this.timename = new SimpleStringProperty(timename);
        location = new ArrayList<>();
        for(int i = 0; i < maxdays; i++){
            location.add(new SimpleStringProperty("/"));
        }
    }


    public String getTimename() {
        return timename.get();
    }

    public StringProperty timenameProperty() {
        return timename;
    }

    public void setTimename(String timename) {
        this.timename.set(timename);
    }

    public ArrayList<StringProperty> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<StringProperty> location) {
        this.location = location;
    }

    public void addLocationElement(String locationname){
        location.add(new SimpleStringProperty(locationname));
    }

    public void setLocationElement(String locationname, int index){
        location.set(index,new SimpleStringProperty(locationname));
    }

    public StringProperty getLocationElement(int index){
        return location.get(index);
    }
}