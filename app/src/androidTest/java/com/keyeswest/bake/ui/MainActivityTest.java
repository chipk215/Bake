package com.keyeswest.bake.ui;



import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.keyeswest.bake.MainActivity;
import com.keyeswest.bake.R;


import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.keyeswest.bake.ui.utils.atPosition;
import static com.keyeswest.bake.ui.utils.isTablet;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String NUTELLA = "Nutella Pie";
    private static final String CHEESECAKE = "Cheesecake";
    private static final String BROWNIES = "Brownies";


    // Launch the MainActivity prior to each test
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void launchTest(){
        onView(withId(R.id.recipe_recycler_view)).check(matches(isDisplayed()));
    }


    /**
     * Check the first and fourth items in the list for correct names
     */
    @Test
    public void recipeNameTest(){

        // check the first item
        onView(withId(R.id.recipe_recycler_view))
                .check(matches(atPosition(0,hasDescendant(withText(NUTELLA)))));

        //check the 4th item
        onView(withId(R.id.recipe_recycler_view))
                .perform(scrollToPosition(3))
                .check(matches(atPosition(3,hasDescendant(withText(CHEESECAKE)))));

    }


    @Test
    public void clickRecipeItemOpensRecipeDetailActivityOnPhone(){

        // The isTablet check ensures this test only runs on small screens
        if (! isTablet(InstrumentationRegistry.getTargetContext())) {

           onView(withId(R.id.recipe_recycler_view))
                   .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

           onView(withId(R.id.recipe_name_tv)).check(matches(isDisplayed()));

           onView(withId(R.id.recipe_name_tv)).check(matches(withText(BROWNIES)));
        }

        Assert.assertTrue(true);

    }


}
