package com.keyeswest.bake.ui;

import android.content.Intent;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.keyeswest.bake.R;
import com.keyeswest.bake.RecipeDetailActivity;
import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.IngredientViewModel;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
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
public class RecipeDetailActivityTest {

    private Recipe mRecipe;

    @Rule
    public ActivityTestRule mActivityTestRule =
            new ActivityTestRule<>(RecipeDetailActivity.class,
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
        ingredient.setMeasure("Bushel");
        ingredient.setQuantity(10.2f);
        ingredients.add(ingredient);
        mRecipe.setIngredients(ingredients);

        List<Step> steps = new ArrayList<>();
        Step step = new Step();
        step.setId(1);
        step.setShortDescription("Short description");
        step.setDescription("Long Description");
        step.setVideoURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/" +
                "590129ad_17-frost-all-around-cake-yellow-cake/" +
                "17-frost-all-around-cake-yellow-cake.mp4");
        step.setThumbnailURL("");
        steps.add(step);
        mRecipe.setSteps(steps);

        mRecipe.setRecipeImageUriString( "android.resource://com.keyeswest.bake/drawable/baking");
        mRecipe.setDescription("Recipe Description.");


        // clear shared preferences
        getTargetContext().getSharedPreferences(mRecipe.getSharedPreferencesIngredientFileName(),
                0).edit().clear().commit();


    }

    @Test
    public void launchTest(){
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
        onView(withId(R.id.make_it_btn)).check(matches(isDisplayed()));

        //Check for the views provided by the IngredientsListFragment
        onView(withId(R.id.ingredients_label_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredient_recycler_view)).check(matches(isDisplayed()));

        Ingredient ingredient = mRecipe.getIngredients().get(0);
        IngredientViewModel viewModel = new IngredientViewModel(getTargetContext(),ingredient);


        // check the text of first checkbox item in the list
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
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


        // verify the checkbox in the first item in the list is unchecked
        onView(withId(R.id.ingredient_recycler_view))
                .check(matches(atPosition(0,hasDescendant(isChecked()))));

    }

    @Test
    public void valuesTest(){

        Intent intent = RecipeDetailActivity.newIntent(getTargetContext(), mRecipe);
        mActivityTestRule.launchActivity(intent);

        //TODO revisit to check image views
       // onView(withId(R.id.recipe_image_view)).check(matches());

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

    @Test
    public void clickingMakeButtonLaunchesStepsListActivityTest(){
        Intent intent = RecipeDetailActivity.newIntent(getTargetContext(), mRecipe);
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.make_it_btn)).perform(click());

        onView(withId(R.id.steps_recyclerView)).check(matches(isDisplayed()));

    }
}
