package com.cst2335.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CocktailDetailFragment extends Fragment {
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.cocktail_detail_fragment, container, false);


        cocktailName = (TextView) view.findViewById(R.id.cocktailDetailname);

        instructionView = (TextView) view.findViewById(R.id.detail_instructions_text);
        ingredient1View = (TextView) view.findViewById(R.id.detail_ingredient_1);
        ingredient2View = (TextView) view.findViewById(R.id.detail_ingredient_2);
        ingredient3View = (TextView) view.findViewById(R.id.detail_ingredient_3);
        cocktailDetailImage = (ImageView) view.findViewById(R.id.detail_cocktail_image);
        favoriteButton = (Button) view.findViewById(R.id.favorite_button);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.cocktail_detail);


        Bundle fromCocktailList = this.getArguments();
        if(fromCocktailList != null){


        String name = fromCocktailList.getString("NAME");
        String image = fromCocktailList.getString("IMAGE");
        String instruction = fromCocktailList.getString("INSTRUCTION");
        String ingredient1 = fromCocktailList.getString("Ingredient1");
        String ingredient2 = fromCocktailList.getString("Ingredient2");
        String ingredient3 = fromCocktailList.getString("Ingredient3");
        String filename = image.substring(image.lastIndexOf("/")+1).trim();


        cocktailName.setText(name);
        instructionView.setText(instruction);
        ingredient1View.setText(ingredient1);
        ingredient2View.setText(ingredient2);
        ingredient3View.setText(ingredient3);

        FavoriteDatabase dbOpener = new FavoriteDatabase(getActivity());
        db = dbOpener.getWritableDatabase();
        favoriteButton.setOnClickListener(view1 -> {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(FavoriteDatabase.COL_IMAGE, filename);
            newRowValues.put(FavoriteDatabase.COL_NAME,name);
            newRowValues.put(FavoriteDatabase.COL_INSTRUCTION,instruction);
            newRowValues.put(FavoriteDatabase.COL_INGREDIENT_1,ingredient1);
            newRowValues.put(FavoriteDatabase.COL_INGREDIENT_2,ingredient2);
            newRowValues.put(FavoriteDatabase.COL_INGREDIENT_3,ingredient3);
            db.insert(FavoriteDatabase.TABLE_NAME, null, newRowValues);
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "Saved favorite "+ name, Snackbar.LENGTH_LONG).setAction("Ok", view2 -> {
                        return;
                    });
            snackbar.show();

        });
            LoadImageInBackGround querry = new LoadImageInBackGround();
            querry.execute(image);
        }




        return  view;
    }


    public class LoadImageInBackGround extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... args) {
            String filename = args[0].substring(args[0].lastIndexOf("/")+1);
            System.out.println(filename);

            File file = getActivity().getBaseContext().getFileStreamPath(filename);
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
            FileOutputStream outputStream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
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

