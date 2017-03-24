package com.example.android.uamp.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadata;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.widget.SimpleCursorAdapter;

import com.example.android.uamp.utils.LogHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.jar.Manifest;

import static java.security.AccessController.getContext;

/**
 * Created by vinceh on 3/22/17.
 */

public class LocalMediaSource implements MusicProviderSource {
    private static final String TAG = LogHelper.makeLogTag(LocalMediaSource.class);
    private static ContentResolver mContentResolver;

    @Override
    public boolean is_local() { return true; }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        return iterator(null);
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator(Context context) {
        try {
            mContentResolver = context.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = null;
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = null;

            Cursor cursor = mContentResolver.query(uri, projection, selection, selectionArgs,
                    sortOrder);

            ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    MediaMetadataCompat song = buildFromCursor(cursor);
                    if (song != null) {
                        tracks.add(song);
                    }
                }
                cursor.close();
            }
            return tracks.iterator();
        } catch (Exception e) {
            LogHelper.e(TAG, e, "Could not retrieve music list from local storage");
            throw new RuntimeException("Could not retrieve music list from local storage", e);
        }
    }

    private MediaMetadataCompat buildFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID));
        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
        String genre = "Rock"; // how to get genre?
        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
        String iconUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_KEY)); // ?
        long trackNumber = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TRACK));

        Uri uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", (int)id);

        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, String.valueOf(id));
        builder.putString(MediaStore.Audio.AudioColumns.DATA, data);
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, title);
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist);
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album);
        builder.putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre);
        builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration);
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl);
        builder.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber);
        return builder.build();
    }
}
