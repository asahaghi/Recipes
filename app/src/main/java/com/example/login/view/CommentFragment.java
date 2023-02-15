package com.example.login.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.adapters.CommentAdapter;
import com.example.login.core.Constant;
import com.example.login.model.Comment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {
    View rootView;
    SharedPreferences sharedPreferences;
    private int id;
    private TextView tvTitle,tvAdd;
    private EditText etComment;
    public CommentFragment(int id) {
        this.id = id;
    }
    BottomNavigationView navBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_comment,null,false);
        init();
        return rootView;
    }
    private void init(){
        sharedPreferences = getContext().getSharedPreferences(Constant.MY_PREFS_NAME,MODE_PRIVATE);
        RecyclerView rvComment = rootView.findViewById(R.id.rv_comment);
        rvComment.setLayoutManager(new LinearLayoutManager(getContext()));
        navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        List<Comment> comments = new ArrayList<>();
        /*if (id==4){
            comments.add(new Comment(id,"ahmad","kheili khub"));
            comments.add(new Comment(id,"ali","kheili bad"));
        }*/
        etComment = rootView.findViewById(R.id.et_comment);
        tvAdd = rootView.findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etComment.getText().length()>3 && etComment.getText()!=null && etComment.getText().toString().indexOf(" ")!=0 ){
                    comments.add(new Comment(id,sharedPreferences.getString("name",""),etComment.getText().toString()));
                    etComment.getText().clear();
                    etComment.clearFocus();


                }
            }
        });



        rvComment.setAdapter(new CommentAdapter(getContext(),comments));
        tvTitle = rootView.findViewById(R.id.tv_title);

    }
}
