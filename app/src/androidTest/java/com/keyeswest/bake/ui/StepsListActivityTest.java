package com.keyeswest.bake.ui;


import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.keyeswest.bake.R;
import com.keyeswest.bake.StepsListActivity;
import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;
import com.keyeswest.bake.models.StepViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.InstrumentationRegistry.getTargetContext;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.keyeswest.bake.ui.utils.atPosition;


@RunWith(AndroidJUnit4.class)
public class StepsListActivityTest {

    private Recipe mRecipe;

    @Rule
    public ActivityTestRule mActivityTestRule =
            new ActivityTestRule<>(StepsListActivity.class,
                    false, false);

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
        step.setId(1);
        step.setShortDescription("Short description one");
        step.setDescription("Long description one");
        step.setVideoURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/" +
                "590129ad_17-frost-all-around-cake-yellow-cake/" +
                "17-frost-all-around-cake-yellow-cake.mp4");
        step.setThumbnailURL("");
        steps.add(step);

        Step stepTwo = new Step();
        stepTwo.setId(2);
        stepTwo.setShortDescription("Short description two");
        stepTwo.setDescription("Long description two");
        stepTwo.setVideoURL("");
        stepTwo.setThumbnailURL("");
        steps.add(stepTwo);

        Step stepThree = new Step();
        stepThree.setId(3);
        stepThree.setShortDescription("Short description three");
        stepThree.setDescription("Long description three");
        stepThree.setVideoURL("");
        stepThree.setThumbnailURL("");
        steps.add(stepThree);


        mRecipe.setSteps(steps);

        mRecipe.setRecipeImageUriString( "android.resource://com.keyeswest.bake/drawable/baking");
        mRecipe.setDescription("Recipe Description.");

        // clear shared preferences
        getTargetContext()
                .getSharedPreferences(mRecipe.getSharedPreferencesStepsFileName(), 0)
                .edit().clear().commit();

    }

    @Test
    public void launchTest(){
        Intent intent = StepsListActivity.newIntent(getTargetContext(), mRecipe);
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.steps_recyclerView)).check(matches(isDisplayed()));

        // check the text of the 1st item in the list
        StepViewModel viewModel = new StepViewModel(getTargetContext(),mRecipe.getSteps().get(0));
        onView(withId(R.id.steps_recyclerView))
                .check(matches(atPosition(0,
                        hasDescendant(withText(viewModel.getListLabel())))));

        // check the 3rd item
        viewModel = new StepViewModel(getTargetContext(),mRecipe.getSteps().get(2));
        onView(withId(R.id.steps_recyclerView))
                .check(matches(atPosition(2,
                        hasDescendant(withText(viewModel.getListLabel())))));

    }

    @Test
    public void clickOnCheckboxStepTest(){
        Intent intent = StepsListActivity.newIntent(getTargetContext(), mRecipe);
        mActivityTestRule.launchActivity(intent);

        // verify the checkbox in the first item in the list is unchecked
        onView(withId(R.id.steps_recyclerView))
                .check(matches(atPosition(0,hasDescendant(isNotChecked()))));

        // click on the item
        onView(withId(R.id.steps_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        utils.clickChildViewWithId(R.id.step_checkBox)));


        // verify the checkbox in the first item in the list is unchecked
        onView(withId(R.id.steps_recyclerView))
                .check(matches(atPosition(0,hasDescendant(isChecked()))));


    }

    @Test
    public void clickOnStepNavigatesToStepDetailTest(){

        Intent intent = StepsListActivity.newIntent(getTargetContext(), mRecipe);
        mActivityTestRule.launchActivity(intent);

        // click on the item
        onView(withId(R.id.steps_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check for the complete label on the step detail view
        onView(withId(R.id.complete_tv)).check(matches(isDisplayed()));




    }


}
