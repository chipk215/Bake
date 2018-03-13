package com.keyeswest.bake.adapters;

import android.support.v7.widget.RecyclerView;

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


    public interface OnIngredientClickListener{
        void onIngredientClick();
    }

    private final List<Ingredient> mIngredients;

    private final OnIngredientClickListener mListener;

    public IngredientAdapter(List<Ingredient> ingredients, OnIngredientClickListener listener){
        mIngredients = ingredients;
        mListener = listener;

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

        IngredientHolder(IngredientItemBinding binding){
            super(binding.getRoot());
            mBinding = binding;

            // revisit can this be done with binding?
            ButterKnife.bind(this, itemView);
            mIngredientCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Keep tack of the checked state of the ingredients

                    if (mIngredientViewModel.getCheckedState()){

                        // update the UI
                        mIngredientViewModel.setCheckedState(false);

                        // changing the text color in the UI bridges the functionality with
                        // the widget which does not support checkboxes. The color of the
                        // ingredients is consistent between app and widget
                        mIngredientCheckbox.setTextColor(mIngredientCheckbox.getContext()
                                .getResources().getColor(R.color.colorOutOfItem));

                    }else{

                        // see notes above... handle same for checked state

                        mIngredientViewModel.setCheckedState(true);
                        mIngredientCheckbox.setTextColor(mIngredientCheckbox.getContext()
                                .getResources().getColor(R.color.colorItemInStock));
                    }

                    // The fragment is updated so that the state of the reset button can be updated
                    mListener.onIngredientClick();

                }
            });
        }


        public void bind(final Ingredient ingredient){
            mIngredientViewModel = new
                    IngredientViewModel(this.itemView.getContext(),ingredient);

            mBinding.setIngredient(mIngredientViewModel);
            mBinding.executePendingBindings();

            // This can probably be handled using data binding. I did a quick trial and it
            // had a bug and decided to let it go for now due to time constraints.
            if (ingredient.getCheckedState()){
                mIngredientCheckbox.setTextColor(mIngredientCheckbox.getContext()
                        .getResources().getColor(R.color.colorItemInStock));
            }else
            {
                mIngredientCheckbox.setTextColor(mIngredientCheckbox.getContext()
                        .getResources().getColor(R.color.colorOutOfItem));
            }
        }

    }
}
