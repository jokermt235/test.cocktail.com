package org.mutalip.cocktail.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ImageIntentService extends IntentService {

    private static  String TAG = "ImageIntentService";
    private static   ImageLoader imageLoader;
    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "ReceiverService";

    public static final String IMAGE_URL = "url";


    public ImageIntentService() {
        super("ImageService");
    }

    public static void setImageLoader(ImageLoader loader){
        imageLoader = loader;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String imageUrl = intent.getStringExtra(IMAGE_URL);
        Log.d(TAG, imageUrl);
        if (!TextUtils.isEmpty(imageUrl)) {
            try{
                File output = new File(Environment.getExternalStorageDirectory(),
                        FILENAME);
                if (output.exists()) {
                    output.delete();
                }

                InputStream stream = null;
                FileOutputStream fos = null;
                try {

                    java.net.URL url = new URL(imageUrl);
                    stream = url.openConnection().getInputStream();
                    InputStreamReader reader = new InputStreamReader(stream);
                    fos = new FileOutputStream(output.getPath());
                    int next = -1;
                    while ((next = reader.read()) != -1) {
                        fos.write(next);
                    }
                    // Successful finished
                    result = Activity.RESULT_OK;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                downloadCompleted(output.getAbsolutePath(),result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadCompleted(String filePath, int result){
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, filePath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
