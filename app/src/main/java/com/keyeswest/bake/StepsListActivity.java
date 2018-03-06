package com.keyeswest.bake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.keyeswest.bake.fragments.StepDetailFragment;
import com.keyeswest.bake.fragments.StepsListFragment;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;
import com.keyeswest.bake.utilities.SaveCheckStateToSharedPreferences;
import com.keyeswest.bake.utilities.StepListUtilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Provides the view for the list of steps associated with a recipe.
 * The list contains the short step description and a checkbox indicating
 * whether the user has completed the step.
 *
 * Serves as the Master activity in the Master/Detail flow associated with recipe steps.
 */
public class StepsListActivity extends AppCompatActivity implements

        StepsListFragment.OnStepSelected,

        // Invoked by StepDetailFragment when user wants to access previous or next step
        StepDetailFragment.OnStepNavigation,

        // Required to implement but not used
        StepDetailFragment.OnCompletionStateChange{

    private static final String TAG="StepsListActivity";

    private static final String EXTRA_STEPS = "com.keyeswest.bake.steps";
    private static final String RECIPE_KEY = "save_recipe";
    private static final String SELECTED_KEY = "selectedIndex";
    private static final String STEP_KEY = "save_step";
    private static final int STEP_UPDATED_CODE = 0;

    private boolean mTwoPane;
    private Recipe mRecipe;
    private Step mStep;
    private int mSelectedIndex;

    @Nullable
    @BindView(R.id.step_divider_view) View mTwoPaneDivider;


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
        ButterKnife.bind(this);
        mSelectedIndex = 0;

        if (savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            mStep = savedInstanceState.getParcelable(STEP_KEY);
            mSelectedIndex = savedInstanceState.getInt(SELECTED_KEY);
        }else{
            Intent intent = getIntent();
            mRecipe = intent.getParcelableExtra(EXTRA_STEPS);
        }

        mTwoPane = (mTwoPaneDivider != null);

        // Add the UI fragment for displaying the recipe steps
        StepsListFragment fragment = StepsListFragment.newInstance(mRecipe);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.steps_container, fragment)
                .commit();

        if (mTwoPane){

            StepDetailFragment stepDetailFragment =
                    StepDetailFragment.newInstance(mRecipe.getSteps().get(mSelectedIndex));

            if (fragmentManager.findFragmentById(R.id.step_detail_container) == null) {
                // first load of fragment
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }

            onStepSelected(mRecipe.getSteps().get(mSelectedIndex));

        }
    }


    /**
     * Handle the user selection of a single recipe step.
     * @param step - user selected step
     */
    @Override
    public void onStepSelected(Step step) {

        mStep = step;
        mSelectedIndex = mRecipe.getStepIndex(mStep);
        if (mSelectedIndex != -1) {

            if (mTwoPane) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                StepDetailFragment stepDetailFragment =
                        StepDetailFragment.newInstance(mRecipe.getSteps().get(mSelectedIndex));

                // replace detail fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            } else {

                // phone scenario
                // The StepDetailActivity takes the entire list of steps to enable navigation
                //between steps without popping back to this activity.
                Intent intent = StepDetailActivity.newIntent(this,
                        mRecipe.getSteps(), mSelectedIndex);

                // The result is the list of steps with updated check box states corresponding to user
                // input when working with individual steps
                startActivityForResult(intent, STEP_UPDATED_CODE);
            }
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(RECIPE_KEY,mRecipe);
        savedInstanceState.putParcelable(STEP_KEY,mStep);
        savedInstanceState.putInt(SELECTED_KEY, mSelectedIndex);


    }

    @Override
    public void onNextSelected(String currentStepId) {
        List<Step> steps = mRecipe.getSteps();
        int currentIndex = StepListUtilities.getIndexForCorrespondingId(currentStepId, steps);
        if ((currentIndex != -1) && ((currentIndex +1) < steps.size()) ){
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(steps.get(currentIndex + 1));
            mSelectedIndex +=1;
            replaceFragment(stepDetailFragment);

        }
    }

    @Override
    public void onPreviousSelected(String currentStepId) {
        List<Step> steps = mRecipe.getSteps();
        int currentIndex = StepListUtilities.getIndexForCorrespondingId(currentStepId, steps);
        if ((currentIndex != -1) && ((currentIndex -1) >= 0) ){
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(steps.get(currentIndex-1));
            mSelectedIndex -=1;
            replaceFragment(stepDetailFragment);


        }
    }



    private void replaceFragment(StepDetailFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, fragment)
                .commit();
    }


    @Override
    public void onCompletionStateChange(Step step) {

    }
}
