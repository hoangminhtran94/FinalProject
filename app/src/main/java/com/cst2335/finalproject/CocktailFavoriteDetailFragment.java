package com.cst2335.finalproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CocktailFavoriteDetailFragment extends Fragment {
    private ImageButton imgView;
    private TextView cocktailName;
    private TextView instructionView;
    private TextView ingredient1View;
    private TextView ingredient2View;
    private TextView ingredient3View;
    private Bitmap cocktailImage;
    private ImageView cocktailDetailImage;
    private Button favoriteButton;
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
        if(fromCocktailList != null) {


            String name = fromCocktailList.getString("NAME");
            String image = fromCocktailList.getString("IMAGE");
            String instruction = fromCocktailList.getString("INSTRUCTION");
            String ingredient1 = fromCocktailList.getString("Ingredient1");
            String ingredient2 = fromCocktailList.getString("Ingredient2");
            String ingredient3 = fromCocktailList.getString("Ingredient3");

            File file = getActivity().getBaseContext().getFileStreamPath(image);
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
        return view;
    }
}


