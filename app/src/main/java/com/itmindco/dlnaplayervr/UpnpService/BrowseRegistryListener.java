package com.itmindco.dlnaplayervr.UpnpService;

import com.itmindco.dlnaplayervr.Models.DeviceModel;
import com.itmindco.dlnaplayervr.R;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

public class BrowseRegistryListener extends DefaultRegistryListener {
    ContentDirectoryBrowse.Callbacks mCallbacks;

    public void setCallbacksListener(ContentDirectoryBrowse.Callbacks mCallbacks){
        this.mCallbacks = mCallbacks;
    }

    public void removeCallbacksListener(){
        this.mCallbacks = null;
    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {
        deviceRemoved(device);
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        deviceRemoved(device);
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        deviceAdded(device);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        deviceRemoved(device);
    }

    public void deviceAdded(Device device) {

        DeviceModel deviceModel = new DeviceModel(R.drawable.ic_device, device);

        Service conDir = deviceModel.getContentDirectory();
        if (conDir != null) {
                if (mCallbacks != null)
                    mCallbacks.onDeviceAdded(deviceModel);
        }
    }

    public void deviceRemoved(Device device) {
        if (mCallbacks != null)
            mCallbacks.onDeviceRemoved(new DeviceModel(R.drawable.ic_device, device));
    }
}
