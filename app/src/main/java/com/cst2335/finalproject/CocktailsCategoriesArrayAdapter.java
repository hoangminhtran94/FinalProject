package com.cst2335.finalproject;
import android.content.Context;
import android.widget.ArrayAdapter;

public class CocktailsCategoriesArrayAdapter extends ArrayAdapter {
    private Context context;
    private String[] characters;

    public CocktailsCategoriesArrayAdapter(Context context, int resource, String[] characters){
        super(context, resource);
        this.context = context;
        this.characters = characters;
    }

    @Override
    public int getCount(){
        return characters.length;
    }
    @Override
    public Object getItem(int position){
        String character = characters[position];
        return String.format(character + "\n" );
    }

}