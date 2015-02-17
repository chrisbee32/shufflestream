package com.shufflestream.pojo;

import java.io.Serializable;
import java.util.HashMap;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

public class ShuffleObject implements Serializable {

    private static final long serialVersionUID = -6285248026080342930L;

    @Override
    public String toString() {
        return "ShuffleObject [Id=" + Id + "AssetUrl=" + AssetUrl + ", Title=" + Title + ", Artist=" + Artist + ", Description=" + Description
                + ", ArtistWebsite=" + ArtistWebsite + Channel + "]";
    }

    private String Id = "";
    private String AssetUrl = "";
    private String Title = "";
    private String Artist = "";
    private String Description = "";
    private String ArtistWebsite = "";
    private String Channel = "";
    private String Active = "";
    private HashMap<String, String> Attributes = new HashMap<String, String>();

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAssetUrl() {
        return AssetUrl;
    }

    public void setAssetUrl(String assetUrl) {
        AssetUrl = assetUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getArtistWebsite() {
        return ArtistWebsite;
    }

    public void setArtistWebsite(String artistWebsite) {
        ArtistWebsite = artistWebsite;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public HashMap<String, String> getAttributes() {
        return Attributes;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        Attributes = attributes;
    }

}
