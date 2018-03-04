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

import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    public interface OnItemClickListener{
        void onItemClick(Step step);

    }

    private final List<Step> mSteps;
    public Hashtable<String, Boolean> mCheckboxStates;
    private final OnItemClickListener mListener;

    public StepAdapter( List<Step> steps, Hashtable<String, Boolean> checkBoxStates,OnItemClickListener listener ){
        mSteps = steps;
        mCheckboxStates = checkBoxStates;
        mListener = listener;
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

    public  Hashtable<String, Boolean> getCheckBoxStates(){
        return mCheckboxStates;
    }

    class StepHolder extends RecyclerView.ViewHolder{

        private StepViewModel mStepViewModel;

        @BindView(R.id.step_checkBox) CheckBox mStepCheckbox;

        private final StepItemBinding mStepBinding;

        public StepHolder(StepItemBinding binding){
            super(binding.getRoot());
            mStepBinding = binding;

            ButterKnife.bind(this, itemView);

            mStepCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStepViewModel.getCheckedState()){
                        mStepViewModel.setCheckedState(false);
                        mStepCheckbox.setChecked(false);
                        mCheckboxStates.put(mStepViewModel.getUniqueId(), false);
                        mListener.onItemClick(mStepViewModel.getStep());


                    }else{
                        mStepViewModel.setCheckedState(true);
                        mStepCheckbox.setChecked(true);
                        mCheckboxStates.put(mStepViewModel.getUniqueId(), true);
                        mListener.onItemClick(mStepViewModel.getStep());

                    }
                }
            });
        }

        public void bind(final Step step){
            mStepViewModel = new StepViewModel(this.itemView.getContext(), step);

            mStepViewModel.setCheckedState(mCheckboxStates.get(step.getUniqueId()));
            mStepBinding.setStep(mStepViewModel);
            mStepBinding.executePendingBindings();
        }

    }
}
