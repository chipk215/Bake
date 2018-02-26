package com.keyeswest.bake;

import android.support.test.InstrumentationRegistry;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.utilities.NetworkUtilities;
import com.keyeswest.bake.utilities.RecipeFetcher;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class RecipeFetcherTest {

    private final String[] mRecipeNames = {"Nutella Pie", "Brownies", "Yellow Cake", "Cheesecake"};

    private final int[] mIngredientCounts = {9, 10, 10, 9};

    //note recipe 3 Yellow Cake does not have a step 7
    private final int[] mStepCounts = {7, 10, 13, 13};

    private final int mNumberRecipes = 4;

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
                new RecipeFetcher.RecipeResults() {
                    @Override
                    public void handleRecipes(List<Recipe> recipeList) {
                        String fetchFailed = "Failed to fetch recipes";
                        Assert.assertNotNull(fetchFailed, recipeList);
                        checkRecipes(recipeList);


                        signal.countDown();
                    }

                    @Override
                    public void networkUnavailable() {

                    }
                });

        fetcher.fetchRecipes(mActivityTestRule.getActivity().getSupportLoaderManager());

        // returns false if time out occurs
        boolean result = signal.await(30, TimeUnit.SECONDS);
        Assert.assertTrue("Signal timed out waiting for network response",result);

    }

    /**
     * Verifies that the correct callback method is invoked if the wireless network is not
     * available.
     *
     * Mock NetworkUtilities in RecipeFetcher to accommodate test.
     * @throws Throwable
     */
    @Test
    public void getRecipesNoNetworkTest() throws Throwable{

        final CountDownLatch signal = new CountDownLatch(1);

        // create mock of NetworkUtilities to effect no network response
        NetworkUtilities networkUtilitiesMock = mock(NetworkUtilities.class);
        when(networkUtilitiesMock.isNetworkAvailable(InstrumentationRegistry
                .getTargetContext())).thenReturn(false);

        RecipeFetcher fetcher = new RecipeFetcher(InstrumentationRegistry.getTargetContext(),
                new RecipeFetcher.RecipeResults() {
                    @Override
                    public void handleRecipes(List<Recipe> recipeList) {
                        // Should not get this invocation
                        fail("No json response expected");

                        signal.countDown();
                    }

                    @Override
                    public void networkUnavailable() {
                        // we should get this callback
                        assertTrue(true);
                        signal.countDown();

                    }
                });

        //inject the mock
        fetcher.setNetworkUtilities(networkUtilitiesMock);

        // execute the fetch
        fetcher.fetchRecipes(mActivityTestRule.getActivity().getSupportLoaderManager());

        boolean result = signal.await(30, TimeUnit.SECONDS);
        Assert.assertTrue("Signal timed out waiting for network response",result);
    }


    private void checkRecipes(List<Recipe> recipeList){

        String contentFailed = "Failed to find expected content";
        Assert.assertTrue(contentFailed,
                recipeList.get(0).getName().contains("Nutella Pie"));
        assertEquals(mNumberRecipes, recipeList.size());
        for (int i=0; i< mNumberRecipes; i++){
            assertEquals(mRecipeNames[i], recipeList.get(i).getName());
            assertEquals(mIngredientCounts[i], recipeList.get(i).getIngredients().size());
            assertEquals(mStepCounts[i], recipeList.get(i).getSteps().size());
        }
    }


}
