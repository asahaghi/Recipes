package com.example.login.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.example.login.api.RecipeService;
import com.example.login.api.RecipesResponse;
import com.example.login.model.Menu;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeFragment extends Fragment {
    private View rootView;
    private int id;
    private ListView lvRecipe;
    //private TextView tvTest;
    private List<Menu> menu = new ArrayList<>();
    private ImageView ivRecipe;
    private TextView tvIng;
    private FrameLayout flRecipe;
    ArrayList<String> ingredients;
    public RecipeFragment(int id) {
        this.id = id;
    }

    public RecipeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe,null,false);
        init();
        callRecipeApi();
        return rootView;
    }
    private void init(){
        //tvTest = rootView.findViewById(R.id.tv_test);
        lvRecipe = rootView.findViewById(R.id.lv_recipe);
        ivRecipe = rootView.findViewById(R.id.iv_recipe);
        tvIng = rootView.findViewById(R.id.tv_ing);
        flRecipe = rootView.findViewById(R.id.fl_recipe);

    }
    private void callRecipeApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mocki.io/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeService api = retrofit.create(RecipeService.class);
        api.recipes().enqueue(new Callback<RecipesResponse>() {
            @Override
            public void onResponse(Call<RecipesResponse> call, Response<RecipesResponse> response) {
                menu = response.body().getResult();
                int i;
                int num = menu.size();
                    for (Menu m : menu){
                        try {
                            int count = menu.get(id-1).getIngredients().size();
                            tvIng.setText(String.format("Ingredients(%s)",count));
                            ingredients = (ArrayList<String>)menu.get(id-1).getIngredients();
                            Glide.with(getContext())
                                    .load(menu.get(id-1).getImageUrl())
                                    .centerCrop()
                                    //.placeholder(R.drawable.mini_pizza)
                                    .into(ivRecipe);
                        }catch (Exception exception){
                            Log.d("AAA", "onResponse: ");
                        }

                    }

            }

            @Override
            public void onFailure(Call<RecipesResponse> call, Throwable t) {

            }
        });
    }
}
