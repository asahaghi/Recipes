package com.example.login.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.example.login.api.RecipeService;
import com.example.login.api.RecipesResponse;
import com.example.login.core.Constant;
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
    private TextView tvTest;
    SharedPreferences sharedPreferences;
    private List<Menu> menu = new ArrayList<>();
    private ImageView ivRecipe,ivPlay;
    private TextView tvIng;
    private FrameLayout flRecipe;
    ArrayList<String> ingredients;
    private ProgressBar pbLoading;
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
        flRecipe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=bzg_BU39y2g");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

            }
        });
        return rootView;
    }
    private void init(){
        sharedPreferences = getContext().getSharedPreferences(Constant.MY_PREFS_NAME,MODE_PRIVATE);
        tvTest = rootView.findViewById(R.id.tv_test);
        lvRecipe = rootView.findViewById(R.id.lv_recipe);
        ivRecipe = rootView.findViewById(R.id.iv_recipe);
        tvIng = rootView.findViewById(R.id.tv_ing);
        flRecipe = rootView.findViewById(R.id.fl_recipe);
        ivPlay = rootView.findViewById(R.id.iv_play);
        pbLoading = rootView.findViewById(R.id.pb_loading);

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
                pbLoading.setVisibility(View.GONE);
                ivPlay.setVisibility(View.VISIBLE);
                tvIng.setVisibility(View.VISIBLE);
                menu = response.body().getResult();
                int i;
                int num = menu.size();
                    for (Menu m : menu){
                        try {
                            int count1 = menu.get(id-1).getIngredients().size();
                            tvIng.setText(String.format("Ingredients(%s)",count1));
                            ingredients = (ArrayList<String>)menu.get(id-1).getIngredients();
                            Glide.with(getContext())
                                    .load(menu.get(id-1).getImageUrl())
                                    .centerCrop()
                                    //.placeholder(R.drawable.mini_pizza)
                                    .into(ivRecipe);
                        }catch (Exception exception){
                            Log.d("AAA", "onResponse: ");
                        }
                        refreshDisplay();

                    }

            }

            @Override
            public void onFailure(Call<RecipesResponse> call, Throwable t) {

            }
        });
    }

    private void refreshDisplay(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, ingredients);
        /*int count = adapter.getCount();
        adapter.remove(adapter.getItem(count - 1));*/
        lvRecipe.setAdapter(adapter);
    }
}
