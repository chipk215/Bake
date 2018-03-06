package com.keyeswest.bake.ui;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.keyeswest.bake.R;
import com.keyeswest.bake.StepDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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

        // completion state checkbox
        onView(withId(R.id.step_complete_cb)).check(matches(isDisplayed()));

        // complete label
        onView(withId(R.id.complete_tv)).check(matches(isDisplayed()));

        // step description
        onView(withId(R.id.step_description_tv)).check(matches(isDisplayed()));

        // previous button
        onView(withId(R.id.prev_button)).check(matches(isDisplayed()));

        // next button
        onView(withId(R.id.next_button)).check(matches(isDisplayed()));

    }



}
