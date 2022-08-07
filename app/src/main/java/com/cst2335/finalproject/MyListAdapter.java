package com.cst2335.finalproject;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

public class MyListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CocktailModel> cocktails;
    private Bitmap cocktailImage;
    ImageView image;


    static Toast t;


    public MyListAdapter(Context context, ArrayList<CocktailModel> cocktails) {
        this.context = context;
        this.cocktails = cocktails;
    }


    @Override
    public int getCount() {
        return cocktails.size();
    }

    @Override
    public Object getItem(int position) {
        return cocktails.get(position);
    }

    //    public int getType(int position) {
//        return cocktails.get(position).getType();
//    }
    public String getName(int position) {
        return cocktails.get(position).getName();
    }

    public String getImage(int position) {
        return cocktails.get(position).getImage();
    }

    public String getInstruction(int position) {
        return  cocktails.get(position).getInstruction();
    }

    public String getIngredient1(int position) {
        return  cocktails.get(position).getIngredient1();
    }

    public String getIngredient2(int position) {
        return  cocktails.get(position).getIngredient2();
    }

    public String getIngredient3(int position) {
        return  cocktails.get(position).getIngredient3();
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView;

//        LayoutInflater inflater = (LayoutInflater) this.getSystemService;

        newView = LayoutInflater.from(context).inflate(R.layout.cocktailslist_list_cell, parent, false);
        TextView name = newView.findViewById(R.id.categoryNameCellDetail);
        image = newView.findViewById(R.id.categoryImageCellDetail);;

        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(( ) -> {
            String filename = getImage(position).substring(getImage(position).lastIndexOf("/")+1);

            File file = context.getFileStreamPath(filename);
            if (!file.exists()) {
                saveImage(getImage(position));
            }else {
                Log.i("Cocktails", "Saved icon, " + filename + " is displayed.");
                try {
                    FileInputStream in = new FileInputStream(file);
                    cocktailImage = BitmapFactory.decodeStream(in);

                } catch (FileNotFoundException e) {
                    Log.i("WeatherForecast", "Saved icon, " + filename + " is not found.");
                }
            }
        });

        image.setImageBitmap(cocktailImage);




            Button button = newView.findViewById(R.id.detailButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Button","Button");
                Intent intent= new Intent(context, CocktailDetailActivity.class);
                intent.putExtra("NAME",getName(position));
                intent.putExtra("IMAGE",getImage(position));
                intent.putExtra("INSTRUCTION",getInstruction(position));
                intent.putExtra("Ingredient1",getIngredient1(position));
                intent.putExtra("Ingredient2",getIngredient2(position));
                intent.putExtra("Ingredient3",getIngredient3(position));
                context.startActivity(intent);
            }
        });
        name.setText(getName(position));
        name.setClickable(true);
        newView.setClickable(true);


        return newView;
    }
    private void saveImage(String URL) {
        HttpURLConnection connection = null;
        try {
            java.net.URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            String filename = URL.substring(URL.lastIndexOf("/")+1).trim();

            cocktailImage = BitmapFactory.decodeStream(connection.getInputStream());
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
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



