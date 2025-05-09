package com.maalelan.postcardstorehouse.models.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.maalelan.postcardstorehouse.models.database.dao.PostcardDao;
import com.maalelan.postcardstorehouse.models.database.dao.PostcardImageDao;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardEntity;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardImageEntity;
import com.maalelan.postcardstorehouse.models.database.migrations.Migration1to2;

@Database(entities = {PostcardEntity.class, PostcardImageEntity.class}, version = 3)
public abstract class PostcardDatabase extends RoomDatabase {

    //DAO-method
    public  abstract PostcardDao postcardDao();
    public abstract PostcardImageDao postcardImageDao();

    // Singleton design pattern
    private static PostcardDatabase INSTANCE;

    public static PostcardDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PostcardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PostcardDatabase.class, "postcard_database")
                            .fallbackToDestructiveMigration()
                            //.addMigrations(new Migration1to2()) // for now no need for this
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
// Double-checked locking
// First if check: This checks if the instance has already been created.
// If it is null, the next check is performed, and the block is synchronized.
//
//Synchronization (synchronized): This ensures that only one thread can create the instance at a time.
// Even if multiple threads call getDatabase(),
// synchronization ensures that only one thread performs the database creation.
//
//Second if check inside the synchronized block: After the synchronized block is executed,
// it's possible that another thread has already created the instance.
// The second if check ensures that the database is not created again,
// even if another thread has already executed the creation before this thread.
