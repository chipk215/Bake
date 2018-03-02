package com.keyeswest.bake;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.keyeswest.bake.fragments.MasterListFragment;
import com.keyeswest.bake.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MasterListFragment.OnRecipeSelected {
    private static final String TAG = "MainActivity";

    private boolean mTwoPane;

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
        Recipe recipe = MasterListFragment.getRecipe(recipeBundle);
        if (recipe != null) {
            Log.d(TAG, "Recipe Name= " + recipe.getName());
        }else{
            Log.e(TAG, "Error, no recipe returned");
        }

        if (mTwoPane){
            // wide screen

        }else{
            Intent intent = RecipeDetailActivity.newIntent(this, recipe);
            startActivity(intent);
        }


    }
}
