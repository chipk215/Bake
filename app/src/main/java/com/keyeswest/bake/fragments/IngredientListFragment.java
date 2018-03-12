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
import android.widget.Button;
import android.widget.ScrollView;


import com.keyeswest.bake.R;

import com.keyeswest.bake.adapters.IngredientAdapter;
import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.tasks.ReadCheckboxStates;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class IngredientListFragment extends Fragment {

    private static final String TAG = "IngredientListFragment";
    private static final String RECIPE_KEY = "recipeKey";

    private List<Ingredient> mIngredients;
    private IngredientAdapter mIngredientAdapter;
    private String mRecipePrefsIngredientsFilename;

    @BindView(R.id.ingredient_recycler_view)
    RecyclerView mIngredientRecyclerView;

    @BindView(R.id.ing_reset_btn)Button mResetButton;

    private Unbinder mUnbinder;

    public static IngredientListFragment newInstance(Recipe recipe){
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_KEY, recipe);

        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public IngredientListFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Recipe  recipe= getArguments().getParcelable(RECIPE_KEY);
        mIngredients = recipe.getIngredients();
        mRecipePrefsIngredientsFilename = recipe.getSharedPreferencesIngredientFileName();
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

        setupIngredientAdapter();

        ReadCheckboxStates<Ingredient> readIngredientStateTask = new ReadCheckboxStates<>(getContext(),
                mRecipePrefsIngredientsFilename, new ReadCheckboxStates.ResultsCallback<Ingredient>(){
            @Override
            public void CheckboxStates(List<Ingredient> updatedList) {

                setupResetButton();
                updateIngredients(updatedList);
            }
        });

        readIngredientStateTask.execute(mIngredients);


        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear all the checkboxes
                for (Ingredient i : mIngredients){
                    i.setCheckedState(false);
                }

                mIngredientAdapter.notifyItemRangeChanged(0,mIngredients.size());
                mResetButton.setEnabled(false);
            }
        });



        return rootView;
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onPause(){

        // revisit should this be off the UI thread?
        // We would wait to call super.onPause until complete, right?


        SharedPreferences.Editor editor = getContext().getSharedPreferences(mRecipePrefsIngredientsFilename, MODE_PRIVATE).edit();

        for (Ingredient i : mIngredients){
            Boolean isChecked = i.getCheckedState();
            editor.putBoolean(i.getUniqueId(), isChecked);
        }

        editor.apply();


        super.onPause();
    }



    private void updateIngredients(List<Ingredient> ingredients){

        mIngredients = ingredients;
        mIngredientAdapter.notifyDataSetChanged();

    }
    private void setupIngredientAdapter(){

        if (isAdded()){
            mIngredientAdapter = new IngredientAdapter(mIngredients, new IngredientAdapter.OnIngredientClickListener() {
                @Override
                public void onIngredientClick() {
                    setupResetButton();
                }
            });

            mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        }

    }

    private void setupResetButton(){

        boolean setEnabled = false;
        for (Ingredient i : mIngredients){
            if (i.getCheckedState()){
                setEnabled = true;
                break;
            }
        }
        mResetButton.setEnabled(setEnabled);
    }


}
