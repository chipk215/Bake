package com.keyeswest.bake.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.keyeswest.bake.R;

import com.keyeswest.bake.databinding.FragmentRecipeDetailBinding;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.RecipeViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment {
    private static final String TAG = "RecipeDetailFragment";
    private static final String KEY_SAVE_RECIPE = "save_recipe";

    private Recipe mRecipe;

    @BindView(R.id.make_it_btn)Button mMakeItButton;
    private Unbinder mUnbinder;

    // Required so fragment manager can instantiate
    public RecipeDetailFragment(){}

    public static RecipeDetailFragment newInstance(Recipe recipe){
        Bundle args = new Bundle();
        args.putParcelable(KEY_SAVE_RECIPE, recipe);

        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mRecipe = getArguments().getParcelable(KEY_SAVE_RECIPE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){


        FragmentRecipeDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_detail, container, false);

        View rootView = binding.getRoot();
        mUnbinder = ButterKnife.bind(this, rootView);
        binding.setRecipe(new RecipeViewModel(getContext(),mRecipe));


        mMakeItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the make it activity
                // Intent intent = StepsActivity.newIntent(getContext(), mR)
            }
        });

        return rootView;


    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }


}
