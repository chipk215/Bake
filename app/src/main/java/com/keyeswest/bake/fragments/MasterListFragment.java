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

import java.util.ArrayList;
import java.util.List;

public class MasterListFragment extends Fragment {

    private RecyclerView mRecipeRecyclerView;

    private RecipeAdapter mRecipeAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list,
                container, false);


        mRecipeRecyclerView = rootView.findViewById(R.id.recipe_recycler_view);
        int columns = getResources().getInteger(R.integer.recipe_grid_columns);
        mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),columns));


        List<Recipe> recipes = new ArrayList<>();
        Recipe r1 = new Recipe();
        r1.setName("Nutella Pie");
        recipes.add(r1);

        Recipe r2 = new Recipe();
        r2.setName("Brownies");
        recipes.add(r2);

        Recipe r3 = new Recipe();
        r3.setName("Cheese Cake");
        recipes.add(r3);

        Recipe r4 = new Recipe();
        r4.setName("ABCDEFGHIJKLM");
        recipes.add(r4);



        if (isAdded()){
            mRecipeRecyclerView.setAdapter(new RecipeAdapter(recipes));
        }

        return rootView;
    }
}
