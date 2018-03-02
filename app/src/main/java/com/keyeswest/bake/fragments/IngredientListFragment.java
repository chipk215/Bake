package com.keyeswest.bake.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyeswest.bake.R;
import com.keyeswest.bake.adapters.IngredientAdapter;
import com.keyeswest.bake.models.Ingredient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class IngredientListFragment extends Fragment {

    private static final String TAG = "IngredientListFragment";
    private static final String INGREDIENTS_KEY = "ingredients_key";
    private static final String RECIPE_HASH_KEY = "recipeHashKey";


    private List<Ingredient> mIngredients;
    private IngredientAdapter mIngredientAdapter;
    private String mRecipeHash;

    private Hashtable<String, Boolean> mIngredientCheckboxState;

    @BindView(R.id.ingredient_recycler_view)
    RecyclerView mIngredientRecyclerView;

    private Unbinder mUnbinder;

    public static IngredientListFragment newInstance(List<Ingredient> ingredients, String recipeHash){
        Bundle args = new Bundle();
        args.putParcelableArrayList(INGREDIENTS_KEY, (ArrayList<Ingredient>)ingredients);
        args.putString(RECIPE_HASH_KEY, recipeHash);
        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public IngredientListFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIngredients = getArguments().getParcelableArrayList(INGREDIENTS_KEY);
        mRecipeHash = getArguments().getString(RECIPE_HASH_KEY);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        final View rootView = inflater.inflate(R.layout.fragment_ingredient_list,
                container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.custom_list_divider));
        mIngredientRecyclerView.addItemDecoration(itemDecorator);


        mIngredientCheckboxState = new Hashtable<>();
        for (Ingredient i : mIngredients){
            Boolean isChecked = getContext().getSharedPreferences(mRecipeHash, MODE_PRIVATE)
                    .getBoolean(i.getIngredientName(), false);
            mIngredientCheckboxState.put(i.getIngredientName(), isChecked);
        }
        setupIngredientAdapter();

        return rootView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onStop(){


        mIngredientCheckboxState = mIngredientAdapter.getCheckBoxStates();

        SharedPreferences.Editor editor = getContext().getSharedPreferences(mRecipeHash, MODE_PRIVATE).edit();

        for (Ingredient i : mIngredients){
            Boolean isChecked = mIngredientCheckboxState.get(i.getIngredientName());
            editor.putBoolean(i.getIngredientName(), isChecked);
        }

        editor.apply();


        super.onStop();
    }

    private void setupIngredientAdapter(){

        mIngredientAdapter = new IngredientAdapter(mIngredients, mIngredientCheckboxState);

        if (isAdded()){
            mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        }
    }


}
