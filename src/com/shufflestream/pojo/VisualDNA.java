package com.shufflestream.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;


/**
 * Created by dylan on 4/10/15.
 */
public class VisualDNA implements Serializable, Comparable<VisualDNA> {


    private int Id = 0;
    private String UUID = "";
    private String Title = "";
    private int ParentId = 0;
    private int ScaleMin = 0;
    private int ScaleMax = 0;
    private int InterfaceOrder = 0;
    private String Description = "";
    private String Group = "";
    private boolean isTopDNA;
    private List<String> ScaleValues = new ArrayList<String>();


    public int getInterfaceOrder() {
        return InterfaceOrder;
    }

    public void setInterfaceOrder(int interfaceOrder) {
        InterfaceOrder = interfaceOrder;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }


    public int getScaleMin() {
        return ScaleMin;
    }

    public void setScaleMin(int scaleMin) {
        ScaleMin = scaleMin;
    }

    public int getScaleMax() {
        return ScaleMax;
    }

    public void setScaleMax(int scaleMax) {
        ScaleMax = scaleMax;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<String> getScaleValues() {
        return ScaleValues;
    }

    public void setScaleValues(List<String> scaleValues) {
        ScaleValues = scaleValues;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public boolean isTopDNA() {
        return isTopDNA;
    }

    public void setIsTopDNA(boolean isTopDNA) {
        this.isTopDNA = isTopDNA;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public int compareTo(VisualDNA o) {
        int sorted = this.InterfaceOrder = o.getInterfaceOrder();
        return sorted;
    }


}
