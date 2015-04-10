package com.shufflestream.pojo;

/**
 * Created by dylan on 4/10/15.
 */
public class ShuffleObjectDimensionsCache {

    private int Id = 0;
    private String UUID = "";

    private ScreenDensity ScrDensity = new ScreenDensity();
    private ViewBoundary ViewBounds = new ViewBoundary();


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public ScreenDensity getScrDensity() {
        return ScrDensity;
    }

    public void setScrDensity(ScreenDensity scrDensity) {
        ScrDensity = scrDensity;
    }

    public ViewBoundary getViewBounds() {
        return ViewBounds;
    }

    public void setViewBounds(ViewBoundary viewBounds) {
        ViewBounds = viewBounds;
    }
}
