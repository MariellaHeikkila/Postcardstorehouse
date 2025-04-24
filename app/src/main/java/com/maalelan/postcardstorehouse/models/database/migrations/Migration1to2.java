package com.maalelan.postcardstorehouse.models.database.migrations;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration1to2 extends Migration {
    public Migration1to2(){
        super(1, 2); //version 1 --> 2
    }

    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // example: Add new column "favorite" to PPostcard table
        database.execSQL("ALTER TABLE postcard ADD COLUMN favorite INTEGER NOT NULL DEFAULT 0");
    }
}
