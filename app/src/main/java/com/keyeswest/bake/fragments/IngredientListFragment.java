package com.keyeswest.bake.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyeswest.bake.R;
import com.keyeswest.bake.adapters.IngredientAdapter;
import com.keyeswest.bake.models.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IngredientListFragment extends Fragment {

    private List<Ingredient> mIngredients;
    private IngredientAdapter mIngredientAdapter;

    @BindView(R.id.ingredient_recycler_view)
    RecyclerView mIngredientRecyclerView;

    private Unbinder mUnbinder;

    public IngredientListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        final View rootView = inflater.inflate(R.layout.fragment_ingredient_list,
                container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupIngredientAdapter();

        return rootView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void setupIngredientAdapter(){

        mIngredientAdapter = new IngredientAdapter(mIngredients);

        if (isAdded()){
            mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        }
    }


    public void setIngredients(List<Ingredient> ingredients){
        mIngredients = ingredients;
    }
}
