package com.keyeswest.bake.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyeswest.bake.R;
import com.keyeswest.bake.adapters.RecipeAdapter;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.utilities.RecipeFetcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MasterListFragment extends Fragment {

    @BindView(R.id.recipe_recycler_view)  RecyclerView mRecipeRecyclerView;

    private RecipeAdapter mRecipeAdapter;

    private Unbinder mUnbinder;

    private RecipeFetcher mRecipeFetcher;

    private List<Recipe> mRecipeList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mRecipeFetcher = new RecipeFetcher(getContext(), new RecipeFetcher.RecipeResults() {
            @Override
            public void handleRecipes(List<Recipe> recipeList) {
                mRecipeList = recipeList;
                mRecipeAdapter = new RecipeAdapter(recipeList);
                if (isAdded()){
                    mRecipeRecyclerView.setAdapter(mRecipeAdapter);
                }

            }

            @Override
            public void networkUnavailable() {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list,
                container, false);
        mUnbinder = ButterKnife.bind(this, rootView);


        int columns = getResources().getInteger(R.integer.recipe_grid_columns);
        mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),columns));

        mRecipeFetcher.fetchRecipes(getActivity().getSupportLoaderManager());

        return rootView;
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
