package com.keyeswest.bake;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyeswest.bake.fragments.StepDetailFragment;
import com.keyeswest.bake.models.Step;

import java.util.ArrayList;
import java.util.List;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnGetStepSelected {

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

        }else{
            Bundle bundle = getIntent().getParcelableExtra(EXTRA_STEP_BUNDLE);
            mSteps = bundle.getParcelableArrayList(STEPS_KEY);
            mSelectedIndex = bundle.getInt(SELECTED_INDEX_KEY);

            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mSteps.get(mSelectedIndex));

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment)
                    .commit();

        }


    }

    @Override
    public void onNextSelected(String currentStepId) {
        int currentIndex = getIndexForCorrespondingId(currentStepId);
        if ((currentIndex != -1) && ((currentIndex +1) < mSteps.size()) ){
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mSteps.get(currentIndex + 1));
            replaceFragment(stepDetailFragment);

        }
    }

    @Override
    public void onPreviousSelected(String currentStepId) {
        int currentIndex = getIndexForCorrespondingId(currentStepId);
        if ((currentIndex != -1) && ((currentIndex -1) >= 0) ){
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mSteps.get(currentIndex-1));
            replaceFragment(stepDetailFragment);

        }

    }

    private void replaceFragment(StepDetailFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, fragment)
                .commit();
    }

    private int getIndexForCorrespondingId(String id){
        for (int i=0; i< mSteps.size(); i++){
            if (mSteps.get(i).getUniqueId().equals(id)){
                return i;
            }
        }

        return -1;
    }
}
