package com.itmindco.dlnaplayervr.Models;

public class VideoListItem {
    public final String url;
    public final String title;
    public final String details;
    public final TypeListItem type;

    private int icon;

    public VideoListItem(String url, String title, String details, TypeListItem type) {
        this.url = url;
        this.title = title;
        this.details = details;
        this.type = type;
    }

    @Override
    public String toString() {
        return title;
    }

    public enum TypeListItem{
        DIRECTORY,
        ITEM,
        DEVICE,
        LOCALCONTENT,
        ROOT
    }
}
