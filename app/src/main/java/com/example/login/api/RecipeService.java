package com.example.login.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("b58b830e-117b-41b9-84e4-36e35440997d")
    Call<RecipesResponse> recipes();


}
