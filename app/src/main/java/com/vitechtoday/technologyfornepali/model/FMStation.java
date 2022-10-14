package com.vitechtoday.technologyfornepali.model;

public class FMStation {

    String fmName;
    String fmURL;

    public FMStation() {
    }

    public FMStation(String fmName, String fmURL) {
        this.fmName = fmName;
        this.fmURL = fmURL;
    }

    public String getFmName() {
        return fmName;
    }

    public void setFmName(String fmName) {
        this.fmName = fmName;
    }

    public String getFmURL() {
        return fmURL;
    }

    public void setFmURL(String fmURL) {
        this.fmURL = fmURL;
    }

}
