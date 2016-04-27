package com.itmindco.dlnaplayervr.Models;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

public class VideoListItem {
    public String url;
    public String title;
    public String details;
    public TypeListItem type;
    public Device device;
    public Service service;
    private DIDLObject item;

    private int icon;

    public static VideoListItem createForDevice(Device device) {
        VideoListItem temp = new VideoListItem(String.valueOf(device.hashCode()), "", "", TypeListItem.DEVICE);
        temp.device = device;
        String name =
                device.getDetails() != null
                        && device.getDetails().getFriendlyName() != null
                        ? device.getDetails().getFriendlyName()
                        : device.getDisplayString();

        temp.title = device.isFullyHydrated() ? name : name + " *";
        return temp;
    }

    public static VideoListItem createForItem(Service service, DIDLObject item) {
        VideoListItem temp = new VideoListItem("", item.getTitle(), "", TypeListItem.ITEM);
        String url = "N/A";

        Res resource = item.getFirstResource();
        if (resource != null && resource.getValue() != null) {
            url = resource.getValue();
        }

        temp.url = url;
        temp.item = item;
        temp.service = service;
        if (item instanceof Container) {
            temp.type = TypeListItem.DIRECTORY;
        }

        return temp;
    }

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

    public Service getContentDirectory() {
        if (type == TypeListItem.DEVICE) {
            for (Service current : this.device.getServices())
                if (current.getServiceType().getType().equals("ContentDirectory"))
                    return current;
        }

        return null;
    }

    public Item getItem() {
        if (type == TypeListItem.DIRECTORY)
            return null;

        return (Item) item;
    }

    public Container getContainer() {
        if (type != TypeListItem.DIRECTORY)
            return null;

        return (Container) item;
    }

    public String getId() {
        return item.getId();
    }

    public enum TypeListItem{
        DIRECTORY,
        ITEM,
        DEVICE,
        LOCALCONTENT,
        ROOT
    }
}
