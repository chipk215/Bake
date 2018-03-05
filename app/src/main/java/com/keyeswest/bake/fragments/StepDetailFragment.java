package com.keyeswest.bake.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

    private OnGetStepSelected mHostActivityCallback;

    public interface OnGetStepSelected{
        void onNextSelected(String currentStepId);
        void onPreviousSelected(String currentStepId);
    }

    private OnCompletionStateChange mOnHostActivityCompletionCallback;
    public interface OnCompletionStateChange
    {
        void onCompletionStateChange(Step step);
    }

    private Step mStep;
    private Unbinder mUnbinder;

    @BindView(R.id.step_description_tv)TextView mDescriptionTextView;
    @BindView(R.id.prev_button)Button mPreviousButton;
    @BindView(R.id.next_button)Button mNextButton;
    @BindView(R.id.step_complete_cb)CheckBox mStepCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mStep = getArguments().getParcelable(SAVE_STEP_KEY);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mHostActivityCallback = (OnGetStepSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnGetStepSelected");
        }

        try {
            mOnHostActivityCompletionCallback = (OnCompletionStateChange) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCompletionStateChange");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_step_detail,
                container, false);

        mUnbinder = ButterKnife.bind(this, view);
        mDescriptionTextView.setText(mStep.getDescription());

        // requires the step ids to start at 0 and increase by 1
        mPreviousButton.setEnabled(mStep.getId() != 0);

        //this is hacky ...revisit
        mNextButton.setEnabled((mStep.getId()+1) < mStep.getNumberOfStepsInRecipe());

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivityCallback.onPreviousSelected(mStep.getUniqueId());
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivityCallback.onNextSelected(mStep.getUniqueId());
            }
        });

        mStepCheckBox.setChecked(mStep.getCheckedState());

        mStepCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStep.setCheckedState(((CheckBox) v).isChecked());
                mOnHostActivityCompletionCallback.onCompletionStateChange(mStep);

            }
        });

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }


}
