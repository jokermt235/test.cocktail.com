package org.mutalip.cocktail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.mutalip.cocktail.model.DrinkRepo;
import org.mutalip.cocktail.model.IDrink;

public class MainActivity extends AppCompatActivity implements IDrink {
    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new DrinkRepo(this).filter();

    }

    @Override
    public void getData(JSONArray data) {
    }
}