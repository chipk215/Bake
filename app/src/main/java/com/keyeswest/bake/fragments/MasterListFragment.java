package com.keyeswest.bake.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.keyeswest.bake.R;
import com.keyeswest.bake.adapters.RecipeAdapter;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.utilities.RecipeFetcher;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MasterListFragment extends Fragment {

    private static final String TAG = "MasterListFragment";

    private RecipeAdapter mRecipeAdapter;

    private Unbinder mUnbinder;

    private RecipeFetcher mRecipeFetcher;

    private List<Recipe> mRecipeList;

    @BindView(R.id.recipe_recycler_view)  RecyclerView mRecipeRecyclerView;

    @BindView(R.id.error_layout) LinearLayout mErrorLayout;

    @BindView(R.id.error_btn_retry)Button mRetryButton;

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

                mErrorLayout.setVisibility(View.GONE);
                mRecipeRecyclerView.setVisibility(View.VISIBLE);


                mRecipeList = recipeList;
                mRecipeAdapter = new RecipeAdapter(recipeList);
                if (isAdded()){
                    mRecipeRecyclerView.setAdapter(mRecipeAdapter);
                }

            }

            @Override
            public void networkUnavailable() {
                Log.e(TAG, "Download error occurred");

                if (mErrorLayout.getVisibility() == View.GONE){
                    mErrorLayout.setVisibility(View.VISIBLE);
                    mRecipeRecyclerView.setVisibility(View.GONE);
                }
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

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecipeFetcher.fetchRecipes(getActivity().getSupportLoaderManager());
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
