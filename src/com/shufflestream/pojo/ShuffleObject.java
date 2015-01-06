package com.shufflestream.pojo;

import java.io.Serializable;

public class ShuffleObject implements Serializable {

    private static final long serialVersionUID = -6285248026080342930L;

    @Override
    public String toString() {
        return "ShuffleObject [AssetUrl=" + AssetUrl + ", Title=" + Title + ", Artist=" + Artist + ", Description=" + Description
                + ", ArtistWebsite=" + ArtistWebsite + Channel + "]";
    }

    private String AssetUrl = "";
    private String Title = "";
    private String Artist = "";
    private String Description = "";
    private String ArtistWebsite = "";
    private String Channel = "";

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

}
