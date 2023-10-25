package org.apexlegendsbphelper;

public class Legend {
    private int id;
    private String name;
    private short legendClass;
    private boolean isFavourite = false;

    public int getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getLegendClass() {
        return legendClass;
    }

    public void setLegendClass(short legendClass) {
        this.legendClass = legendClass;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
