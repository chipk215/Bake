package com.keyeswest.bake;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.keyeswest.bake.fragments.RecipeDetailFragment;
import com.keyeswest.bake.models.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE_BUNDLE = "com.keyeswest.bake.recipe";

    public static Intent newIntent(Context packageContext, Recipe recipe){
        Intent intent = new Intent(packageContext, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_BUNDLE, recipe);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);


        Recipe recipe = getIntent().getExtras().getParcelable(EXTRA_RECIPE_BUNDLE);

        // create a new recipe detail fragment
        RecipeDetailFragment recipeFragment = new RecipeDetailFragment();
        recipeFragment.setRecipe(recipe);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.detail_container,recipeFragment)
                .commit();



    }
}
