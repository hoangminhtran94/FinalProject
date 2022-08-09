package com.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CocktailsListFragment extends Fragment{

    ArrayList<CocktailModel> currentCocktailsList;
    ArrayList<String> cocktailsNames;

    private MyListAdapter adapter;
    private ListView cocktailsListView;
    private TextView mResultsTextView;
    private SQLiteDatabase db;
    Fragment searchFragment;
    Fragment cocktailDetailFragment;
    Bitmap cocktailImage;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ocktails_list_view_fragment, container, false);


        context = getContext();
        currentCocktailsList = new ArrayList<CocktailModel>();
        cocktailsNames = new ArrayList<String>();
        cocktailsListView = view.findViewById(R.id.cocktail_list);


        loadDataFromDatabase();

        cocktailDetailFragment = new CocktailDetailFragment();
        searchFragment = new SearchFragment();
        adapter = new MyListAdapter(getActivity(), currentCocktailsList);
        cocktailsListView.setItemsCanFocus(true);
        cocktailsListView.setClickable(true);

        mResultsTextView = (TextView) view.findViewById(R.id.search_results);
        cocktailsListView.setClickable(true);
        cocktailsListView.setAdapter(adapter);

        cocktailsListView.setOnItemClickListener((adapterView, view1, i, l) -> {

            Bundle bundle = new Bundle();
            bundle.putString("NAME",currentCocktailsList.get(i).name);
            bundle.putString("IMAGE",currentCocktailsList.get(i).image);
            bundle.putString("INSTRUCTION",currentCocktailsList.get(i).instruction);
            bundle.putString("Ingredient1",currentCocktailsList.get(i).ingredient1);
            bundle.putString("Ingredient2",currentCocktailsList.get(i).ingredient2);
            bundle.putString("Ingredient3",currentCocktailsList.get(i).ingredient3);
            cocktailDetailFragment.setArguments(bundle);
            loadFragment(cocktailDetailFragment);
                });





        Bundle fromSearch = this.getArguments();
        if(fromSearch != null){
        String results = fromSearch.getString("search");
        mResultsTextView.setText("Cocktails Found for: " + results);}
        return view;
    }



    private void loadDataFromDatabase() {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(getActivity());
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

    private void makeToast(String s) {
       Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.delete(MyOpener.TABLE_NAME,null,null);
        makeToast("Exiting...");
//        new AlertDialog.Builder(getActivity())
//                .setTitle("Really Exit?")
//                .setMessage("Are you sure you want to exit?")
//                .setNegativeButton(android.R.string.no, null)
//                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
//                    makeToast("Exiting...");
//                }).create().show();
    }

}
