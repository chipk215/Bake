package com.keyeswest.bake;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.keyeswest.bake.fragments.StepDetailFragment;
import com.keyeswest.bake.models.Step;
import com.keyeswest.bake.utilities.StepListUtilities;

import java.util.ArrayList;
import java.util.List;

public class StepDetailActivity extends AppCompatActivity implements
        // Invoked by StepDetailFragment when user wants to access previous or next step
        StepDetailFragment.OnStepNavigation,

        // The user changes the completion checkbox state
        StepDetailFragment.OnCompletionStateChange{

    public static final String TAG="StepDetailActivity";
    public static final String EXTRA_STEP_BUNDLE = "com.keyeswest.bake.step";
    public static final String STEPS_KEY = "stepsKey";
    public static final String SELECTED_INDEX_KEY="selectedIndexKey";

    public static Intent newIntent(Context packageContext, List<Step> steps, int selectedIndex){
        Intent intent = new Intent(packageContext, StepDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS_KEY, (ArrayList<Step>)steps);
        bundle.putInt(SELECTED_INDEX_KEY, selectedIndex);
        intent.putExtra(EXTRA_STEP_BUNDLE, bundle);

        return intent;
    }

    private List<Step> mSteps;
    private int mSelectedIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout file activity_step_detail.xml
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState != null){
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_KEY);
            mSelectedIndex = savedInstanceState.getInt(SELECTED_INDEX_KEY);
        }else {
            Bundle bundle = getIntent().getParcelableExtra(EXTRA_STEP_BUNDLE);
            mSteps = bundle.getParcelableArrayList(STEPS_KEY);
            mSelectedIndex = bundle.getInt(SELECTED_INDEX_KEY);
        }

        StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mSteps.get(mSelectedIndex));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_detail_container, stepDetailFragment)
                .commit();


    }

    /**
     * User wants the next Step in the recipe
     * @param currentStepId
     */
    @Override
    public void onNextSelected(String currentStepId) {
        int currentIndex = StepListUtilities.getIndexForCorrespondingId(currentStepId, mSteps);
        if ((currentIndex != -1) && ((currentIndex +1) < mSteps.size()) ){
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mSteps.get(currentIndex + 1));
            mSelectedIndex +=1;
            replaceFragment(stepDetailFragment);

        }
    }


    /**
     * User wants the previous step in the recipe
     * @param currentStepId
     */
    @Override
    public void onPreviousSelected(String currentStepId) {
        int currentIndex = StepListUtilities.getIndexForCorrespondingId(currentStepId,mSteps);
        if ((currentIndex != -1) && ((currentIndex -1) >= 0) ){
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mSteps.get(currentIndex-1));
            mSelectedIndex -=1;
            replaceFragment(stepDetailFragment);

        }

    }


    /**
     * User changed the step completion state by clicking on the checkbox
     * @param step = the step whose state was changed
     */
    @Override
    public void onCompletionStateChange(Step step) {
        int index = StepListUtilities.getIndexForCorrespondingId(step.getUniqueId(), mSteps);
        Log.d(TAG, "Checkbox changed for step index: " + Integer.toString(index));
        mSteps.get(index).setCheckedState(step.getCheckedState());

        // Update the Intent data (the steps) returned to the invoking activity
        updateActivityResults();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(STEPS_KEY, (ArrayList<Step>)mSteps);
        savedInstanceState.putInt(SELECTED_INDEX_KEY, mSelectedIndex);
    }


    /**
     * Helper function for unwrapping the intent data returned to the calling Activity.
     * @param data
     * @return
     */
    public static List<Step> getUpdatedSteps(Intent data){
        Bundle bundle =  data.getBundleExtra(EXTRA_STEP_BUNDLE);
        return bundle.getParcelableArrayList(STEPS_KEY);

    }


    private void replaceFragment(StepDetailFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, fragment)
                .commit();
    }


    private void updateActivityResults(){
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS_KEY, (ArrayList<Step>)mSteps);
        data.putExtra(EXTRA_STEP_BUNDLE, bundle);
        setResult(RESULT_OK, data);
    }

}
