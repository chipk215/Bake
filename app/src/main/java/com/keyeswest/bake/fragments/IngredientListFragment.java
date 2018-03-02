package com.keyeswest.bake.fragments;


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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IngredientListFragment extends Fragment {

    private static final String INGREDIENTS_KEY = "ingredients_key";
    private List<Ingredient> mIngredients;
    private IngredientAdapter mIngredientAdapter;

    @BindView(R.id.ingredient_recycler_view)
    RecyclerView mIngredientRecyclerView;

    private Unbinder mUnbinder;

    public static IngredientListFragment newInstance(List<Ingredient> ingredients){
        Bundle args = new Bundle();
        args.putParcelableArrayList(INGREDIENTS_KEY, (ArrayList<Ingredient>)ingredients);
        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public IngredientListFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIngredients = getArguments().getParcelableArrayList(INGREDIENTS_KEY);
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


}
