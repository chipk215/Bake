package com.keyeswest.bake;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyeswest.bake.fragments.StepsFragment;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsActivity extends SingleFragmentActivity {


    private static final String EXTRA_STEPS = "com.keyeswest.bake.steps";


    public static Intent newIntent(Context context, Recipe recipe){
        Intent intent = new Intent(context, StepsActivity.class);
        intent.putExtra(EXTRA_STEPS, recipe);
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        Recipe recipe= getIntent().getParcelableExtra(EXTRA_STEPS);

        return StepsFragment.newInstance(recipe);
    }


}
