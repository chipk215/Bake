package com.keyeswest.bake.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.keyeswest.bake.R;
import com.keyeswest.bake.adapters.StepAdapter;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;
import com.keyeswest.bake.tasks.ReadCheckboxStates;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class StepsFragment extends Fragment {

    private static final String TAG="StepsFragment";

    private static final String STEPS_ARG = "stepsArg";
    private static final String RECIPE_PREFS_STEPS_FILENAME_KEY = "recipePrefsKey";

    private Recipe mRecipe;
    private List<Step> mSteps;
    private String mRecipePrefsStepsFilename;

    private Hashtable<String, Boolean> mStepsCheckboxState;
    private StepAdapter mStepAdapter;

    @BindView(R.id.steps_recyclerView)RecyclerView mStepsRecyclerView;


    // ButterKnife helper
    private Unbinder mUnbinder;



    public static StepsFragment newInstance(Recipe recipe){
        Bundle args = new Bundle();
        args.putParcelable(STEPS_ARG, recipe);

        StepsFragment fragment = new StepsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            mRecipe = bundle.getParcelable(STEPS_ARG);
            mSteps = mRecipe.getSteps();
            mRecipePrefsStepsFilename = mRecipe.getSharedPreferencesStepsFileName();


        }else{
            Log.e(TAG,"Expected step data not provided to initialize StepsFragment") ;
            return;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.custom_list_divider));
        mStepsRecyclerView.addItemDecoration(itemDecorator);

        ReadCheckboxStates<Step> task = new ReadCheckboxStates<>(getContext(),
                mRecipePrefsStepsFilename, new ReadCheckboxStates.ResultsCallback(){

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
        mStepAdapter = new StepAdapter(mSteps, mStepsCheckboxState, new StepAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Step step) {
                // Transition to step detail view
            }
        });
        if (isAdded()){
            mStepsRecyclerView.setAdapter(mStepAdapter);

        }
    }



    @Override
    public void onPause(){

        // revisit should this be off the UI thread?
        // We would wait to call super.onPause until complete, right?
        mStepsCheckboxState = mStepAdapter.getCheckBoxStates();

        SharedPreferences.Editor editor = getContext().getSharedPreferences(mRecipePrefsStepsFilename, MODE_PRIVATE).edit();

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
