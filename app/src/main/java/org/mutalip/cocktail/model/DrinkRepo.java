package org.mutalip.cocktail.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mutalip.cocktail.api.CocktailService;
import org.mutalip.cocktail.instance.IConverterFactory;
import org.mutalip.cocktail.instance.RetrofitInstance;
import org.mutalip.cocktail.model.entity.DrinkList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DrinkRepo {
    private static String TAG  = "DrinkRepo";
    IConverterFactory factory;
    CocktailService service;
    IDrink resultData;
    public DrinkRepo(IDrink resultData){
        this.resultData = resultData;
        factory = new DrinkFactory();
        Retrofit instance = RetrofitInstance.getInstance(factory);
        service = instance.create(CocktailService.class);
    }
    public void filter(){
        Call<ResponseBody> call = service.randDrink();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    //resultData.getData(response.body());
                    try {
                        JSONObject data = new JSONObject(response.body().string());
                        resultData.getData(data.getJSONArray("drinks"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
