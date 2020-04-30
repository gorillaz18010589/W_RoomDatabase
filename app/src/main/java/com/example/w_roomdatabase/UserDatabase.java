package com.example.w_roomdatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = User.class,version = 1)
public abstract class UserDatabase extends RoomDatabase {
    abstract UserDao userDao();
}
