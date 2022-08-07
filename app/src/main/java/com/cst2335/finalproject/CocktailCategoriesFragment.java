package com.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CocktailCategoriesFragment extends     Fragment {
    String[] characters = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Z","Y"};
    private SQLiteDatabase db;


    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_cocktails_category, container, false);
        CocktailsCategoriesArrayAdapter cocktailsArrayAdapter = new CocktailsCategoriesArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, characters);
        ListView cocktailsCategories = (ListView) view.findViewById(R.id.cocktailCategories);
        cocktailsCategories.setAdapter(cocktailsArrayAdapter);

        cocktailsCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("Clicked","clicked "+position);
                String search = characters[position];
                MyOpener dbOpener = new MyOpener(getActivity());
                db = dbOpener.getWritableDatabase();
                Executor newThread = Executors.newSingleThreadExecutor();
                newThread.execute(() -> {
                    try {
                        URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/search.php?f=" + search);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        InputStream response = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        String result = sb.toString();
                        JSONObject report = new JSONObject(result);
                        JSONArray cocktailArray = report.getJSONArray("drinks");
                        HashMap<String, String> cocktailList;

                        for (int i = 0; i < cocktailArray.length(); i++) {
                            JSONObject cocktailItem = cocktailArray.getJSONObject(i);
                            String name = cocktailItem.getString("strDrink");
                            String instruction = cocktailItem.getString("strInstructions");
                            String image = cocktailItem.getString("strDrinkThumb");
                            String ingredient1 = cocktailItem.getString("strIngredient1");
                            String ingredient2 = cocktailItem.getString("strIngredient2");
                            String ingredient3 = cocktailItem.getString("strIngredient3");

                            Executor newThread1 = Executors.newSingleThreadExecutor();
                            newThread1.execute(()->{
                                ContentValues newRowValues = new ContentValues();
                                newRowValues.put(MyOpener.COL_NAME,name);
                                newRowValues.put(MyOpener.COL_IMAGE,image);
                                newRowValues.put(MyOpener.COL_INSTRUCTION,instruction);
                                newRowValues.put(MyOpener.COL_INGREDIENT_1,ingredient1);
                                newRowValues.put(MyOpener.COL_INGREDIENT_2,ingredient2);
                                newRowValues.put(MyOpener.COL_INGREDIENT_3,ingredient3);
                                db.insert(MyOpener.TABLE_NAME, null, newRowValues);
                            });


                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        Log.d("Error", e.toString());
                    }
                    Intent intent = new Intent(getActivity(), CocktailsListView.class);
                    intent.putExtra("search", search);
                    startActivity(intent);
                });
            }
        });
        return  view;
    }




}
