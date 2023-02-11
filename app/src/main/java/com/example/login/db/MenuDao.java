package com.example.login.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.login.model.Menu;

import java.util.List;


@Dao
    public interface MenuDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(List<Menu> menu);

        @Query("SELECT * FROM menu WHERE _id = :id GROUP BY ingredients")

        Menu getMenu(int id);
}
