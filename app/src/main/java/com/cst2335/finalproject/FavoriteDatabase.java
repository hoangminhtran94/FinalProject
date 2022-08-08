package com.cst2335.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDatabase extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "favoriteCocktails";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "FAVORITE_COCKTAILS";
    public final static String COL_NAME = "COCKTAILNAME";
    public final static String COL_INSTRUCTION = "INSTRUCTIONS";
    public final static String COL_IMAGE = "IMAGE";
    public final static String COL_INGREDIENT_1 = "INGREDIENT1";
    public final static String COL_INGREDIENT_2 = "INGREDIENT2";
    public final static String COL_INGREDIENT_3 = "INGREDIENT3";
    public final static String COL_ID = "_id";

    public FavoriteDatabase(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " text,"
                + COL_IMAGE + " text ,"
                + COL_INGREDIENT_1 + " text,"
                + COL_INGREDIENT_2 + " text,"
                + COL_INGREDIENT_3 + " text,"
                + COL_INSTRUCTION  + " text);");  // add or remove columns
    }


    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    public void deleteEntry(String name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, COL_NAME + "=" +"\""+ name+"\"", null);
    }


    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}