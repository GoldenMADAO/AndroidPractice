package com.madao.datastorage;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* Internal Storage */
public class InternalActivity extends Activity {
    public static final String FILENAME = "hello_file";
    public static final String TAG = "FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void restore() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buf = new byte[1024];

            int len = fis.read(buf);
            fis.close();

            String str = new String(buf, 0, len);
            System.out.println(str);
        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }
    }

    public void save() {
        String str = "hello world";

        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(str.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.v(TAG, e.toString());
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public File getTempFile(String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
