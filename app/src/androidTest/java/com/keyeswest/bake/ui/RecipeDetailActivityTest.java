package com.keyeswest.bake.ui;

import android.content.Intent;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.keyeswest.bake.R;
import com.keyeswest.bake.RecipeDetailActivity;
import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.IngredientViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.keyeswest.bake.ui.Utils.atPosition;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest extends StepBaseTest {


    @Rule
    public ActivityTestRule mActivityTestRule =
            new ActivityTestRule<>(RecipeDetailActivity.class,
                    false, false);


    @Test
    public void launchTest() {

        Intent intent = RecipeDetailActivity.newIntent(getTargetContext(), mRecipe);
        mActivityTestRule.launchActivity(intent);

        // Check the views provided by the RecipeDetailFragment
        onView(withId(R.id.recipe_image_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_name_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.step_label_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.step_count_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.serve_label_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.servings_count_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.description_tv)).check(matches(isDisplayed()));

        // check for the MAKE IT Button
        onView(withId(R.id.recipe_make_it_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_make_it_btn)).check(matches(isEnabled()));

        // verify the reset button is visible and disabled
        onView(withId(R.id.ing_reset_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.ing_reset_btn)).check(matches(not(isEnabled())));


        //Check for the views provided by the IngredientsListFragment
        onView(withId(R.id.ingredients_label_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredient_recycler_view)).check(matches(isDisplayed()));

        Ingredient ingredient = mRecipe.getIngredients().get(0);
        IngredientViewModel viewModel = new IngredientViewModel(getTargetContext(),ingredient);


        // verify the text of first checkbox item in the list
        onView(withId(R.id.ingredient_recycler_view))
                .check(matches(atPosition(0,
                        hasDescendant(withText(viewModel.getIngredientInfo())))));

        // verify the checkbox in the first item in the list is unchecked
        onView(withId(R.id.ingredient_recycler_view))
                .check(matches(atPosition(0,hasDescendant(isNotChecked()))));

    }

    @Test
    public void clickOnIngredientTest(){


        Intent intent = RecipeDetailActivity.newIntent(getTargetContext(), mRecipe);
        mActivityTestRule.launchActivity(intent);

        // verify the checkbox in the first item in the list is unchecked
        onView(withId(R.id.ingredient_recycler_view))
                .check(matches(atPosition(0,hasDescendant(isNotChecked()))));

        // click on the item
        onView(withId(R.id.ingredient_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        Utils.clickChildViewWithId(R.id.checkBox) ));


        // verify the checkbox in the first item in the list is checked
        onView(withId(R.id.ingredient_recycler_view))
                .check(matches(atPosition(0,hasDescendant(isChecked()))));

        // verify the reset button is visible and enabled
        onView(withId(R.id.ing_reset_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.ing_reset_btn)).check(matches(isEnabled()));

    }

    @Test
    public void valuesTest(){

        Intent intent = RecipeDetailActivity.newIntent(getTargetContext(), mRecipe);
        mActivityTestRule.launchActivity(intent);


        onView(withId(R.id.recipe_name_tv)).check(matches(withText(mRecipe.getName())));
        onView(withId(R.id.step_label_tv)).check(matches(
                withText(getTargetContext().getResources().getString(R.string.prep_steps))));

        onView(withId(R.id.step_count_tv)).check(matches(
                withText(Integer.toString(mRecipe.getSteps().size()))));

        onView(withId(R.id.serve_label_tv)).check(matches(
                withText(getTargetContext().getResources().getString(R.string.servings))));

        onView(withId(R.id.servings_count_tv)).check(matches(withText(
                Integer.toString(mRecipe.getServings()))));

        onView(withId(R.id.description_tv)).check(matches(withText(mRecipe.getDescription())));

    }


    //Note: This test fails when run on real device. The wrong button is clicked in the 2nd step
    // the test passes on an emulator
    @Test
    public void clickingMakeButtonLaunchesStepsListActivityTest(){
        Intent intent = RecipeDetailActivity.newIntent(getTargetContext(), mRecipe);
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.recipe_make_it_btn)).check(matches(isDisplayed()));

        onView(withId(R.id.recipe_make_it_btn)).perform(scrollTo(),click());

        onView(withId(R.id.steps_recyclerView)).check(matches(isDisplayed()));

    }
}
