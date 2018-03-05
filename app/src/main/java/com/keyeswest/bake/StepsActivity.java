package com.keyeswest.bake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyeswest.bake.fragments.StepsListFragment;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;
import com.keyeswest.bake.utilities.SaveCheckStateToSharedPreferences;

public class StepsActivity extends AppCompatActivity implements StepsListFragment.OnStepSelected {


    private static final String EXTRA_STEPS = "com.keyeswest.bake.steps";
    private static final int STEP_UPDATED_CODE = 0;
    private Recipe mRecipe;
    private Step mStep;

    private boolean mReturningWithResult = false;

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

        StepsListFragment fragment = StepsListFragment.newInstance(mRecipe);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.steps_container, fragment)
                .commit();


    }


    @Override
    public void onStepSelected(Step step) {
        // Either start step detail activity or add to two pane layout
        mStep = step;
        int selectedIndex = mRecipe.getStepIndex(mStep);
        if (selectedIndex != -1){
            // phone scenario
            Intent intent = StepDetailActivity.newIntent(this,mRecipe.getSteps(), selectedIndex);
            startActivityForResult(intent, STEP_UPDATED_CODE);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (resultCode != Activity.RESULT_OK){
           return;
       }

       if (requestCode == STEP_UPDATED_CODE){
           if (data == null){
               return;
           }

           mReturningWithResult = true;
           mRecipe.setSteps(StepDetailActivity.getUpdatedSteps(data));

           SaveCheckStateToSharedPreferences.saveSteps(this, mRecipe);

       }

    }
}
