package com.cst2335.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class FavoriteFragment extends Fragment {
    ArrayList<CocktailModel> favoriteCocktails;
    private SQLiteDatabase db;
    Fragment cocktailFavoriteDetailFragment;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_cocktails_favorite, container, false);
        favoriteCocktails = new ArrayList<CocktailModel>();

        cocktailFavoriteDetailFragment = new CocktailFavoriteDetailFragment();
        loadDataFromDataBase();

        FavoriteCocktailsArrayAdapter favoriteCocktailsArrayAdapter = new FavoriteCocktailsArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, favoriteCocktails);
        ListView favoriteCocktailList = (ListView) view.findViewById(R.id.favoriteCocktailList);
        favoriteCocktailList.setAdapter(favoriteCocktailsArrayAdapter);

        favoriteCocktailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("NAME",favoriteCocktails.get(position).name);
                bundle.putString("IMAGE",favoriteCocktails.get(position).image);
                bundle.putString("INSTRUCTION",favoriteCocktails.get(position).instruction);
                bundle.putString("Ingredient1",favoriteCocktails.get(position).ingredient1);
                bundle.putString("Ingredient2",favoriteCocktails.get(position).ingredient2);
                bundle.putString("Ingredient3",favoriteCocktails.get(position).ingredient3);
                cocktailFavoriteDetailFragment.setArguments(bundle);
                loadFragment(cocktailFavoriteDetailFragment);


            }
        });

        favoriteCocktailList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                FavoriteDatabase dbOpener = new FavoriteDatabase(getActivity());
                dbOpener.deleteEntry(favoriteCocktails.get(i).name);
                favoriteCocktails.remove(i);
                favoriteCocktailsArrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return view;
    }

    public void loadDataFromDataBase() {
        FavoriteDatabase dbOpener = new FavoriteDatabase(getActivity());
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {FavoriteDatabase.COL_ID, FavoriteDatabase.COL_NAME, FavoriteDatabase.COL_IMAGE, FavoriteDatabase.COL_INSTRUCTION, FavoriteDatabase.COL_INGREDIENT_1, FavoriteDatabase.COL_INGREDIENT_2, FavoriteDatabase.COL_INGREDIENT_3};
        //query all the results from the database:
        Cursor results = db.query(false, FavoriteDatabase.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:

        int idColIndex = results.getColumnIndex(FavoriteDatabase.COL_ID);
        int nameColIndex = results.getColumnIndex(FavoriteDatabase.COL_NAME);
        int imageColIndex = results.getColumnIndex(FavoriteDatabase.COL_IMAGE);
        int instructionColIndex = results.getColumnIndex(FavoriteDatabase.COL_INSTRUCTION);
        int ingredient1ColIndex = results.getColumnIndex(FavoriteDatabase.COL_INGREDIENT_1);
        int ingredient2ColIndex = results.getColumnIndex(FavoriteDatabase.COL_INGREDIENT_2);
        int ingredient3ColIndex = results.getColumnIndex(FavoriteDatabase.COL_INGREDIENT_3);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String name = results.getString(nameColIndex);
            String image = results.getString(imageColIndex);
            String instruction = results.getString(instructionColIndex);
            String ingredient1 = results.getString(ingredient1ColIndex);
            String ingredient2 = results.getString(ingredient2ColIndex);
            String ingredient3 = results.getString(ingredient3ColIndex);

            //add the new Contact to the array list:
            favoriteCocktails.add(new CocktailModel(name, image, instruction, ingredient1, ingredient2, ingredient3));


        }


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
