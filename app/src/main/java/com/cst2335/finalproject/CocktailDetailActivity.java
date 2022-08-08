package com.cst2335.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CocktailDetailActivity extends AppCompatActivity {
    private ImageButton imgView;
    private TextView cocktailName;
    private TextView instructionView;
    private TextView ingredient1View;
    private TextView ingredient2View;
    private TextView ingredient3View;
    private Bitmap cocktailImage;
    private ImageView cocktailDetailImage;
    private Button favoriteButton;
    private SQLiteDatabase db;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_detail);

        cocktailName = (TextView) findViewById(R.id.cocktailDetailname);
        instructionView = (TextView) findViewById(R.id.detail_instructions_text);
        ingredient1View = (TextView) findViewById(R.id.detail_ingredient_1);
        ingredient2View = (TextView) findViewById(R.id.detail_ingredient_2);
        ingredient3View = (TextView) findViewById(R.id.detail_ingredient_3);
        cocktailDetailImage = (ImageView) findViewById(R.id.detail_cocktail_image);
        favoriteButton = (Button) findViewById(R.id.favorite_button);
        relativeLayout = (RelativeLayout) findViewById(R.id.cocktail_detail);

        Intent list = getIntent();
        String name = list.getStringExtra("NAME");
        String image = list.getStringExtra("IMAGE");
        String instruction = list.getStringExtra("INSTRUCTION");
        String ingredient1 = list.getStringExtra("Ingredient1");
        String ingredient2 = list.getStringExtra("Ingredient2");
        String ingredient3 = list.getStringExtra("Ingredient3");

        String filename = image.substring(image.lastIndexOf("/")+1).trim();


        cocktailName.setText(name);
        instructionView.setText(instruction);
        ingredient1View.setText(ingredient1);
        ingredient2View.setText(ingredient2);
        ingredient3View.setText(ingredient3);

        FavoriteDatabase dbOpener = new FavoriteDatabase(this);
        db = dbOpener.getWritableDatabase();
        favoriteButton.setOnClickListener(view -> {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(FavoriteDatabase.COL_IMAGE, filename);
            newRowValues.put(FavoriteDatabase.COL_NAME,name);
            newRowValues.put(FavoriteDatabase.COL_INSTRUCTION,instruction);
            newRowValues.put(FavoriteDatabase.COL_INGREDIENT_1,ingredient1);
            newRowValues.put(FavoriteDatabase.COL_INGREDIENT_2,ingredient2);
            newRowValues.put(FavoriteDatabase.COL_INGREDIENT_3,ingredient3);
            db.insert(FavoriteDatabase.TABLE_NAME, null, newRowValues);
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "Saved favorite "+ name, Snackbar.LENGTH_LONG).setAction("Ok", view1 -> {
                        return;
                    });
            snackbar.show();

        });


        LoadImageInBackGround querry = new LoadImageInBackGround();
        querry.execute(image);


    }


    public class LoadImageInBackGround extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... args) {
            String filename = args[0].substring(args[0].lastIndexOf("/")+1);
            System.out.println(filename);

            File file = getBaseContext().getFileStreamPath(filename);
            if (!file.exists()) {
               saveImage(args[0]);
            }else {
                Log.i("Cocktails", "Saved icon, " + filename + " is displayed.");
                try {
                    FileInputStream in = new FileInputStream(file);
                    cocktailImage = BitmapFactory.decodeStream(in);

                } catch (FileNotFoundException e) {
                    Log.i("WeatherForecast", "Saved icon, " + filename + " is not found.");
                }
            }
            return "Done";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cocktailDetailImage = (ImageView) findViewById(R.id.detail_cocktail_image);
            cocktailDetailImage.setImageBitmap(cocktailImage);
        }
    }

    private void saveImage(String URL) {
        HttpURLConnection connection = null;
        try {
            java.net.URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            String filename = URL.substring(URL.lastIndexOf("/")+1).trim();

            cocktailImage = BitmapFactory.decodeStream(connection.getInputStream());
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            cocktailImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            outputStream.flush();
            outputStream.close();
            Log.i("WeatherForecast", "Weather icon, " + filename + " is downloaded and displayed.");
        } catch (Exception e) {
            Log.i("WeatherForecast", "weather icon download error: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    }

