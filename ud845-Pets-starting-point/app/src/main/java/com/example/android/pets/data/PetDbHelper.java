package com.example.android.pets.data;

import static com.example.android.pets.data.PetsContract.PetsEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for Pets app. Manages database creation and version management.
 */
public class PetDbHelper extends SQLiteOpenHelper {

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    public final static int DATABASE_VERSION = 1;

    /** Name of the database file */
    public final static String DATABASE_NAME = "pets.db";

    /**
     * Constructs a new instance of {@link PetDbHelper}.
     *
     * @param context of the app
     */
    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PetsEntry.TABLE_NAME + " ("
                + PetsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetsEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetsEntry.COLUMN_PET_BREED + " TEXT, "
                + PetsEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetsEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

}
