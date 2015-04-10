package com.shufflestream.pojo;

import java.io.Serializable;

/**
 * Created by dylan on 4/10/15.
 */
public class AspectRatio implements Serializable {

    private int Id = 0;
    private String AspectRatio = "1:1";
    private int antecedent = 0;
    private int consequent = 0;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAspectRatio() {
        return AspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        AspectRatio = aspectRatio;
    }

    public int getAntecedent() {
        return antecedent;
    }

    public void setAntecedent(int antecedent) {
        this.antecedent = antecedent;
    }

    public int getConsequent() {
        return consequent;
    }

    public void setConsequent(int consequent) {
        this.consequent = consequent;
    }

}
