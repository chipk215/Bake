package com.keyeswest.bake.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.keyeswest.bake.R;
import com.keyeswest.bake.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepDetailFragment  extends Fragment {

    private static final String SAVE_STEP_KEY = "saveStepKey";

    public static StepDetailFragment newInstance(Step step){
        Bundle args = new Bundle();
        args.putParcelable(SAVE_STEP_KEY, step);

        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private Step mStep;
    private Unbinder mUnbinder;

    @BindView(R.id.step_description_tv)TextView mDescriptionTextView;
    @BindView(R.id.prev_button)Button mPreviousButton;
    @BindView(R.id.next_button)Button mNextButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mStep = getArguments().getParcelable(SAVE_STEP_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_step_detail,
                container, false);

        mUnbinder = ButterKnife.bind(this, view);
        mDescriptionTextView.setText(mStep.getDescription());

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }


}
