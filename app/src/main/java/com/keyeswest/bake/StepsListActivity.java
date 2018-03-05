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


/**
 * Provides the view for the list of steps associated with a recipe.
 * The list contains the short step description and a checkbox indicating
 * whether the user has completed the step.
 *
 * Serves as the Master activity in the Master/Detail flow associated with recipe steps.
 */
public class StepsListActivity extends AppCompatActivity implements StepsListFragment.OnStepSelected {


    private static final String EXTRA_STEPS = "com.keyeswest.bake.steps";
    private static final int STEP_UPDATED_CODE = 0;
    private Recipe mRecipe;
    private Step mStep;

    public static Intent newIntent(Context context, Recipe recipe){
        Intent intent = new Intent(context, StepsListActivity.class);

        // We need the recipe to access the shared preferences filename that is needed
        // to update the checkbox state for the steps.
        // Revisit and optimize by passing only the filename and not the entire recipe.
        intent.putExtra(EXTRA_STEPS, recipe);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        Intent intent = getIntent();
        mRecipe = intent.getParcelableExtra(EXTRA_STEPS);

        // Add the UI fragment for displaying the recipe steps
        StepsListFragment fragment = StepsListFragment.newInstance(mRecipe);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.steps_container, fragment)
                .commit();
    }


    /**
     * Handle the user selection of a single recipe step.
     * @param step - user selected step
     */
    @Override
    public void onStepSelected(Step step) {

        // Either start step detail activity or add to two pane layout
        mStep = step;
        int selectedIndex = mRecipe.getStepIndex(mStep);
        if (selectedIndex != -1){
            // phone scenario
            // The StepDetailActivity takes the entire list of steps to enable navigation
            //between steps without popping back to this activity.
            Intent intent = StepDetailActivity.newIntent(this,mRecipe.getSteps(), selectedIndex);

            // The result is the list of steps with updated check box states corresponding to user
            // input when working with individual steps
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

           // Update shared preferences with changes to the checkbox states
           mRecipe.setSteps(StepDetailActivity.getUpdatedSteps(data));

           // This is a synchronous write on the UI thread... revisit
           SaveCheckStateToSharedPreferences.saveSteps(this, mRecipe);

       }

    }
}
