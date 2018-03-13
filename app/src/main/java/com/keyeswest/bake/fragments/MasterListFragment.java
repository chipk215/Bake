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

import com.keyeswest.bake.R;
import com.keyeswest.bake.adapters.RecipeAdapter;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.RecipeFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.keyeswest.bake.tasks.RecipeJsonDeserializer.*;

/**
 * Handles the displaying of recipes.
 */
public class MasterListFragment extends Fragment {

    private static final String TAG = "MasterListFragment";

    private OnRecipeSelected mHostCallback;
    public interface OnRecipeSelected{
        void onRecipeSelected(Recipe recipe);
    }

    private RecipeAdapter mRecipeAdapter;
    private Unbinder mUnbinder;
    private List<Recipe> mRecipes = new ArrayList<>();


    @BindView(R.id.recipe_recycler_view)  RecyclerView mRecipeRecyclerView;

    // Mandatory empty constructor
    public MasterListFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //Attribution: from Udacity course work, clever!

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mHostCallback = (OnRecipeSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeSelected");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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

        setupRecipeAdapter();

        RecipeFactory.readRecipes(getContext(),new RecipeResultsCallback() {
            @Override
            public void recipeResult(List<Recipe> recipeList) {
                mRecipes = recipeList;

                // Updating the recipe adaptor is not working
                //mRecipeAdapter.notifyDataSetChanged();

                //creating a new adaptor does work, hmmm, why?
                setupRecipeAdapter();
            }
        });

        return rootView;
    }


    private void setupRecipeAdapter(){

        if (isAdded()){

            Log.d(TAG, "Fragment added, setting up adapter");

            mRecipeAdapter = new RecipeAdapter(mRecipes, new RecipeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Recipe recipe) {

                    mHostCallback.onRecipeSelected(recipe);
                }
            });

            mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        }else{
            Log.d(TAG, "Fragment NOT added, not setting up adapter");

        }

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
