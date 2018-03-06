package com.keyeswest.bake.fragments;


import android.content.Context;
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
import android.widget.Toast;


import com.keyeswest.bake.R;
import com.keyeswest.bake.adapters.StepAdapter;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;
import com.keyeswest.bake.tasks.ReadCheckboxStates;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class StepsListFragment extends Fragment {

    private static final String TAG="StepsListFragment";

    private static final String STEPS_ARG = "stepsArg";

    private OnStepSelected mHostActivityCallback;

    public interface OnStepSelected{
        void onStepSelected(Step step);
    }


    private Recipe mRecipe;
    private List<Step> mSteps;


    private StepAdapter mStepAdapter;

    @BindView(R.id.steps_recyclerView)RecyclerView mStepsRecyclerView;


    // ButterKnife helper
    private Unbinder mUnbinder;


    public static StepsListFragment newInstance(Recipe recipe){
        Bundle args = new Bundle();
        args.putParcelable(STEPS_ARG, recipe);

        StepsListFragment fragment = new StepsListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mHostActivityCallback = (OnStepSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepSelected");
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            mRecipe = bundle.getParcelable(STEPS_ARG);
            mSteps = mRecipe.getSteps();



        }else{
            Log.e(TAG,"Expected step data not provided to initialize StepsListFragment") ;
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ReadCheckboxStates<Step> task = new ReadCheckboxStates<>(getContext(),
                mRecipe.getSharedPreferencesStepsFileName(), new ReadCheckboxStates.ResultsCallback<Step>(){

            @Override
            public void CheckboxStates(List<Step> updatedList) {
                mSteps = updatedList;
                setupStepsAdapter();
            }
        });

        task.execute(mSteps);


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
                mRecipe.getSharedPreferencesStepsFileName(), new ReadCheckboxStates.ResultsCallback<Step>(){

            @Override
            public void CheckboxStates(List<Step> updatedList) {
                mSteps = updatedList;
                setupStepsAdapter();
            }
        });

        task.execute(mSteps);



        return view;
    }

    private void setupStepsAdapter(){
        mStepAdapter = new StepAdapter(mSteps, new StepAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Step step) {

                Log.d(TAG, "Step Selected" + Integer.toString(step.getId()));

                mHostActivityCallback.onStepSelected(step);

            }
        });
        if (isAdded()){
            mStepsRecyclerView.setAdapter(mStepAdapter);

        }
    }



    @Override
    public void onPause(){

        // revisit should this be off the UI thread?
        // Can we exit onPause before the operation completes?


        SharedPreferences.Editor editor =
                getContext().getSharedPreferences(mRecipe.getSharedPreferencesStepsFileName(),
                        MODE_PRIVATE).edit();

        for (Step i : mSteps){
            Boolean isChecked = i.getCheckedState();
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
