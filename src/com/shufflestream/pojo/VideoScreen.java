package com.shufflestream.pojo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.UUID;


import org.joda.time.DateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

/**
 * Created by dylan on 4/10/15.
 * core video screen object associated to a user, they may have one or many oif these
 * the
 */
public class VideoScreen implements Serializable {

    private int Id = 0;
    private String Title = "";
    private int ParentId = 0;
    private String Manufacturer = "";
    private String Model = "";
    private String UUID = "";

    /**
     *  list of supported aspect ratios for the screen
     */
    private List<AspectRatio> AspectRatios = new ArrayList<AspectRatio>();


    /**
     *  list of supported  Densities for the screen
     */
    private List<ScreenDensity> ScreenDensities = new ArrayList<ScreenDensity>();


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public List<AspectRatio> getAspectRatios() {
        return AspectRatios;
    }

    public void setAspectRatios(List<AspectRatio> aspectRatios) {
        AspectRatios = aspectRatios;
    }

    public List<ScreenDensity> getScreenDensities() {
        return ScreenDensities;
    }

    public void setScreenDensities(List<ScreenDensity> screenDensities) {
        ScreenDensities = screenDensities;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
