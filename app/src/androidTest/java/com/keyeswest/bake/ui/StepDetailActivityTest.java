package com.keyeswest.bake.ui;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.keyeswest.bake.R;
import com.keyeswest.bake.StepDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.keyeswest.bake.ui.Utils.isTablet;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest extends StepBaseTest{

    @Rule
    public ActivityTestRule mActivityTestRule =
            new ActivityTestRule<>(StepDetailActivity.class,
                    false, false);

    @Test
    public void launchTest(){
        Intent intent = StepDetailActivity.newIntent(getTargetContext(), mRecipe.getSteps(), 0);
        mActivityTestRule.launchActivity(intent);

        if (! isTablet(InstrumentationRegistry.getTargetContext())) {
            // completion state checkbox
            onView(withId(R.id.step_complete_cb)).check(matches(isDisplayed()));

            // complete label
            onView(withId(R.id.complete_tv)).check(matches(isDisplayed()));

        }


        // step description
        onView(withId(R.id.step_description_tv)).check(matches(isDisplayed()));

        // previous button
        onView(withId(R.id.prev_button)).check(matches(isDisplayed()));

        // next button
        onView(withId(R.id.next_button)).check(matches(isDisplayed()));

    }

    @Test
    public void firstStepValuesTest(){
        Intent intent = StepDetailActivity.newIntent(getTargetContext(), mRecipe.getSteps(), 0);
        mActivityTestRule.launchActivity(intent);

        // check the step description
        onView(withId(R.id.step_description_tv)).check(matches(withText(mRecipe.getSteps().get(0).getDescription())));

        if (! isTablet(InstrumentationRegistry.getTargetContext())) {
            //verify the checkbox is unchecked
            onView(withId(R.id.step_complete_cb)).check(matches(isNotChecked()));
        }

        // verify previous button is disabled
        onView(withId(R.id.prev_button)).check(matches(not(isEnabled())));

        //verify next button is enabled
        onView(withId(R.id.next_button)).check(matches(isEnabled()));
    }


    @Test
    public void secondStepValuesTest(){
        Intent intent = StepDetailActivity.newIntent(getTargetContext(), mRecipe.getSteps(), 1);
        mActivityTestRule.launchActivity(intent);

        // check the step description
        onView(withId(R.id.step_description_tv)).check(matches(withText(mRecipe.getSteps().get(1).getDescription())));

        if (! isTablet(InstrumentationRegistry.getTargetContext())) {
            //verify the checkbox is unchecked
            onView(withId(R.id.step_complete_cb)).check(matches(isNotChecked()));
        }

        // verify previous button is enabled
        onView(withId(R.id.prev_button)).check(matches(isEnabled()));

        //verify next button is enabled
        onView(withId(R.id.next_button)).check(matches(isEnabled()));

    }

    @Test
    public void lastStepValuesTest(){

        int lastIndex = mRecipe.getSteps().size()-1;

        Intent intent = StepDetailActivity.newIntent(getTargetContext(),
                mRecipe.getSteps(), lastIndex);

        mActivityTestRule.launchActivity(intent);

        // check the step description
        onView(withId(R.id.step_description_tv)).check(matches(withText(mRecipe.getSteps()
                .get(lastIndex).getDescription())));

        if (! isTablet(InstrumentationRegistry.getTargetContext())) {
            //verify the checkbox is unchecked
            onView(withId(R.id.step_complete_cb)).check(matches(isNotChecked()));
        }

        // verify previous button is enabled
        onView(withId(R.id.prev_button)).check(matches(isEnabled()));

        //verify next button is disabled
        onView(withId(R.id.next_button)).check(matches(not(isEnabled())));

    }

    @Test
    public void verifyClickingOnEnabledPreviousButtonLastItemLoadsPreviousStep(){

        int lastIndex = mRecipe.getSteps().size()-1;

        Intent intent = StepDetailActivity.newIntent(getTargetContext(),
                mRecipe.getSteps(),lastIndex);

        mActivityTestRule.launchActivity(intent);

        //verify correct step is displayed
        onView(withId(R.id.step_description_tv)).check(matches(withText(mRecipe.getSteps().get(lastIndex).getDescription())));

        // verify previous button is enabled
        onView(withId(R.id.prev_button)).check(matches(isEnabled()));

        //verify next button is disabled
        onView(withId(R.id.next_button)).check(matches(not(isEnabled())));


        //click the previous button
        onView(withId(R.id.prev_button)).perform(click());

        // check the  previous step description
        onView(withId(R.id.step_description_tv)).check(matches(withText(mRecipe.getSteps().get(lastIndex-1).getDescription())));

        //both navigation buttons should be enabled
        // verify previous button is enabled
        onView(withId(R.id.prev_button)).check(matches(isEnabled()));

        //verify next button is enabled
        onView(withId(R.id.next_button)).check(matches(isEnabled()));

    }


    @Test
    public void verifyClickingOnEnabledNextButtonLoadsNextStep(){

        Intent intent = StepDetailActivity.newIntent(getTargetContext(), mRecipe.getSteps(), 0);
        mActivityTestRule.launchActivity(intent);

        //verify correct step is displayed
        onView(withId(R.id.step_description_tv)).check(matches(withText(mRecipe.getSteps().get(0).getDescription())));

        // verify previous button is disabled
        onView(withId(R.id.prev_button)).check(matches(not(isEnabled())));

        //verify next button is enabled
        onView(withId(R.id.next_button)).check(matches(isEnabled()));

        //click the next button
        onView(withId(R.id.next_button)).perform(click());

        // check the  next step description
        onView(withId(R.id.step_description_tv)).check(matches(withText(mRecipe.getSteps().get(1).getDescription())));

        //both navigation buttons should be enabled
        // verify previous button is enabled
        onView(withId(R.id.prev_button)).check(matches(isEnabled()));

        //verify next button is enabled
        onView(withId(R.id.next_button)).check(matches(isEnabled()));


    }

}
