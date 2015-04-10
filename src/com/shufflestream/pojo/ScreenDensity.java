package com.shufflestream.pojo;

import java.io.Serializable;

/**
 * Created by dylan on 4/10/15.
 * this provides the resolution at a particular aspect ratio of the video screen
 */
public class ScreenDensity implements Serializable {


    private int Id = 0;
    private String Title = "";  // 1080i , 4k , etc
    private int Width = 0;
    private int Height = 0;
    private int PPI = 0;  //  number of points pixels per inch in diplay
    private boolean IsRetina ;  // future use
    private boolean IsProjector ; // future use


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getPPI() {
        return PPI;
    }

    public void setPPI(int PPI) {
        this.PPI = PPI;
    }

    public boolean isRetina() {
        return IsRetina;
    }

    public void setIsRetina(boolean isRetina) {
        IsRetina = isRetina;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isProjector() {
        return IsProjector;
    }

    public void setIsProjector(boolean isProjector) {
        IsProjector = isProjector;
    }
}
