package com.keyeswest.bake.ui;


import android.support.test.runner.AndroidJUnit4;

import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;

import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;


@RunWith(AndroidJUnit4.class)
public abstract class  StepBaseTest {

    protected Recipe mRecipe;

    @Before
    public void initRecipe(){

        mRecipe = new Recipe();

        mRecipe.setId(1);
        mRecipe.setName("Test Recipe");
        mRecipe.setServings(8);

        List<Ingredient> ingredients = new ArrayList<>();

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("Ingredient One");
        ingredient.setMeasure("OZ");
        ingredient.setQuantity(10.2f);
        ingredients.add(ingredient);


        mRecipe.setIngredients(ingredients);

        // Add three steps
        List<Step> steps = new ArrayList<>();
        Step step = new Step();
        step.setId(0);
        step.setShortDescription("Short description one");
        step.setDescription("Long description one");
        step.setVideoURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/" +
                "590129ad_17-frost-all-around-cake-yellow-cake/" +
                "17-frost-all-around-cake-yellow-cake.mp4");
        step.setThumbnailURL("");
        steps.add(step);

        Step stepTwo = new Step();
        stepTwo.setId(1);
        stepTwo.setShortDescription("Short description two");
        stepTwo.setDescription("Long description two");
        stepTwo.setVideoURL("");
        stepTwo.setThumbnailURL("");
        steps.add(stepTwo);

        Step stepThree = new Step();
        stepThree.setId(2);
        stepThree.setShortDescription("Short description three");
        stepThree.setDescription("Long description three");
        stepThree.setVideoURL("");
        stepThree.setThumbnailURL("");
        steps.add(stepThree);


        mRecipe.setSteps(steps);

        mRecipe.setRecipeImageUriString( "android.resource://com.keyeswest.bake/drawable/baking");
        mRecipe.setDescription("Recipe Description.");


        // these initializing steps are handled by the de-serializer
        int lastStep = mRecipe.getSteps().size();

        for (Step s : mRecipe.getSteps()){
            s.setNumberOfStepsInRecipe(lastStep);
        }

        // clear shared preferences
        getTargetContext()
                .getSharedPreferences(mRecipe.getSharedPreferencesStepsFileName(), 0)
                .edit().clear().commit();

    }


}
