package com.cst2335.finalproject;

import android.graphics.Bitmap;


public class CocktailModel {
    String name;
    String image;
    String instruction;
    String ingredient1;
    String ingredient2;
    String ingredient3;

    CocktailModel(String name, String image, String instruction, String ingredient1, String ingredient2, String ingredient3) {
        this.name = name;
        this.image = image;
        this.instruction = instruction;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.ingredient3= ingredient3;
    }

    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }

    public String getIngredient1() {
        return ingredient1;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public String getIngredient3() {
        return ingredient3;
    }

    public String getInstruction() {
        return instruction;
    }
}

