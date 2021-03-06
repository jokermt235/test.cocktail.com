package org.mutalip.cocktail.model;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mutalip.cocktail.api.CocktailService;
import org.mutalip.cocktail.instance.DbInstance;
import org.mutalip.cocktail.instance.FileInstance;
import org.mutalip.cocktail.instance.IConverterFactory;
import org.mutalip.cocktail.instance.RetrofitInstance;
import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DrinkRepo {
    private static String TAG  = "DrinkRepo";
    IConverterFactory factory;
    CocktailService service;
    DbInstance db;
    IDrink resultData;
    public DrinkRepo(IDrink resultData){
        this.resultData = resultData;
        factory = new DrinkFactory();
        Retrofit instance = RetrofitInstance.getInstance(factory);
        service = instance.create(CocktailService.class);
        String filePath = Environment.getExternalStorageDirectory() + File.separator + "drink.json";
        try {
            db = new FileInstance(filePath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void filter(){
        Call<ResponseBody> call = service.randDrink();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        JSONObject data = new JSONObject(response.body().string());
                        resultData.getData(data.getJSONArray("drinks"));
                    }catch (Exception e){
                        resultData.failure(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                resultData.failure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void save(JSONObject json){
        if(db != null){
            try {
                String remotePath = json.getString("strDrinkThumb");
                String filename = remotePath.substring(remotePath.lastIndexOf('/') + 1);
                json.put("strDrinkThumb", Environment.getExternalStorageDirectory() + File.separator + filename);
                db.save(json);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void filterLocal(){
        resultData.getData(db.getData());
    }

    public void delete(String id){
        try {
            JSONArray data = db.getData();
            for(int i=0;i<data.length(); i++){
                JSONObject item = (JSONObject) data.get(i);
                if(item.getString("idDrink").equals(id)){
                    Log.d(TAG, "Record found in the scope");
                    data.remove(i);
                }
            }
            db.syncLocal(data);
            JSONArray _data = new JSONArray();
            int count = data.length() - 10;
            if(count < 0) count = 0;
            for (int i = data.length() - 1; i>= count; i--) {
                _data.put(data.get(i));
            }
            resultData.getData(_data);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
