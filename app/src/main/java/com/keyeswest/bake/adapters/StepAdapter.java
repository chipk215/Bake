package com.keyeswest.bake.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.keyeswest.bake.R;
import com.keyeswest.bake.databinding.StepItemBinding;
import com.keyeswest.bake.models.Step;
import com.keyeswest.bake.models.StepViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    public interface OnItemClickListener{
        void onItemClick(Step step);

    }

    public interface OnCheckboxClicked{
        void checkboxClicked();
    }

    private final List<Step> mSteps;
    private final OnItemClickListener mListener;
    private final OnCheckboxClicked mCheckBoxListener;

    public StepAdapter( List<Step> steps, OnItemClickListener listener,
                        OnCheckboxClicked checkboxListener ){
        mSteps = steps;
        mListener = listener;
        mCheckBoxListener = checkboxListener;
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        StepItemBinding stepBinding =
                StepItemBinding.inflate(inflater, parent, false);

        return new StepHolder(stepBinding);
    }

    @Override
    public void onBindViewHolder(StepHolder holder, int position) {
        Step step =mSteps.get(position);
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }


    class StepHolder extends RecyclerView.ViewHolder{

        private StepViewModel mStepViewModel;

        @BindView(R.id.step_checkBox) CheckBox mStepCheckbox;

        @BindView(R.id.step_label) TextView mStepLabel;

        private final StepItemBinding mStepBinding;

        StepHolder(StepItemBinding binding){
            super(binding.getRoot());
            mStepBinding = binding;

            ButterKnife.bind(this, itemView);

            mStepCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mStepViewModel.getCheckedState()){
                        mStepViewModel.setCheckedState(false);
                    }else{
                        mStepViewModel.setCheckedState(true);
                    }

                    mCheckBoxListener.checkboxClicked();
                }
            });

            // fragment updates reset button
            mStepLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(mStepViewModel.getStep());
                }
            });
        }

        public void bind(final Step step){
            mStepViewModel = new StepViewModel(this.itemView.getContext(), step);
            mStepBinding.setStep(mStepViewModel);
            mStepBinding.executePendingBindings();
        }

    }
}
