package org.example.config;

public class Resolution {
    private String label;
    private int width;
    private int height;
    private int sidePanelWidth;   // ← اضافه شد

    // getters & setters

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getSidePanelWidth() {
        return sidePanelWidth;
    }
    public void setSidePanelWidth(int sidePanelWidth) {
        this.sidePanelWidth = sidePanelWidth;
    }
}
