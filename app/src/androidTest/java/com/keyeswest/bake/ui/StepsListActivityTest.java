package com.keyeswest.bake.ui;


import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.keyeswest.bake.R;
import com.keyeswest.bake.StepsListActivity;

import com.keyeswest.bake.models.StepViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



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
public class StepsListActivityTest extends StepBaseTest{


    @Rule
    public ActivityTestRule mActivityTestRule =
            new ActivityTestRule<>(StepsListActivity.class,
                    false, false);


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
