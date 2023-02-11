package com.example.login.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.login.model.Menu;

@Database(entities = {Menu.class} , version = 1)
public abstract class AppDatabase extends RoomDatabase {



    public abstract MenuDao menuDao();
}
