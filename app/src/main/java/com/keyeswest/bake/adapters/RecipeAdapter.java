package com.keyeswest.bake.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.keyeswest.bake.R;

import com.keyeswest.bake.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    public interface OnItemClickListener{
        void onItemClick(Recipe recipe);
    }

    private final OnItemClickListener mListener;

    private final List<Recipe> mRecipes;

    public RecipeAdapter(List<Recipe> recipes, OnItemClickListener listener){
        mRecipes = recipes;
        mListener = listener;
    }


    Context mContext;

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_card, parent, false);


        return new RecipeHolder(view);
    }


    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.bind(recipe, mListener);

    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    static class RecipeHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_image_view)ImageView mImageView;
        @BindView(R.id.recipe_name_tv)TextView mRecipeNameTextView;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        public void bind(final Recipe recipe, final OnItemClickListener listener){
            mImageView.setImageDrawable(recipe.getDrawableRecipeImage(this.itemView.getContext()));
            mRecipeNameTextView.setText(recipe.getName());
            mRecipeNameTextView.bringToFront();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(recipe);
                }
            });

        }
    }
}
