package com.dlnaplayervr.itmindco.dlnaplayervr;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dlnaplayervr.itmindco.dlnaplayervr.adplayer.ImaPlayer;
import com.google.android.libraries.mediaframework.exoplayerextensions.Video;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.transport.Router;
import org.fourthline.cling.transport.RouterException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ContentDirectoryBrowseTaskFragment.Callbacks {

    private ContentDirectoryBrowseTaskFragment mFragment;
    private ArrayAdapter<CustomListItem> mDeviceListAdapter;
    private ArrayAdapter<CustomListItem> mItemListAdapter;
    ListView listView;

    private ImaPlayer imaPlayer;
    private FrameLayout videoPlayerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getFragmentManager();
        mFragment = (ContentDirectoryBrowseTaskFragment)fragmentManager.findFragmentByTag("task");

        mDeviceListAdapter = new CustomListAdapter(this);
        mItemListAdapter = new CustomListAdapter(this);

        listView = (ListView)findViewById(R.id.listView);
         if (listView != null) {
            listView.setAdapter(mDeviceListAdapter);
            listView.setOnItemClickListener(MainActivity.this);
        }

        if (mFragment == null) {
            mFragment = new ContentDirectoryBrowseTaskFragment();
            fragmentManager.beginTransaction().add(mFragment, "task").commit();
        } else {
            mFragment.refreshDevices();
            mFragment.refreshCurrent();
        }

        videoPlayerContainer = (FrameLayout) findViewById(R.id.video_frame);

        dumpVideos(getApplicationContext());
        //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void dumpVideos(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Video.VideoColumns.DATA };
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        if (c != null) {
            c.moveToFirst();
            vidsCount = c.getCount();
            do {
                Log.d("VIDEO", c.getString(0));
                mDeviceListAdapter.add(new LocalItemModel(0,"local video",c.getString(0),c.getString(0)));
            }while (c.moveToNext());
            c.close();
        }
        Log.d("VIDEO", "Total count of videos: " + vidsCount);
    }

    public void createImaPlayer(Video videoListItem) {
        if (imaPlayer != null) {
            imaPlayer.release();
        }

        // If there was previously a video player in the container, remove it.
        videoPlayerContainer.removeAllViews();

        //String adTagUrl = videoListItem.adUrl;
        //String videoTitle = videoListItem.title;

        imaPlayer = new ImaPlayer(this,
                videoPlayerContainer,
                videoListItem,
                "my",
                null);
        //imaPlayer.setFullscreenCallback(this);

        //Resources res = getResources();

        // Customize the UI of the video player.

        // Set a logo (an Android icon will be displayed in the top left)
        //Drawable logo = res.getDrawable(R.drawable.gmf_icon);
        //imaPlayer.setLogoImage(logo);



        // Now that the player is set up, let's start playing.
        imaPlayer.play();
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


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mFragment.navigateTo(adapterView.getItemAtPosition(i));
    }

    @Override
    public void onDisplayDevices() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(mDeviceListAdapter);
            }
        });
    }

    @Override
    public void onDisplayDirectories() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mItemListAdapter.clear();
                listView.setAdapter(mItemListAdapter);
            }
        });
    }

    @Override
    public void onDisplayItems(final ArrayList<ItemModel> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mItemListAdapter.clear();
                mItemListAdapter.addAll(items);
            }
        });
    }

    @Override
    public void onDisplayItemsError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mItemListAdapter.clear();
                mItemListAdapter.add(new CustomListItem(
                        R.drawable.ic_warning,
                        getResources().getString(R.string.info_errorlist_folders),
                        error));
            }
        });
    }

    @Override
    public void onDeviceAdded(final DeviceModel device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int position = mDeviceListAdapter.getPosition(device);
                if (position >= 0) {
                    mDeviceListAdapter.remove(device);
                    mDeviceListAdapter.insert(device, position);
                } else {
                    mDeviceListAdapter.add(device);
                }
            }
        });
    }

    @Override
    public void onDeviceRemoved(final DeviceModel device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceListAdapter.remove(device);
            }
        });
    }

    @Override
    public void onPlayVideo(final Video video) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createImaPlayer(video);
            }
        });
    }
}
