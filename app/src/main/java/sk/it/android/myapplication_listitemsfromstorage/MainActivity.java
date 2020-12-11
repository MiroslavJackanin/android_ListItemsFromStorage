package sk.it.android.myapplication_listitemsfromstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1;
    private static final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkMyPermission()) {
            renderList();
        } else {
            requestMyPermissions();
        }
    }

    private void requestMyPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{PERMISSION}, PERMISSION_CODE);
    }
    private boolean checkMyPermission() {
        return ActivityCompat.checkSelfPermission(this, PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                renderList();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Permission denied", Snackbar.LENGTH_INDEFINITE).setAction("Go to settings", v -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }).show();
            }
        }
    }

    public void renderList() {
        Log.i("DEBUG", "Rendering list");

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(MainActivity.this, "Action", Toast.LENGTH_SHORT).show());
    }

    public void getMusic() {
        /*Log.i("DEBUG", "Getting music");

        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        File f = new File("/storage/emulated/0/Music");
        Log.v("Files",f.exists()+"");
        Log.v("Files",f.isDirectory()+"");
        Log.v("Files", Arrays.toString(f.listFiles()));

        Log.i("DEBUG", String.valueOf(songUri));
        if (songCursor == null) {
            Log.i("DEBUG", "Cursor null");
        }
        if (!songCursor.moveToFirst()) {
            Log.i("DEBUG", "Cursor.moveToFirst() == false");
        }

        if (songCursor != null && songCursor.moveToFirst()) {
            Log.i("DEBUG", "inside");

            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);

                Log.i("DEBUG", currentTitle);

                arrayList.add(currentTitle + "\n" + currentArtist);
            } while (songCursor.moveToNext());

            songCursor.close();
        }*/

        /*// String[] STAR = { "*" };  //it is projection you can modify it according to your need
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        String[] selectionArgs = null;
        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";  // check for music files

        Cursor cursor = getContentResolver().query(allsongsuri,null,selection,selectionArgs,sortOrder );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String song_name = cursor
                            .getString(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    int song_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media._ID));

                    String fullpath = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));


                    String album_name = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    int album_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                    String artist_name = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    int artist_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));

                    arrayList.add(song_name + "\n" + fullpath);

                } while (cursor.moveToNext());

            }
            cursor.close();
        }*/

        arrayList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        Log.i("DEBUG CURSOR", cursor.moveToNext()+"");
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            arrayList.add(name);
        }

        cursor.close();
    }
}