package com.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class SearchFragment extends Fragment {

    EditText searchCocktailText;
    Button btnSearchByName;
    Button btnSearchByFirstChar;
    Fragment cocktailCategoriesFragment;
    private SharedPreferences preferences;
    
    private SQLiteDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_search, container, false);

        searchCocktailText = (EditText) view.findViewById(R.id.search_cocktail);
        btnSearchByName = (Button) view.findViewById(R.id.btn_search_by_name);
        btnSearchByFirstChar = (Button) view.findViewById(R.id.btn_search_by_character);
        cocktailCategoriesFragment = new CocktailCategoriesFragment();
        btnSearchByFirstChar.setOnClickListener(view12 -> {
            loadFragment(cocktailCategoriesFragment);
        });


        btnSearchByName.setOnClickListener(view1 -> {
            preferences = this.getActivity().getSharedPreferences("cocktail", Context.MODE_PRIVATE);
            String searchQuery = preferences.getString("searchquery","");
            SharedPreferences.Editor ed = preferences.edit();
            String search = searchCocktailText.getText().toString().trim();
            ed.putString("searchquery",search).apply();

            if(search.isEmpty()) {
                searchCocktailText.setText(searchQuery);
            }
            else {

            MyOpener dbOpener = new MyOpener(getActivity());
            db = dbOpener.getWritableDatabase();
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(() -> {
            try {
                URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + search);
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
        return view;
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
