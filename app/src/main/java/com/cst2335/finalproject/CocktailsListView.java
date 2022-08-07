package com.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CocktailsListView extends AppCompatActivity{

    ArrayList<CocktailModel> currentCocktailsList;
    ArrayList<String> cocktailsNames;

    private MyListAdapter adapter;
    private ListView cocktailsListView;
    private TextView mResultsTextView;
    private SQLiteDatabase db;
    static Toast t;
    static Context context;
    Bitmap cocktailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktails_list_view);

        currentCocktailsList = new ArrayList<CocktailModel>();
        cocktailsNames = new ArrayList<String>();
        cocktailsListView = (ListView) findViewById((R.id.cocktail_list));


        loadDataFromDatabase();
        adapter = new MyListAdapter(this, currentCocktailsList);
        cocktailsListView.setItemsCanFocus(true);
        cocktailsListView.setClickable(true);

        mResultsTextView = (TextView) findViewById(R.id.search_results);
        cocktailsListView.setClickable(true);
        cocktailsListView.setAdapter(adapter);





        Intent intent = getIntent();
        String results = intent.getStringExtra("search");
        mResultsTextView.setText("Cocktails Found for: " + results);

    }

    @Override
    public void onBackPressed() {
        db.execSQL("delete from " + "COCKTAILS");
        currentCocktailsList.clear();
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> CocktailsListView.super.onBackPressed()).create().show();
    }


    private void loadDataFromDatabase() {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {MyOpener.COL_ID, MyOpener.COL_NAME, MyOpener.COL_IMAGE, MyOpener.COL_INSTRUCTION, MyOpener.COL_INGREDIENT_1, MyOpener.COL_INGREDIENT_2, MyOpener.COL_INGREDIENT_3};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:

        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int nameColIndex = results.getColumnIndex(MyOpener.COL_NAME);
        int imageColIndex = results.getColumnIndex(MyOpener.COL_IMAGE);
        int instructionColIndex = results.getColumnIndex(MyOpener.COL_INSTRUCTION);
        int ingredient1ColIndex = results.getColumnIndex(MyOpener.COL_INGREDIENT_1);
        int ingredient2ColIndex = results.getColumnIndex(MyOpener.COL_INGREDIENT_2);
        int ingredient3ColIndex = results.getColumnIndex(MyOpener.COL_INGREDIENT_3);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String name = results.getString(nameColIndex);
            String image = results.getString(imageColIndex);
            String instruction = results.getString(instructionColIndex);
            String ingredient1 = results.getString(ingredient1ColIndex);
            String ingredient2 = results.getString(ingredient2ColIndex);
            String ingredient3 = results.getString(ingredient3ColIndex);
            String filename = image.substring(image.lastIndexOf("/")+1).trim();


            //add the new Contact to the array list:
            currentCocktailsList.add(new CocktailModel(name, image, instruction, ingredient1, ingredient2, ingredient3));


        }
    }

    private static void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        t.show();
    }


}
