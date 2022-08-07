package com.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ExploreFragment extends Fragment {
    Fragment searchFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_explore, container, false);
        searchFragment = new SearchFragment();
        Button exploreCocktails = view.findViewById(R.id.explore_cocktails);
        exploreCocktails.setOnClickListener(view1 -> {
            Log.i("Clicked","Clicked");
            loadFragment(searchFragment);
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
