package com.keyeswest.bake.tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.keyeswest.bake.models.Ingredient;

import java.util.Hashtable;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ReadCheckboxStates extends AsyncTask<String, Void, Hashtable<String, Boolean>> {

    private ResultsCallback mCallback;
    private List<Ingredient> mIngredients;
    private Context mContext;

    private Hashtable<String, Boolean> mIngredientCheckboxState;

    public interface ResultsCallback{
        void ingredientCheckboxStates(Hashtable<String, Boolean> checkboxStates);
    }

    public ReadCheckboxStates(Context context, List<Ingredient> ingredients, ResultsCallback callback){
        mCallback = callback;
        mIngredients = ingredients;
        mContext = context;
    }

    @Override
    protected Hashtable<String, Boolean> doInBackground(String... strings) {

        if ((strings == null) || (strings[0] == null)) {
            return null;
        }

        mIngredientCheckboxState = new Hashtable<>();
        for (Ingredient i : mIngredients){
            Boolean isChecked = mContext.getSharedPreferences(strings[0], MODE_PRIVATE)
                    .getBoolean(i.getIngredientName(), false);
            mIngredientCheckboxState.put(i.getIngredientName(), isChecked);
        }

        return mIngredientCheckboxState;
    }

    @Override
    protected void onPostExecute(Hashtable<String, Boolean> checkboxStates) {
        mCallback.ingredientCheckboxStates(checkboxStates);
    }
}
