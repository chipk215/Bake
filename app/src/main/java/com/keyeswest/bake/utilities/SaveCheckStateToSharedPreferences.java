package com.keyeswest.bake.utilities;


import android.content.Context;
import android.content.SharedPreferences;

import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;

import static android.content.Context.MODE_PRIVATE;

public class SaveCheckStateToSharedPreferences {

    public static void saveSteps(Context context, Recipe recipe){
        SharedPreferences.Editor editor = context.getSharedPreferences(recipe.getSharedPreferencesStepsFileName(), MODE_PRIVATE).edit();

        for (Step i : recipe.getSteps()){
            Boolean isChecked = i.getCheckedState();
            editor.putBoolean(i.getUniqueId(), isChecked);
        }

        editor.commit();
    }
}
