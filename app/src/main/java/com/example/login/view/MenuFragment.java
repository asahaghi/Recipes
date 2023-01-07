package com.example.login.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.adapters.MenuListAdapter;
import com.example.login.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    private View rootView;
    private RecyclerView rvMenu;
    private List<Menu> allMenu = new ArrayList<>();
    private MenuListAdapter menuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu,null,false);
        CardView cvMenu = rootView.findViewById(R.id.cv_menu);

        rootView.setFocusable(true);
        rootView.setFocusableInTouchMode(true); rootView.setClickable(true);

        init();

        return rootView;
    }

    private void init(){
        rvMenu = rootView.findViewById(R.id.rv_menu);
        rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Menu> menu = new ArrayList<>();

        Menu m1 = new Menu("Apple","1 ingredient | 1 Min");
        Menu m2 = new Menu("Pasta","15 ingredients | 30 Min");
        m1.setImageUrl("https://images.immediate.co.uk/production/volatile/sites/30/2020/08/gnocchi-1d16725.jpg?resize=960,872?quality=90&webp=true&resize=375,341");
        m2.setImageUrl("https://www.seekpng.com/png/detail/834-8346546_french-fries-45-french-fries.png");

        menu.add(m1);
        menu.add(m2);


        rvMenu.setAdapter(new MenuListAdapter(getContext(), menu) {
            @Override
            public void onItemClick(int id) {

            }
        });
    }
}
