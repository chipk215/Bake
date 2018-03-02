package com.keyeswest.bake;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.keyeswest.bake.fragments.IngredientListFragment;
import com.keyeswest.bake.fragments.MasterListFragment;
import com.keyeswest.bake.fragments.RecipeDetailFragment;
import com.keyeswest.bake.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MasterListFragment.OnRecipeSelected {

    private static final String TAG = "MainActivity";
    private static final String KEY_SAVE_RECIPE = "save_recipe";

    private boolean mTwoPane;

    private Recipe mRecipe;

    @Nullable
    @BindView(R.id.divider_view)  View mTwoPaneDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mTwoPaneDivider != null){
            // handle split screen
            mTwoPane = true;
            if (savedInstanceState != null){
                mRecipe = savedInstanceState.getParcelable(KEY_SAVE_RECIPE);

                // set up the detail fragments with the recipe information

            }else{

                // add empty fragment containers

                // create a new recipe detail fragment
                RecipeDetailFragment recipeFragment = RecipeDetailFragment.newInstance(mRecipe);

                IngredientListFragment ingredientsFragment =
                        IngredientListFragment.newInstance(mRecipe.getIngredients(),
                                mRecipe.getRecipeHash());

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.detail_container,recipeFragment)
                        .add(R.id.ingredients_container,ingredientsFragment)
                        .commit();
            }
        }else{
            mTwoPane = false;
        }

    }


    @Override
    protected void onStop(){

        super.onStop();
    }


    @Override
    public void onRecipeSelected(Bundle recipeBundle) {
        mRecipe = MasterListFragment.getRecipe(recipeBundle);
        if (mRecipe != null) {
            Log.d(TAG, "Recipe Name= " + mRecipe.getName());
        }else{
            Log.e(TAG, "Error, no recipe returned");
        }

        if (mTwoPane){
            // wide screen


        }else{
            Intent intent = RecipeDetailActivity.newIntent(this, mRecipe);
            startActivity(intent);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(KEY_SAVE_RECIPE, mRecipe);
    }
}
