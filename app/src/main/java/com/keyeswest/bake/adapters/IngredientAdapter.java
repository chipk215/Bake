package com.keyeswest.bake.adapters;


import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.keyeswest.bake.R;
import com.keyeswest.bake.databinding.IngredientItemBinding;
import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.IngredientViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    private final List<Ingredient> mIngredients;

    public IngredientAdapter(List<Ingredient> ingredients){
        mIngredients = ingredients;
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        IngredientItemBinding ingredientBinding =
                IngredientItemBinding.inflate(inflater, parent, false);

        return new IngredientHolder(ingredientBinding);
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);

        holder.bind(ingredient);

    }


    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    class IngredientHolder extends RecyclerView.ViewHolder{

        private IngredientViewModel mIngredientViewModel;

        @BindView(R.id.checkBox) CheckBox mIngredientCheckbox;
        private final IngredientItemBinding mBinding;

        public IngredientHolder(IngredientItemBinding binding){
            super(binding.getRoot());
            mBinding = binding;

            // revisit can this be done with binding?
            ButterKnife.bind(this, itemView);
            mIngredientCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mIngredientViewModel.getCheckedState()){
                        mIngredientCheckbox.setChecked(false);
                        mIngredientViewModel.setCheckedState(false);
                    }else{
                        mIngredientCheckbox.setChecked(true);
                        mIngredientViewModel.setCheckedState(true);
                    }

                }
            });


        }

        public void bind(final Ingredient ingredient){
            mIngredientViewModel = new
                    IngredientViewModel(this.itemView.getContext(),ingredient);

            mBinding.setIngredient(mIngredientViewModel);
            mBinding.executePendingBindings();
        }

    }
}
