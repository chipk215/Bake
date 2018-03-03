package com.keyeswest.bake.fragments;


import android.content.Intent;
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

import com.keyeswest.bake.R;
import com.keyeswest.bake.StepsActivity;
import com.keyeswest.bake.adapters.IngredientAdapter;
import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.tasks.ReadCheckboxStates;

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
    private static final String RECIPE_PREFS_INGREDIENTS_FILENAME_KEY = "recipeIngredientsPrefsKey";


    private List<Ingredient> mIngredients;
    private IngredientAdapter mIngredientAdapter;
    private String mRecipePrefsIngredientsFilename;

    private Hashtable<String, Boolean> mIngredientCheckboxState;

    @BindView(R.id.ingredient_recycler_view)
    RecyclerView mIngredientRecyclerView;



    private Unbinder mUnbinder;

    public static IngredientListFragment newInstance(List<Ingredient> ingredients, String preferencsFilename){
        Bundle args = new Bundle();
        args.putParcelableArrayList(INGREDIENTS_KEY, (ArrayList<Ingredient>)ingredients);
        args.putString(RECIPE_PREFS_INGREDIENTS_FILENAME_KEY, preferencsFilename);
        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public IngredientListFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIngredients = getArguments().getParcelableArrayList(INGREDIENTS_KEY);
        mRecipePrefsIngredientsFilename = getArguments().getString(RECIPE_PREFS_INGREDIENTS_FILENAME_KEY);
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

        ReadCheckboxStates<Ingredient> task = new ReadCheckboxStates<>(getContext(), mRecipePrefsIngredientsFilename, new ReadCheckboxStates.ResultsCallback(){

            @Override
            public void CheckboxStates(Hashtable<String, Boolean> checkboxStates) {
                mIngredientCheckboxState = checkboxStates;
                setupIngredientAdapter();
            }
        });

        task.execute(mIngredients);





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
        mIngredientCheckboxState = mIngredientAdapter.getCheckBoxStates();

        SharedPreferences.Editor editor = getContext().getSharedPreferences(mRecipePrefsIngredientsFilename, MODE_PRIVATE).edit();

        for (Ingredient i : mIngredients){
            Boolean isChecked = mIngredientCheckboxState.get(i.getUniqueId());
            editor.putBoolean(i.getUniqueId(), isChecked);
        }

        editor.apply();


        super.onPause();
    }

    private void setupIngredientAdapter(){

        mIngredientAdapter = new IngredientAdapter(mIngredients, mIngredientCheckboxState);

        if (isAdded()){
            mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        }
    }


}
