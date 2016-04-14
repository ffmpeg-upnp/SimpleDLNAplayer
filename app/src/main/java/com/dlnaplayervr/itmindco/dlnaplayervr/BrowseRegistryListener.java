package com.dlnaplayervr.itmindco.dlnaplayervr;

import android.widget.Toast;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

public class BrowseRegistryListener extends DefaultRegistryListener {
    /* Discovery performance optimization for very slow Android devices! */

    MainActivity mainActivity;

    public BrowseRegistryListener(MainActivity activity) {
        mainActivity = activity;
    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(
                        mainActivity,
                        "Discovery failed of '" + device.getDisplayString() + "': "
                                + (ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
        deviceRemoved(device);
    }
        /* End of optimization, you can remove the whole block if your Android handset is fast (>= 600 Mhz) */

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

    public void deviceAdded(final Device device) {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                DeviceDisplay d = new DeviceDisplay(device);
                //Log.i("DLNA",d.toString());
                int position = mainActivity.listAdapter.getPosition(d);
                if (position >= 0) {
                    // Device already in the list, re-set new value at same position
                    mainActivity.listAdapter.remove(d);
                    mainActivity.listAdapter.insert(d, position);
                } else {
                    mainActivity.listAdapter.add(d);
                }
            }
        });
    }

    public void deviceRemoved(final Device device) {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainActivity.listAdapter.remove(new DeviceDisplay(device));
            }
        });
    }
}
