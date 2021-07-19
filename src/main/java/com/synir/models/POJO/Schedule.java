package com.synir.models.POJO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class Schedule {

    private StringProperty name;
    private int maxdays;

    private ArrayList<Timeblock> timeblocks;

    public Schedule(String name){
        this(name,31);
    }

    public Schedule(String name, int maxdays){
        this.name = new SimpleStringProperty(name);
        this.maxdays = maxdays;
        timeblocks = new ArrayList<>();
        timeblocks.add(new Timeblock("00:00-\n08:00"));
        timeblocks.add(new Timeblock("08:00-\n09:00"));
        timeblocks.add(new Timeblock("09:00-\n10:00"));
        timeblocks.add(new Timeblock("10:00-\n11:00"));
        timeblocks.add(new Timeblock("11:00-\n12:00"));
        timeblocks.add(new Timeblock("12:00-\n13:00"));
        timeblocks.add(new Timeblock("13:00-\n14:00"));
        timeblocks.add(new Timeblock("14:00-\n15:00"));
        timeblocks.add(new Timeblock("15:00-\n16:00"));
        timeblocks.add(new Timeblock("16:00-\n17:00"));
        timeblocks.add(new Timeblock("17:00-\n18:00"));
        timeblocks.add(new Timeblock("18:00-\n23:59"));
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

    public ArrayList<Timeblock> getTimeblocks() {
        return timeblocks;
    }

    public void setTimeblocks(ArrayList<Timeblock> timeblocks) {
        this.timeblocks = timeblocks;
    }

    public Timeblock getSingleTimeBlock(int index){
        return timeblocks.get(index);
    }

    public void addSingle(int x, String location){
        timeblocks.get(x).addLocationElement(location);
    }
}