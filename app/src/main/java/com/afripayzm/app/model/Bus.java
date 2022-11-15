package com.afripayzm.app.model;

import java.util.HashMap;
import java.util.Map;

public class Bus {
    private String name;/*
    private String plate;
    private String color;*/

    public Map toMap(){
        Map map=new HashMap();
        map.put("name",name);/*
        map.put("plate",plate);
        map.put("color",color);*/
        return map;
    }
}
