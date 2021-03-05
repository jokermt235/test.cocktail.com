package org.mutalip.cocktail.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

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
    private static  ResultReceiver resultReceiver;


    public ImageIntentService() {
        super("ImageService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        resultReceiver = intent.getParcelableExtra("receiver");
        String imageUrl = intent.getStringExtra(IMAGE_URL);
        String filename = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        Log.d(TAG, imageUrl);
        if (!TextUtils.isEmpty(imageUrl)) {
            try{
                File output = new File(Environment.getExternalStorageDirectory(),
                        filename);
                if (output.exists()) {
                    output.delete();
                }

                InputStream stream = null;
                FileOutputStream fos = null;
                int next;
                try {
                    java.net.URL url = new URL(imageUrl);
                    URLConnection conn = url.openConnection();
                    int fileLength = conn.getContentLength();
                    stream = conn.getInputStream();
                    fos = new FileOutputStream(output.getPath());
                    long total=0;
                    byte data[] = new byte[1024];
                    while ((next = stream.read(data)) != -1) {
                        total+=next;
                        fos.write(data,0,next);
                        sendDownloadValue((int)((total*100)/fileLength));
                    }
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

    private void sendDownloadValue(int value){
        Bundle b = new Bundle();
        b.putInt("download_value", value);
        resultReceiver.send(101, b);
    }
}
