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
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.example.login.api.RecipeService;
import com.example.login.api.RecipesResponse;
import com.example.login.core.Constant;
import com.example.login.db.AppDatabase;
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
    SharedPreferences sharedPreferences;
    private List<Menu> menu = new ArrayList<>();
    private ImageView ivRecipe,ivPlay;
    private TextView tvIng,tvComment;
    private FrameLayout flRecipe;
    ArrayList<String> ingredients;
    String youtubeUrl;
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

        lvRecipe = rootView.findViewById(R.id.lv_recipe);
        ivRecipe = rootView.findViewById(R.id.iv_recipe);
        tvIng = rootView.findViewById(R.id.tv_ing);
        flRecipe = rootView.findViewById(R.id.fl_recipe);

        init();
        refreshDisplay();

        flRecipe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(youtubeUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

            }
        });
        return rootView;
    }
    private void init(){
        sharedPreferences = getContext().getSharedPreferences(Constant.MY_PREFS_NAME,MODE_PRIVATE);
        AppDatabase db = Room.databaseBuilder(getContext(),AppDatabase.class, "recipes.db")
                .allowMainThreadQueries()
                .build();

        Menu menu = db.menuDao().getMenu(id);

        //tv1 = rootView.findViewById(R.id.tv_1);

        String title = db.menuDao().getMenu(id).getTitle();

        //Toast.makeText(getContext(), "this is "+ , Toast.LENGTH_SHORT).show();

        ingredients = (ArrayList<String>) db.menuDao().getMenu(id).getIngredients();

        ingredients.add(String.valueOf(lvRecipe));
        int count = db.menuDao().getMenu(id).getIngredients().size();

        tvIng.setText(String.format("Ingredients(%s)",count));


        youtubeUrl = db.menuDao().getMenu(id).getYoutubeUrl();



        Glide.with(getContext())
                .load(db.menuDao().getMenu(id).getImageUrl())
                .centerCrop()
                //.placeholder(R.drawable.mini_pizza)
                .into(ivRecipe);


        tvComment = rootView.findViewById(R.id.tv_comment);
        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)requireActivity()).addFragment(new CommentFragment(id));
            }
        });

    }


    private void refreshDisplay(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, ingredients);
        int count = adapter.getCount();
        adapter.remove(adapter.getItem(count - 1));
        lvRecipe.setAdapter(adapter);
    }
}
