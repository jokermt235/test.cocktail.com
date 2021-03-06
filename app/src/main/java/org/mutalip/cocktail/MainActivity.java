package org.mutalip.cocktail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mutalip.cocktail.model.DrinkRepo;
import org.mutalip.cocktail.model.IDrink;
import org.mutalip.cocktail.service.ImageIntentService;
import org.mutalip.cocktail.service.ImageLoader;

public class MainActivity extends AppCompatActivity implements
        IDrink , ImageLoader, SimpleGestureFilter.SimpleGestureListener,
        View.OnClickListener{
    private static String TAG = "MainActivity";
    private static int REQUEST_CODE = 120;
    private static String perms[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ImageView imageDrink;
    private TextView idDrink;
    private TextView strDrink;
    private TextView strCategory;
    private TextView strAlcoholic;
    private Intent serviceIntent;
    private ProgressBar imageProgress;
    private View progressView;
    private View resendView;
    private View successView;
    private TextView messageError;
    private SimpleGestureFilter detector;
    private Button resendButton;
    private Button favorite;
    private Button historyButton;
    private DrinkRepo repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageDrink = findViewById(R.id.strDrinkThumb);
        idDrink = findViewById(R.id.idDrink);
        strDrink = findViewById(R.id.strDrink);
        strCategory = findViewById(R.id.strCategory);
        strAlcoholic = findViewById(R.id.strAlcoholic);
        serviceIntent = new Intent(this, ImageIntentService.class);
        progressView = findViewById(R.id.layoutProgress);
        resendView   = findViewById(R.id.layoutResend);
        successView  = findViewById(R.id.layoutSuccess);
        imageProgress   = findViewById(R.id.imageProgressBar);
        messageError = findViewById(R.id.messageError);
        resendButton = findViewById(R.id.resendButton);
        resendButton.setOnClickListener(this);
        favorite    = findViewById(R.id.favorite);
        favorite.setOnClickListener(this);
        historyButton = findViewById(R.id.history);
        historyButton.setOnClickListener(this);
        detector = new SimpleGestureFilter(MainActivity.this, this);
        repo = new DrinkRepo(this);
        getDrink();
    }

    private void getDrink(){
        if(storagePermited()) {
            repo.filter();
        }else{
            ActivityCompat.requestPermissions(this, perms, REQUEST_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "View clicked");
        switch (v.getId()) {
            case  R.id.favorite: {
                v.setBackground(new ColorDrawable(Color.YELLOW));
                break;
            }
            case R.id.resendButton : {
                successView.setVisibility(View.GONE);
                progressView.setVisibility(View.VISIBLE);
                resendView.setVisibility(View.GONE);
                getDrink();
                break;
            }
            case R.id.history : {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                break;
            }
        }
    }

    private boolean storagePermited(){
        String requiredPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int permit = this.checkCallingOrSelfPermission(requiredPermission);
        if (permit == PackageManager.PERMISSION_GRANTED) {
            return  true;
        }
        return  false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(storagePermited()) {
            repo.filter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(ImageIntentService.NOTIFICATION));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void getData(JSONArray data) {
        try {
            JSONObject drink = (JSONObject) data.get(0);
            idDrink.setText(drink.getString("idDrink"));
            strDrink.setText(drink.getString("strDrink"));
            strCategory.setText(drink.getString("strCategory"));
            strAlcoholic.setText(drink.getString("strAlcoholic"));
            resendView.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);
            successView.setVisibility(View.VISIBLE);
            imageProgress.setVisibility(View.VISIBLE);
            serviceIntent.putExtra("url", drink.getString("strDrinkThumb"));
            serviceIntent.putExtra(ImageIntentService.FILENAME, drink.getString("idDrink"));
            serviceIntent.putExtra("receiver", new ProgressReceiver(new Handler()));
            startService(serviceIntent);
            repo.save(drink);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void load(Bitmap bmp) {
        Log.d(TAG, "Image uploading finished");
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String file =  intent.getStringExtra(ImageIntentService.FILEPATH);
            if(intent.getIntExtra(ImageIntentService.RESULT, 1) == Activity.RESULT_OK){
                imageProgress.setVisibility(View.GONE);
                imageDrink.setVisibility(View.VISIBLE);
                imageDrink.setImageBitmap(BitmapFactory.decodeFile(file));
            }
            if(intent.getIntExtra("download_result", 0) == 101){
                int value = intent.getIntExtra("download_value", 1);
                imageProgress.setProgress(value);
            }
        }
    };

    @Override
    public void onSwipe(int direction) {
        Log.d(TAG, "Swipe left");
        if(direction == SimpleGestureFilter.SWIPE_LEFT){
            if(storagePermited()) {
                new DrinkRepo(this).filter();
            }else{
                ActivityCompat.requestPermissions(this, perms, REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void failure(String message) {
        successView.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        resendView.setVisibility(View.VISIBLE);
        messageError.setText(message);
    }
    class ProgressReceiver extends ResultReceiver {
        public ProgressReceiver(Handler handler){
            super(handler);
        }
        protected void onReceiveResult(int resultCode, Bundle resultData){
            if(resultCode==101) {
                int value = resultData.getInt("download_value");
                imageProgress.setProgress(value);
            }
        }

    }

}
