package org.mutalip.cocktail.api;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CocktailService {
    @GET("random.php")
    Call<ResponseBody> randDrink();
}
