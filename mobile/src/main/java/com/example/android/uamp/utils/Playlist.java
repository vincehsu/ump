package com.example.android.uamp.utils;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by vinceh on 3/27/17.
 */

public class Playlist {
    private static LinkedList<MediaBrowserCompat.MediaItem> mList;
    private static Playlist mInstance;

    private Playlist() {
        mList = new LinkedList<>();
    }

    public static synchronized Playlist getInstance() {
        if (mInstance == null)
            mInstance = new Playlist();
        return mInstance;
    }

    public synchronized boolean add(MediaBrowserCompat.MediaItem item) {
        if (item == null)
            return false;
        return mList.add(item);
    }

    public synchronized boolean remove(MediaBrowserCompat.MediaItem item) {
        if (item == null)
            return false;
        return mList.remove(item);
    }

    public synchronized boolean contains(MediaBrowserCompat.MediaItem item) {
        return (mList.contains(item));
    }

    public ArrayList<MediaBrowserCompat.MediaItem> retrieveList() {
        return new ArrayList<>(mList);
    }

    public synchronized int size() {
        return mList.size();
    }
}
