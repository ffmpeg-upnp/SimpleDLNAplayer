package com.itmindco.dlnaplayervr.Models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoListContent {

    public static final List<VideoListItem> ITEMS = new ArrayList<VideoListItem>();

    public static final Map<String, VideoListItem> ITEM_MAP = new HashMap<String, VideoListItem>();

    static {
        addItem(new VideoListItem("localvideo","Local","", VideoListItem.TypeListItem.LOCALCONTENT));
    }

    private static void addItem(VideoListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.url, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static void fillLocalVideos(Context context){
        ITEMS.clear();
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {

            return;
        }

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Video.VideoColumns.DATA };
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        if (c != null) {
            c.moveToFirst();
            vidsCount = c.getCount();
            do {
                //Log.d("VIDEO", c.getString(0));
                addItem(new VideoListItem(c.getString(0),c.getString(0),"", VideoListItem.TypeListItem.ITEM));
            }while (c.moveToNext());
            c.close();
        }
        //Log.d("VIDEO", "Total count of videos: " + vidsCount);
    }

    public static void fillRoot(){
        ITEMS.clear();
        addItem(new VideoListItem("localvideo","Local","", VideoListItem.TypeListItem.LOCALCONTENT));
    }

}
