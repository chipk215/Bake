package com.keyeswest.bake.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.keyeswest.bake.R;
import com.keyeswest.bake.adapters.StepAdapter;
import com.keyeswest.bake.models.Step;
import com.keyeswest.bake.tasks.ReadCheckboxStates;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class StepsFragment extends Fragment {

    private static final String TAG="StepsFragment";

    private static final String STEPS_ARG = "stepsArg";
    private static final String STEPS_HASH = "stepsHash";

    private List<Step> mSteps;
    private String mStepHash;

    private Hashtable<String, Boolean> mStepsCheckboxState;
    private StepAdapter mStepAdapter;

    @BindView(R.id.steps_recyclerView)RecyclerView mStepsRecyclerView;

    // ButterKnife helper
    private Unbinder mUnbinder;


    public static StepsFragment newInstance(List<Step> steps, String stepHash){
        Bundle args = new Bundle();
        args.putParcelableArrayList(STEPS_ARG, (ArrayList<Step>) steps);
        args.putString(STEPS_HASH, stepHash);
        StepsFragment fragment = new StepsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            mSteps = bundle.getParcelableArrayList(STEPS_ARG);
            mStepHash= bundle.getString(STEPS_HASH);


        }else{
            Log.e(TAG,"Expected step data not provided to initialize StepsFragment") ;
            return;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        ReadCheckboxStates<Step> task = new ReadCheckboxStates<>(getContext(), mStepHash, new ReadCheckboxStates.ResultsCallback(){

            @Override
            public void CheckboxStates(Hashtable<String, Boolean> checkboxStates) {
                mStepsCheckboxState = checkboxStates;
                setupStepsAdapter();
            }
        });

        task.execute(mSteps);

        return view;
    }

    private void setupStepsAdapter(){
        mStepAdapter = new StepAdapter(mSteps, mStepsCheckboxState);
        if (isAdded()){
            mStepsRecyclerView.setAdapter(mStepAdapter);

        }
    }




    @Override
    public void onPause(){

        // revisit should this be off the UI thread?
        // We would wait to call super.onPause until complete, right?
        mStepsCheckboxState = mStepAdapter.getCheckBoxStates();

        SharedPreferences.Editor editor = getContext().getSharedPreferences(mStepHash, MODE_PRIVATE).edit();

        for (Step i : mSteps){
            Boolean isChecked = mStepsCheckboxState.get(i.getUniqueId());
            editor.putBoolean(i.getUniqueId(), isChecked);
        }

        editor.apply();


        super.onPause();
    }




    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }




}
