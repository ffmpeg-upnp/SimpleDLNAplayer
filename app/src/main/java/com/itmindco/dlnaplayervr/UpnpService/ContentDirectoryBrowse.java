package com.itmindco.dlnaplayervr.UpnpService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.itmindco.dlnaplayervr.Models.VideoListContent;
import com.itmindco.dlnaplayervr.Models.VideoListItem;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.action.ActionException;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ErrorCode;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

public class ContentDirectoryBrowse {

    public interface Callbacks {
        void onRefresh();

        void onError(String msg);
    }

    private Callbacks mCallbacks;
    private BrowseRegistryListener mListener;
    private AndroidUpnpService mService;

    public ContentDirectoryBrowse(Callbacks mCallbacks){
        this.mCallbacks = mCallbacks;
        mListener = new BrowseRegistryListener();
    }


    public void refreshDevices() {
        if (mService == null)
            return;

        mService.getRegistry().removeAllRemoteDevices();

        for (Device device : mService.getRegistry().getDevices())
            mListener.deviceAdded(device);

        mService.getControlPoint().search();
    }

    //для device вызываем с id = "0"
    public void showContent(Service service, String id) {
        if (mService == null || service == null)
            return;


        mService.getControlPoint().execute(
                new CustomContentBrowseActionCallback(service, id));

    }

    public Boolean bindServiceConnection(Context context) {
        if (context == null)
            return false;

        context.bindService(
            new Intent(context, AndroidUpnpServiceImpl.class),
            serviceConnection, Context.BIND_AUTO_CREATE
        );

        return true;
    }

    public Boolean unbindServiceConnection(Context context) {
        if (mService != null)
            mService.getRegistry().removeListener(mListener);

        if (context == null)
            return false;

        context.unbindService(serviceConnection);
        return true;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = (AndroidUpnpService) service;
            mService.getRegistry().addListener(mListener);

            for (Device device : mService.getRegistry().getDevices())
                mListener.deviceAdded(device);

            mService.getControlPoint().search();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private class BrowseRegistryListener extends DefaultRegistryListener {

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

            VideoListItem deviceItem = VideoListItem.createForDevice(device);
            if (deviceItem.getContentDirectory() != null) {
                VideoListContent.addItem(deviceItem);
                if (mCallbacks != null)
                    mCallbacks.onRefresh();
            }
        }

        public void deviceRemoved(Device device) {
            VideoListContent.removeItem(String.valueOf(device.hashCode()));
            if (mCallbacks != null)
                mCallbacks.onRefresh();
        }
    }

    private class CustomContentBrowseActionCallback extends Browse {
        private Service service;

        public CustomContentBrowseActionCallback(Service service, String id) {
            super(service, id, BrowseFlag.DIRECT_CHILDREN, "*", 0, null,
                    new SortCriterion(true, "dc:title"));

            this.service = service;
        }


        @Override
        public void received(final ActionInvocation actionInvocation, final DIDLContent didl) {

//        URI usableIcon = item.getFirstPropertyValue(DIDLObject.Property.UPNP.ICON.class);
//        if (usableIcon != null)
//            itemModel.setIconUrl(usableIcon.toString());

            //ArrayList<ItemModel> items = new ArrayList<ItemModel>();

            try {
                VideoListContent.clear();
                for (Container childContainer : didl.getContainers()) {
                    VideoListItem item = VideoListItem.createForItem(service, childContainer);
                    VideoListContent.addItem(item);
                    //items.add(createItemModel(childContainer));
                }

                for (Item childItem : didl.getItems()) {
                    VideoListItem item = VideoListItem.createForItem(service, childItem);
                    VideoListContent.addItem(item);
                }

                //уже вызывается из MainActivity
//                if (mCallbacks != null)
//                    mCallbacks.onRefresh();

            } catch (Exception ex) {
                actionInvocation.setFailure(new ActionException(
                        ErrorCode.ACTION_FAILED,
                        "Can't create list childs: " + ex, ex));
                failure(actionInvocation, null, ex.getMessage());
            }
        }

        @Override
        public void updateStatus(Status status) {

        }

        @Override
        public void failure(ActionInvocation invocation, UpnpResponse response, String s) {
            if (mCallbacks != null)
                mCallbacks.onError(s);
            //    mCallbacks.onDisplayItemsError(createDefaultFailureMessage(invocation, response));
        }
    }
}