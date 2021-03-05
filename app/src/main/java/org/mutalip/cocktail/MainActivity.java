package org.mutalip.cocktail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mutalip.cocktail.model.DrinkRepo;
import org.mutalip.cocktail.model.IDrink;
import org.mutalip.cocktail.service.ImageIntentService;
import org.mutalip.cocktail.service.ImageLoader;

public class MainActivity extends AppCompatActivity implements IDrink , ImageLoader {
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
        ActivityCompat.requestPermissions(this, perms, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(ImageIntentService.NOTIFICATION));
    }

    @Override
    protected void onStart() {
        super.onStart();
        new DrinkRepo(this).filter();
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
            serviceIntent.putExtra("url", drink.getString("strDrinkThumb"));
            serviceIntent.putExtra(ImageIntentService.FILENAME, drink.getString("idDrink"));
            startService(serviceIntent);
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
            Log.d(TAG, ImageIntentService.FILEPATH);
        }
    };
}