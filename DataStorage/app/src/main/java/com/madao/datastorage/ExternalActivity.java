package com.madao.datastorage;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class ExternalActivity extends Activity {
    public static final String LOG_TAG = "LOG";
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void checkPermission() {
        final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        int permissionCheck = checkSelfPermission(permission);
        if (PackageManager.PERMISSION_GRANTED != permissionCheck) {
            if (shouldShowRequestPermissionRationale(permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else {
                requestPermissions(new String[]{permission}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }

    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdir()) {
            Log.e(LOG_TAG, "Directory not created");
        }

        return file;
    }

    public File getPrivateDir(String albumName) {
        File file = new File(getExternalFilesDir(null), albumName);
        if (!file.mkdir()) {
            Log.e(LOG_TAG, "Directory not created");
        }

        return file;
    }
}
