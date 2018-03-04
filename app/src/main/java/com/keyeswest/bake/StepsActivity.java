package com.keyeswest.bake;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyeswest.bake.fragments.StepsFragment;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsActivity extends AppCompatActivity {


    private static final String EXTRA_STEPS = "com.keyeswest.bake.steps";
    private Recipe mRecipe;

    public static Intent newIntent(Context context, Recipe recipe){
        Intent intent = new Intent(context, StepsActivity.class);
        intent.putExtra(EXTRA_STEPS, recipe);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        Intent intent = getIntent();
        mRecipe = intent.getParcelableExtra(EXTRA_STEPS);

        StepsFragment fragment = StepsFragment.newInstance(mRecipe);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.steps_container, fragment)
                .commit();


    }




}
