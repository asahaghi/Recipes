package com.example.login.view;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.login.R;
import com.example.login.core.Constant;
import com.example.login.db.AppDatabase;
import com.example.login.model.Menu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostFragment extends Fragment {

    SharedPreferences sharedPreferences;
    private View rootView;
    BottomNavigationView navBar;
    ImageView img;
    Button btnDone , btnUpload;
    EditText etTitle,etIngredients,youtube,etCookTime,etServings;
    String  details,title,ingredients,youtubeUrl;
    String imageUrl = "";
    String cookTime,servings;
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode() == RESULT_OK){
                img.setImageURI(result.getData().getData());
                imageUrl = String.valueOf(result.getData().getData());
            }
        }
    });
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_post,null,false);
        init();


        return rootView;
    }

    private void init(){
        sharedPreferences = getContext().getSharedPreferences(Constant.MY_PREFS_NAME,MODE_PRIVATE);
        AppDatabase db = Room.databaseBuilder(getContext(),AppDatabase.class, "recipes.db")
                .allowMainThreadQueries()
                .build();
        navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        img = rootView.findViewById(R.id.img);
        btnDone = rootView.findViewById(R.id.btn_done);
        btnUpload = rootView.findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startForResult.launch(iGallery);

            }
        });
        etTitle = rootView.findViewById(R.id.et_title);
        etCookTime = rootView.findViewById(R.id.et_cook_time);
        etIngredients = rootView.findViewById(R.id.et_ingredients);
        etServings = rootView.findViewById(R.id.et_servings);
        youtube = rootView.findViewById(R.id.youtube);
        //////////////////////////////////////
        cookTime = String.valueOf(etCookTime.getText());
        servings = String.valueOf(etServings.getText());
        ingredients = String.valueOf(etIngredients.getText());

        List<String> ingArray = Arrays.asList(ingredients.split("/"));
        title = String.valueOf(etTitle.getText());
        youtubeUrl = String.valueOf(youtube.getText());


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = String.valueOf(etTitle.getText());
                details = etServings.getText().toString() +" servings | "+ etCookTime.getText().toString() ;
                cookTime = String.valueOf(etCookTime.getText());
                servings = String.valueOf(etServings.getText());
                ingredients = String.valueOf(etIngredients.getText());
                List<String> ingArray = Arrays.asList(ingredients.split("/"));
                youtubeUrl = String.valueOf(youtube.getText());
                if (youtubeUrl.startsWith("www.")){
                    youtubeUrl = "http://" + youtubeUrl;
                }
                if (isValid(imageUrl,details,youtubeUrl,title,ingredients)) {
                    //Toast.makeText(getContext(), youtubeUrl, Toast.LENGTH_SHORT).show();
                    Menu menu = new Menu();
                    menu.setTitle(title);
                    menu.setDetails(details);
                    menu.setIngredients(ingArray);
                    menu.setAuthor(sharedPreferences.getString("name",""));
                    menu.setYoutubeUrl(youtubeUrl);
                    menu.setImageUrl(imageUrl);

                    db.menuDao().insertMenu(menu);

                    Toast.makeText(getContext(), "recipe is successfully added to the menu", Toast.LENGTH_SHORT).show();
                    ((MainActivity)requireActivity()).replaceFragment(new ProfileFragment());
                }

            }
        });


    }

    private void dbInsert(){

    }

    private boolean isValid(String imageUrl1 , String details1, String youtube1, String title1, String ingredients1){
        imageUrl1 = imageUrl;
        details1 = etServings.getText().toString() + etCookTime.getText().toString();
        youtube1 = String.valueOf(youtube.getText());
        if (youtube1.startsWith("www.")){
            youtube1 = "http://" + youtube1;
        }
        title1 = String.valueOf(etTitle.getText());
        ingredients1 = String.valueOf(etIngredients.getText());
        if (imageUrl1.length()==0 ){
            Toast.makeText(getContext(), "no image set", Toast.LENGTH_SHORT).show();
            return false;
        }else if (details1.length()==0) {
            Toast.makeText(getContext(), "empty details", Toast.LENGTH_SHORT).show();return false;
        }else if (youtube1.length()==0) {
            Toast.makeText(getContext(), "no url set", Toast.LENGTH_SHORT).show();return false;
        }else if (title1.length()==0) {
            Toast.makeText(getContext(), "no title", Toast.LENGTH_SHORT).show();return false;
        }else if (ingredients1.length()==0) {
            Toast.makeText(getContext(), "empty ingredients", Toast.LENGTH_SHORT).show();return false;
        }else if (!youtube1.startsWith("http://www.") && !youtube1.startsWith("https://www.")) {
            Toast.makeText(getContext(), "wrong url", Toast.LENGTH_SHORT).show();return false;
        }else if (ingredients1.split("/").length<2 ) {
            Toast.makeText(getContext(), "recipe should have at least two ingredients", Toast.LENGTH_SHORT).show();return false;
        }
            btnDone.setVisibility(View.VISIBLE);
            return true;
    }





}
