package org.mutalip.cocktail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.mutalip.cocktail.adapter.HistoryGridAdapter;
import org.mutalip.cocktail.model.DrinkRepo;
import org.mutalip.cocktail.model.IDrink;

public class HistoryActivity extends AppCompatActivity implements IDrink {
    private static String TAG = "HistoryActivity";
    private  DrinkRepo repo;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.historyList);
        repo = new DrinkRepo(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        repo.filterLocal();
    }

    @Override
    public void getData(JSONArray data) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        try{
            JSONArray _data = new JSONArray();
            int count = data.length() - 10;
            if(count < 0) count = 0;
            for (int i = data.length() - 1; i>= count; i--) {
                _data.put(data.get(i));
            }
            recyclerView.setAdapter(new HistoryGridAdapter(HistoryActivity.this, _data));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void failure(String message) {

    }
}