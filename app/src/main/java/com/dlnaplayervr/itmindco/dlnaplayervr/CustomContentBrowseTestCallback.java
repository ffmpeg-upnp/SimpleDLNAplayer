package com.dlnaplayervr.itmindco.dlnaplayervr;


import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.SortCriterion;

public class CustomContentBrowseTestCallback extends Browse {
    private Device device;
    private Service service;
    BrowseRegistryListener registryListener;

    public CustomContentBrowseTestCallback(Device device, Service service,BrowseRegistryListener registryListener) {
        super(service, "0", BrowseFlag.DIRECT_CHILDREN, "*", 0, null,
                new SortCriterion(true, "dc:title"));

        this.device = device;
        this.service = service;
        this.registryListener = registryListener;
    }

    @Override
    public void received(final ActionInvocation actionInvocation, final DIDLContent didl) {
        if (registryListener != null)
            registryListener.deviceAdded(device);
    }

    @Override
    public void updateStatus(Status status) {

    }

    @Override
    public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {

    }
}

