package com.keyeswest.bake;

import android.support.test.InstrumentationRegistry;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.keyeswest.bake.utilities.RecipeFetcher;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class RecipeFetcherTest {

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, false, true);

    @Test
    public void getRecipesHappyPathTest() throws Throwable{

        final CountDownLatch signal = new CountDownLatch(1);

        RecipeFetcher fetcher = new RecipeFetcher(InstrumentationRegistry.getTargetContext(),
                new RecipeFetcher.RecipeJsonResults() {
                    @Override
                    public void handleRecipeJSON(String recipeJson) {
                        String fetchFailed = "Failed to fetch recipes";
                        Assert.assertNotNull(fetchFailed, recipeJson);
                        String contentFailed = "Failed to find expected content";
                        Assert.assertTrue(contentFailed,recipeJson.contains("Nutella Pie"));

                        signal.countDown();
                    }
                });

        fetcher.fetchRecipes(mActivityTestRule.getActivity().getSupportLoaderManager());

        // returns false if time out occurs
        boolean result = signal.await(30, TimeUnit.SECONDS);
        Assert.assertTrue("Signal timed out waiting for network response",result);


    }
}
