package com.keyeswest.bake.fragments;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
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

import com.keyeswest.bake.Widget.BakeAppWidget;
import com.keyeswest.bake.Widget.RecipeWidgetService;
import com.keyeswest.bake.adapters.IngredientAdapter;
import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.tasks.ReadCheckboxStates;
import com.keyeswest.bake.utilities.WriteSharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Handles the displaying of the ingredients list
 */
public class IngredientListFragment extends Fragment {

    @SuppressWarnings("unused")
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        //noinspection unchecked
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


    /**
     * Rather than update the shared preferences file every time the user checks or unchecks an
     * ingredient, the shared preferences file is updated when the user navigates away from the
     * view.
     *
     * This is seemed like a good idea but does have one consequence to the widget implementation,
     * the widget isn't updated with refreshed ingredient check state unless the user makes
     * a checkbox change and then leaves the fragment.
     *
     * In the future I may not pre-optimize until the the performance of the app requires
     * optimization and just update shared preferences on every state change.
     */
    @Override
    public void onPause(){

        // Update the shared preferences file on a worker thread
        WriteSharedPreferences<Ingredient> prefWriter = new WriteSharedPreferences<>(getContext(),
                mRecipePrefsIngredientsFilename, mIngredients);
        new Thread(prefWriter).start();

        //Update the widget
        RecipeWidgetService.refreshIngredients();

        // Force an update to the widget provider
        Context context = getContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        int[] ids = appWidgetManager
                .getAppWidgetIds(new ComponentName(context, BakeAppWidget.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(ids,R.id.item_list);
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
