package com.itmindco.dlnaplayervr;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.itmindco.dlnaplayervr.Models.DeviceModel;
import com.itmindco.dlnaplayervr.Models.ItemModel;
import com.itmindco.dlnaplayervr.Models.LocalItemModel;
import com.itmindco.dlnaplayervr.adplayer.ImaPlayer;
import com.google.android.libraries.mediaframework.exoplayerextensions.Video;

import java.io.IOException;
import java.util.ArrayList;

import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity
         {

    private ContentDirectoryBrowseTaskFragment mFragment;
    private ArrayAdapter<CustomListItem> mDeviceListAdapter;
    private ArrayAdapter<CustomListItem> mItemListAdapter;
    ListView listView;


    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FragmentManager fragmentManager = getFragmentManager();
//        mFragment = (ContentDirectoryBrowseTaskFragment)fragmentManager.findFragmentByTag("task");
//
//        mDeviceListAdapter = new CustomListAdapter(this);
//        mItemListAdapter = new CustomListAdapter(this);


//        listView = (ListView)findViewById(R.id.listView);
//         if (listView != null) {
//            listView.setAdapter(mDeviceListAdapter);
//            listView.setOnItemClickListener(MainActivity.this);
//        }
//
//        if (mFragment == null) {
//            mFragment = new ContentDirectoryBrowseTaskFragment();
//            fragmentManager.beginTransaction().add(mFragment, "task").commit();
//        } else {
//            mFragment.refreshDevices();
//            mFragment.refreshCurrent();
//        }



        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        if (mFragment.goBack())
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, 0, 0, R.string.searchLAN).setIcon(android.R.drawable.ic_menu_search);
        menu.add(0, 1, 0, R.string.switchRouter).setIcon(android.R.drawable.ic_menu_revert);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case 0:
                mFragment.refreshDevices();
                mFragment.refreshCurrent();

                break;
            case 1:
//                if (upnpService != null) {
//                    Router router = upnpService.get().getRouter();
//                    try {
//                        if (router.isEnabled()) {
//                            Toast.makeText(this, R.string.disablingRouter, Toast.LENGTH_SHORT).show();
//                            router.disable();
//                        } else {
//                            Toast.makeText(this, R.string.enablingRouter, Toast.LENGTH_SHORT).show();
//                            router.enable();
//                        }
//                    } catch (RouterException ex) {
//                        Toast.makeText(this, getText(R.string.errorSwitchingRouter) + ex.toString(), Toast.LENGTH_LONG).show();
//                        ex.printStackTrace(System.err);
//                    }
//                }
                break;
        }
        return false;

        //return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        mFragment.navigateTo(adapterView.getItemAtPosition(i));
//    }
//
//    @Override
//    public void onDisplayDevices() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                listView.setAdapter(mDeviceListAdapter);
//            }
//        });
//    }
//
//    @Override
//    public void onDisplayDirectories() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mItemListAdapter.clear();
//                listView.setAdapter(mItemListAdapter);
//            }
//        });
//    }
//
//    @Override
//    public void onDisplayItems(final ArrayList<ItemModel> items) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mItemListAdapter.clear();
//                mItemListAdapter.addAll(items);
//            }
//        });
//    }
//
//    @Override
//    public void onDisplayItemsError(final String error) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mItemListAdapter.clear();
//                mItemListAdapter.add(new CustomListItem(
//                        R.drawable.ic_warning,
//                        getResources().getString(R.string.info_errorlist_folders),
//                        error));
//            }
//        });
//    }
//
//    @Override
//    public void onDeviceAdded(final DeviceModel device) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                int position = mDeviceListAdapter.getPosition(device);
//                if (position >= 0) {
//                    mDeviceListAdapter.remove(device);
//                    mDeviceListAdapter.insert(device, position);
//                } else {
//                    mDeviceListAdapter.add(device);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onDeviceRemoved(final DeviceModel device) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mDeviceListAdapter.remove(device);
//            }
//        });
//    }
//
//    @Override
//    public void onPlayVideo(final Video video) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    createIjkPlayer(video);
//                } catch (IOException e) {
//                    e.printStackTrace();
//
//                }
//            }
//        });
//    }


}
