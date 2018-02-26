package com.keyeswest.bake.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keyeswest.bake.R;
import com.keyeswest.bake.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private final List<Recipe> mRecipes;

    public RecipeAdapter(List<Recipe> recipes){
        mRecipes = recipes;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);

        return new RecipeHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.name.setText(recipe.getName());
        holder.ingredientCount.setText(Integer.toString(recipe.getIngredients().size()));
        holder.stepCount.setText(Integer.toString(recipe.getSteps().size()));
        holder.difficulty.setText(recipe.getComplexity(holder.itemView.getContext()));


    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    static class RecipeHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_name_tv) TextView name;
        @BindView(R.id.ingredient_count_tv)TextView ingredientCount;
        @BindView(R.id.step_count_tv)TextView stepCount;
        @BindView(R.id.difficulty_tv)TextView difficulty;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
         //   name = itemView.findViewById(R.id.recipe_name_tv);

        }
    }
}
