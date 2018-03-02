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

        // handle the savedInstanceState
        if (savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelable(KEY_SAVE_RECIPE);
        }

        mTwoPane = (mTwoPaneDivider != null);


    }


    @Override
    protected void onStop(){

        super.onStop();
    }


    @Override
    public void onRecipeSelected(Bundle recipeBundle) {
        mRecipe = MasterListFragment.getRecipe(recipeBundle);


        if (mTwoPane){

            // determine if the detail fragments have been loaded
            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeDetailFragment recipeFragment = RecipeDetailFragment.newInstance(mRecipe);

            IngredientListFragment ingredientsFragment =
                    IngredientListFragment.newInstance(mRecipe.getIngredients(),
                            mRecipe.getRecipeHash());
            if (fragmentManager.findFragmentById(R.id.detail_container) == null){
                // fragments have not been loaded


                fragmentManager.beginTransaction()
                        .add(R.id.detail_container,recipeFragment)
                        .add(R.id.ingredients_container,ingredientsFragment)
                        .commit();

            } else{
                // replace the fragments
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_container,recipeFragment)
                        .replace(R.id.ingredients_container,ingredientsFragment)
                        .commit();
            }


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
