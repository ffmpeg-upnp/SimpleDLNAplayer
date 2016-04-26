package com.itmindco.dlnaplayervr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.itmindco.dlnaplayervr.Fragments.VideoItemFragment;
import com.itmindco.dlnaplayervr.Fragments.VideoPlayerFragment;
import com.itmindco.dlnaplayervr.Models.VideoListContent;
import com.itmindco.dlnaplayervr.Models.VideoListItem;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements VideoItemFragment.OnListFragmentInteractionListener {

    VideoItemFragment videoItemFragment;
    VideoPlayerFragment videoPlayerFragment;
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }


        videoItemFragment = (VideoItemFragment) getSupportFragmentManager().findFragmentById(R.id.video_list);
        videoPlayerFragment = (VideoPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.video_player_fragment);

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
        //if (mFragment.goBack())
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
                //mFragment.refreshDevices();
                //mFragment.refreshCurrent();

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
    public void onListFragmentInteraction(VideoListItem item) {
        switch (item.type){
            case LOCALCONTENT:
                VideoListContent.FillLocalVideos(this);
                videoItemFragment.UpdateList();
                break;
            case ITEM:
                //play video
                try {
                    videoPlayerFragment.PlayVideo(item.url);
                }
                catch (IOException ex){
                    Log.e("DLNAPlayer",ex.getLocalizedMessage());
                }
                break;
        }
    }
}
