package com.example.login.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.R;
import com.example.login.adapters.MenuListAdapter;
import com.example.login.api.RecipeService;
import com.example.login.api.RecipesResponse;
import com.example.login.db.AppDatabase;
import com.example.login.model.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuFragment extends Fragment {
    private View rootView;
    private RecyclerView rvMenu;
    private AppCompatEditText etSearch;
    private List<Menu> allMenu = new ArrayList<>();
    private List<Menu> filteredMenu = new ArrayList<>();
    private MenuListAdapter menuAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu,null,false);
        CardView cvMenu = rootView.findViewById(R.id.cv_menu);

        rootView.setFocusable(true);
        rootView.setFocusableInTouchMode(true); rootView.setClickable(true);

        init();

        callRecipesApi();



        return rootView;
    }

    private void init(){
        rvMenu = rootView.findViewById(R.id.rv_menu);
        rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));

        etSearch = rootView.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

    private void filter(String searchText){
        int i;
        filteredMenu.clear();
        for (i=0 ; i<allMenu.size() ; i++){
            if (allMenu.get(i).getTitle().toLowerCase().contains(searchText.toLowerCase())){
                filteredMenu.add(allMenu.get(i));
            }
        }
        menuAdapter.notifyDataSetChanged();
    }

    public void callRecipesApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mocki.io/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeService api = retrofit.create(RecipeService.class);
        api.recipes().enqueue(new Callback<RecipesResponse>() {
            @Override
            public void onResponse(Call<RecipesResponse> call, Response<RecipesResponse> response) {
                rootView.findViewById(R.id.cv_search).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.tv_first).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.pb_loading).setVisibility(View.GONE);

                // TODO: 7/15/2022 insertToDataBase
                //<editor-fold desc="add menu to database">
                AppDatabase db = Room.databaseBuilder(getContext(),AppDatabase.class, "recipes.db")
                       .allowMainThreadQueries()
                        .build();
                db.menuDao().insert(response.body().getResult());
                //</editor-fold>

                allMenu = response.body().getResult();
                filteredMenu.addAll(allMenu);
                menuAdapter = new MenuListAdapter(getContext(),filteredMenu) {
                    @Override
                    public void onItemClick(int id) {
                        
                        ((MainActivity)requireActivity()).addFragment(new RecipeFragment(id));
                    }
                };
                rvMenu.setAdapter(menuAdapter);
            }

            @Override
            public void onFailure(Call<RecipesResponse> call, Throwable t) {
               // Log.d("AAA", "onFailure: ");
            }
        });

    }


}
