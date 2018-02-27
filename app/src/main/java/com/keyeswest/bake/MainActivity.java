package com.keyeswest.bake;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.keyeswest.bake.fragments.MasterListFragment;
import com.keyeswest.bake.models.Recipe;


public class MainActivity extends AppCompatActivity implements MasterListFragment.OnRecipeSelected {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    }
}
