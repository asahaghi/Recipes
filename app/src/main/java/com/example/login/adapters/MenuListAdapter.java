
package com.example.login.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.login.R;
import com.example.login.model.Menu;
import com.example.login.view.MainActivity;


import java.util.List;

public abstract class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder> {

    private Context context;
    private List<Menu> menu;


    public MenuListAdapter(Context context, List<Menu> menu){

        this.context = context;
        this.menu = menu;
    }

    @NonNull
    @Override
    public MenuListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_menu,null,false);

        return new MenuListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuListViewHolder holder, int position) {
        position = holder.getAdapterPosition();

        Glide.with(context)
                .load(menu.get(position).getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.mini_pizza)
                .into(holder.ivPoster);



        holder.tvMenu.setText(menu.get(position).getTitle());
        holder.tvDetails.setText(menu.get(position).getDetails());

        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(context,"Item at position"+ holder.getAdapterPosition() +"clicked",Toast.LENGTH_SHORT).show();
                onItemClick(menu.get(holder.getAdapterPosition()).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class MenuListViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenu, tvDetails;
        ImageView ivPoster;
        LinearLayout llParent;
        CardView cvMenu;
        View rootView;
        public MenuListViewHolder(@NonNull View itemView) {
            super(itemView);
            llParent = itemView.findViewById(R.id.ll_parent);
            tvMenu = itemView.findViewById(R.id.tv_menu);
            tvDetails = itemView.findViewById(R.id.tv_details);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            cvMenu = itemView.findViewById(R.id.cv_menu);
        }
    }
    public abstract void onItemClick(int id);
}

