package com.example.login.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("e6d7f2d0-d9a8-466c-8c47-4c742803b91a")
    Call<RecipesResponse> recipes();


}
