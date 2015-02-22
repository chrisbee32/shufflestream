package com.shufflestream.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class ShuffleObject implements Serializable, Comparable<ShuffleObject> {

    private static final long serialVersionUID = -6285248026080342930L;

    @Override
    public String toString() {
        return "ShuffleObject [Id=" + Id + ", AssetUrl_orig=" + AssetUrl_orig + ", AssetUrl_thum=" + AssetUrl_thumb + ", AssetUrl_med="
                + AssetUrl_med
                + ", AssetUrl_large=" + AssetUrl_large + ", Title=" + Title + ", Artist=" + Artist + ", Description=" + Description
                + ", ArtistWebsite=" + ArtistWebsite + ", Channel=" + Channel + ", CreatedDate=" + CreatedDate + ", UpdatedDate=" + UpdatedDate
                + ", Active=" + Active + ", Attributes=" + Attributes + "]";
    }

    private int Id = 0;
    private String AssetUrl_orig = "";
    private String AssetUrl_thumb = "";
    private String AssetUrl_med = "";
    private String AssetUrl_large = "";
    private String Title = "";
    private String Artist = "";
    private String Description = "";
    private String ArtistWebsite = "";
    private String Channel = "";
    private DateTime CreatedDate = DateTime.now();
    private DateTime UpdatedDate = DateTime.now();
    private boolean Active = true;
    private int SortOrderInChannel = 0;
    private Map<String, String> Attributes = new HashMap<String, String>();

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAssetUrl_orig() {
        return AssetUrl_orig;
    }

    public void setAssetUrl_orig(String assetUrl_orig) {
        AssetUrl_orig = assetUrl_orig;
    }

    public String getAssetUrl_thumb() {
        return AssetUrl_thumb;
    }

    public void setAssetUrl_thumb(String assetUrl_thumb) {
        AssetUrl_thumb = assetUrl_thumb;
    }

    public String getAssetUrl_med() {
        return AssetUrl_med;
    }

    public void setAssetUrl_med(String assetUrl_med) {
        AssetUrl_med = assetUrl_med;
    }

    public String getAssetUrl_large() {
        return AssetUrl_large;
    }

    public void setAssetUrl_large(String assetUrl_large) {
        AssetUrl_large = assetUrl_large;
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

    public DateTime getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        CreatedDate = createdDate;
    }

    public DateTime getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(DateTime updatedDate) {
        UpdatedDate = updatedDate;
    }

    public boolean getActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public Map<String, String> getAttributes() {
        return Attributes;
    }

    public void setAttributes(Map<String, String> value) {
        Attributes = value;
    }

    public int getSortOrderInChannel() {
        return SortOrderInChannel;
    }

    public void setSortOrderInChannel(int sortOrderInChannel) {
        SortOrderInChannel = sortOrderInChannel;
    }

    @Override
    public int compareTo(ShuffleObject o) {
        int sorted = this.SortOrderInChannel - o.getSortOrderInChannel();
        return sorted;
    }

}
