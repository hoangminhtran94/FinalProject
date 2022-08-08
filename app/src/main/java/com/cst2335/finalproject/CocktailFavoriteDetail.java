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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CocktailFavoriteDetail extends AppCompatActivity {
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
        String ingredient1 = list.getStringExtra("INGREDIENT1");
        String ingredient2 = list.getStringExtra("INGREDIENT2");
        String ingredient3 = list.getStringExtra("INGREDIENT3");

        File file = getBaseContext().getFileStreamPath(image);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            cocktailImage = BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        cocktailDetailImage.setImageBitmap(cocktailImage);
        cocktailName.setText(name);
        instructionView.setText(instruction);
        ingredient1View.setText(ingredient1);
        ingredient2View.setText(ingredient2);
        ingredient3View.setText(ingredient3);


    }
}


