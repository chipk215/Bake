package com.keyeswest.bake.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.keyeswest.bake.databinding.IngredientItemBinding;
import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.IngredientViewModel;

import java.util.List;

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

    static class IngredientHolder extends RecyclerView.ViewHolder{
        private final IngredientItemBinding mBinding;

        public IngredientHolder(IngredientItemBinding binding){
            super(binding.getRoot());
            mBinding = binding;

        }

        public void bind(final Ingredient ingredient){
            IngredientViewModel viewModel = new
                    IngredientViewModel(this.itemView.getContext(),ingredient);

            mBinding.setIngredient(viewModel);
            mBinding.executePendingBindings();
        }

    }
}
