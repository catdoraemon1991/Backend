package com.laioffer.entity;

import java.lang.reflect.Method;
import java.util.List;

public class Station {
    private String stationName;
    private List<Method> methods;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }
}
