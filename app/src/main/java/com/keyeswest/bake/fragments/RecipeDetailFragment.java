package com.keyeswest.bake.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyeswest.bake.R;

import com.keyeswest.bake.databinding.FragmentRecipeDetailBinding;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.RecipeViewModel;

public class RecipeDetailFragment extends Fragment {
    private static final String TAG = "RecipeDetailFragment";

    private Recipe mRecipe;

    // Required so fragment manager can instantiate
    public RecipeDetailFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){


        FragmentRecipeDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_detail, container, false);

        View rootView = binding.getRoot();
        binding.setRecipe(new RecipeViewModel(getContext(),mRecipe));

        return rootView;


    }


    public void setRecipe(Recipe recipe){
        mRecipe = recipe;
    }
}
