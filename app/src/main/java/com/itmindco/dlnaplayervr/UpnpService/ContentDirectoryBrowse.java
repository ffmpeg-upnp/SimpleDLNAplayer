package com.itmindco.dlnaplayervr.UpnpService;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;

import com.google.android.libraries.mediaframework.exoplayerextensions.Video;
import com.itmindco.dlnaplayervr.Models.DeviceModel;
import com.itmindco.dlnaplayervr.Models.ItemModel;
import com.itmindco.dlnaplayervr.Models.LocalItemModel;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;

import java.util.ArrayList;
import java.util.Stack;

public class ContentDirectoryBrowse {

    public interface Callbacks {
        void onDisplayDevices();
        void onDisplayDirectories();
        void onDisplayItems(ArrayList<ItemModel> items);
        void onDisplayItemsError(String error);
        void onDeviceAdded(DeviceModel device);
        void onDeviceRemoved(DeviceModel device);
    }

    private Callbacks mCallbacks;
    private BrowseRegistryListener mListener;
    private AndroidUpnpService mService;
    private Stack<ItemModel> mFolders = new Stack<ItemModel>();
    private Boolean mIsShowingDeviceList = true;
    private DeviceModel mCurrentDevice = null;

    public ContentDirectoryBrowse(Callbacks mCallbacks){
        this.mCallbacks = mCallbacks;
        mListener = new BrowseRegistryListener();
        mListener.setCallbacksListener(this.mCallbacks);
    }

    public void onDestroy() {
        mCallbacks = null;
        //unbindServiceConnection();
    }

    public void navigateTo(Object model) {
        if (model instanceof DeviceModel) {

            DeviceModel deviceModel = (DeviceModel)model;
            Device device = deviceModel.getDevice();

            if (device.isFullyHydrated()) {
                Service conDir = deviceModel.getContentDirectory();

                if (conDir != null)
                    mService.getControlPoint().execute(
                            new CustomContentBrowseActionCallback(conDir, "0"));

                if (mCallbacks != null)
                    mCallbacks.onDisplayDirectories();

                mIsShowingDeviceList = false;

                mCurrentDevice = deviceModel;
            } else {
                //Toast.makeText(mActivity, R.string.info_still_loading, Toast.LENGTH_SHORT)
                //    .show();
            }
        }

        else if (model instanceof ItemModel) {

            ItemModel item = (ItemModel)model;

            if (item.isContainer()) {
                if (mFolders.isEmpty())
                    mFolders.push(item);
                else
                    if (mFolders.peek().getId() != item.getId())
                        mFolders.push(item);

                mService.getControlPoint().execute(
                        new CustomContentBrowseActionCallback(item.getService(),
                                item.getId()));

            } else {
                try {
//                    Uri uri = Uri.parse(item.getUrl());
//                    MimeTypeMap mime = MimeTypeMap.getSingleton();
//                    String type = mime.getMimeTypeFromUrl(uri.toString());
//                    Intent intent = new Intent();
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
//                    intent.setDataAndType(uri, type);
//                    startActivity(intent);
                    Video video = new Video(item.getUrl(), Video.VideoType.OTHER);
                    //mCallbacks.onPlayVideo(video);
                } catch(NullPointerException ex) {
                    //Toast.makeText(mActivity, R.string.info_could_not_start_activity, Toast.LENGTH_SHORT)
                    //        .show();
                } catch(ActivityNotFoundException ex) {
                    //Toast.makeText(mActivity, R.string.info_no_handler, Toast.LENGTH_SHORT)
                    //    .show();
                }
            }
        }

        else if (model instanceof LocalItemModel) {

            LocalItemModel item = (LocalItemModel)model;


                try {
                    Uri uri = Uri.parse(item.getUrl());
                    Video video = new Video(item.getUrl(), Video.VideoType.OTHER);
                    //mCallbacks.onPlayVideo(video);


                } catch(NullPointerException ex) {
                    //Toast.makeText(mActivity, R.string.info_could_not_start_activity, Toast.LENGTH_SHORT)
                    //        .show();
                } catch(ActivityNotFoundException ex) {
                    //Toast.makeText(mActivity, R.string.info_no_handler, Toast.LENGTH_SHORT)
                    //        .show();
                }

        }
    }


    public Boolean goBack() {
        if (mFolders.empty()) {
            if (!mIsShowingDeviceList) {
                mIsShowingDeviceList = true;
                if (mCallbacks != null)
                    mCallbacks.onDisplayDevices();
            } else {
                return true;
            }
        } else {
            ItemModel item = mFolders.pop();

            mService.getControlPoint().execute(
                    new CustomContentBrowseActionCallback(item.getService(),
                            item.getContainer().getParentID()));
        }

        return false;
    }

    public void refreshDevices() {
        if (mService == null)
            return;

        mService.getRegistry().removeAllRemoteDevices();

        for (Device device : mService.getRegistry().getDevices())
            mListener.deviceAdded(device);

        mService.getControlPoint().search();
    }

    public void refreshCurrent() {
        if (mService == null)
            return;

        if (mIsShowingDeviceList != null && mIsShowingDeviceList) {
            if (mCallbacks != null)
                mCallbacks.onDisplayDevices();

            mService.getRegistry().removeAllRemoteDevices();

            for (Device device : mService.getRegistry().getDevices())
                mListener.deviceAdded(device);

            mService.getControlPoint().search();
        } else {
            if (!mFolders.empty()) {
                ItemModel item = mFolders.peek();
                if (item == null)
                    return;

                mService.getControlPoint().execute(
                        new CustomContentBrowseActionCallback(item.getService(),
                                item.getId()));
            } else {
                if (mCurrentDevice != null) {
                    Service service = mCurrentDevice.getContentDirectory();
                    if (service != null)
                        mService.getControlPoint().execute(
                            new CustomContentBrowseActionCallback(service, "0"));
                }
            }
        }
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

}