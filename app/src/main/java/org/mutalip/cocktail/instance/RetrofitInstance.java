package org.mutalip.cocktail.instance;
import retrofit2.Retrofit;

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static String TAG  = "RetrofitInstance";
    private static  final String BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    public static  Retrofit getInstance(IConverterFactory factory) {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(factory.create())
                    .build();
        }
        return retrofit;
    }
}
