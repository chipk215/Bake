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

    //attribution: https://android.jlelse.eu/android-handling-checkbox-state-in-recycler-views-71b03f237022
    public SparseBooleanArray mCheckStateArray;

    public IngredientAdapter(List<Ingredient> ingredients){
        mIngredients = ingredients;
        mCheckStateArray = new SparseBooleanArray();
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
                    int adapterPosition = getAdapterPosition();
                    if (! mCheckStateArray.get(adapterPosition, false)){
                        mIngredientCheckbox.setChecked(true);
                        mCheckStateArray.put(adapterPosition, true);
                    }else{
                        mIngredientCheckbox.setChecked(false);
                        mCheckStateArray.put(adapterPosition, false);

                    }


                }
            });


        }

        public void bind(final Ingredient ingredient){
            IngredientViewModel viewModel = new
                    IngredientViewModel(this.itemView.getContext(),ingredient);

            mBinding.setIngredient(viewModel);
            mBinding.executePendingBindings();
        }

    }
}
