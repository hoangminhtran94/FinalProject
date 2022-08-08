package com.cst2335.finalproject;
import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class FavoriteCocktailsArrayAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<CocktailModel> favoriteCocktails;

    public FavoriteCocktailsArrayAdapter(Context context, int resource, ArrayList<CocktailModel> favoriteCocktails){
        super(context, resource);
        this.context = context;
        this.favoriteCocktails = favoriteCocktails;
    }

    @Override
    public int getCount(){
        return favoriteCocktails.size();
    }
    @Override
    public Object getItem(int position){
        String cocktailName = favoriteCocktails.get(position).name;
        return String.format(cocktailName + "\n" );
    }

}