package com.keyeswest.bake.adapters;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import com.keyeswest.bake.R;
import com.keyeswest.bake.databinding.RecipeCardBinding;
import com.keyeswest.bake.models.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private final List<Recipe> mRecipes;

    public RecipeAdapter(List<Recipe> recipes){
        mRecipes = recipes;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecipeCardBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.recipe_card, parent, false);

        return new RecipeHolder(binding);
    }


    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.bind(recipe);

    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    static class RecipeHolder extends RecyclerView.ViewHolder{
        private final RecipeCardBinding binding;

        public RecipeHolder(RecipeCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(Recipe recipe){
            binding.setRecipe(recipe);
            binding.executePendingBindings();
        }
    }
}
