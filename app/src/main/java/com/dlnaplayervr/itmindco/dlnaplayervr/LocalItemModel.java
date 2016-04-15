package com.dlnaplayervr.itmindco.dlnaplayervr;

public class LocalItemModel extends CustomListItem {

    private String Id;

    public LocalItemModel(int icon, String title, String description, String Id ) {
        super(icon, title, description);
        this.Id = Id;
    }

    public String getId() {
        return "";
    }


    public String getUrl() {

        return Id;
    }
}
