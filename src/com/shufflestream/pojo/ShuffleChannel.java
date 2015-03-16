package com.shufflestream.pojo;

import java.io.Serializable;
import org.joda.time.DateTime;

public class ShuffleChannel implements Serializable {

    private static final long serialVersionUID = -6285248026080342930L;

    private int Id = 0;
    private String ChannelName = "";
    private String Description = "";
    private String ThumbnailUrl = "";
    private int TotalContent = 0;
    private DateTime CreatedDate = DateTime.now();
    private DateTime UpdatedDate = DateTime.now();
    private boolean Active = true;

    @Override
    public String toString() {
        return "ShuffleChannel [Id=" + Id + ", ChannelName=" + ChannelName + ", Description=" + Description + ", ThumbnailUrl=" + ThumbnailUrl
                + ", TotalContent=" + TotalContent + ", CreatedDate=" + CreatedDate + ", UpdatedDate=" + UpdatedDate + ", Active="
                + Active + "]";
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getThumbnailUrl() {
        return ThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        ThumbnailUrl = thumbnailUrl;
    }

    public int getTotalContent() {
        return TotalContent;
    }

    public void setTotalContent(int totalContent) {
        TotalContent = totalContent;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
