package com.keyeswest.bake;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.keyeswest.bake.fragments.IngredientListFragment;
import com.keyeswest.bake.fragments.RecipeDetailFragment;
import com.keyeswest.bake.models.Recipe;



/**
 * Handles the view displaying recipe details except for the list of steps.
 *
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE_BUNDLE = "com.keyeswest.bake.recipe";
    private static final String KEY_SAVE_RECIPE = "save_recipe";

    public static Intent newIntent(Context packageContext, Recipe recipe){
        Intent intent = new Intent(packageContext, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_BUNDLE, recipe);
        return intent;
    }

    private Recipe mRecipe;

    private ScrollView mScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelable(KEY_SAVE_RECIPE);
        }else{
            mRecipe = getIntent().getExtras().getParcelable(EXTRA_RECIPE_BUNDLE);
            // create a new recipe detail fragment
            RecipeDetailFragment recipeFragment = RecipeDetailFragment.newInstance(mRecipe);

            IngredientListFragment ingredientsFragment =
                    IngredientListFragment.newInstance(mRecipe);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.detail_container,recipeFragment)
                    .add(R.id.ingredients_container,ingredientsFragment)
                    .commit();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        scrollToTopOfView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(KEY_SAVE_RECIPE, mRecipe);
    }


    // Attribution:  https://stackoverflow.com/a/19703408/9128441

    // Scroll to to top of the window since the ingredient list
    // moves the focus to the bottom of view
    private void scrollToTopOfView(){
        mScrollView = findViewById(R.id.recipe_detail_scrollview);
        mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.fullScroll(View.FOCUS_UP);
                    }
                });
            }
        });
    }

}
