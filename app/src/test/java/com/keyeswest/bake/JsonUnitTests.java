package com.keyeswest.bake;


import com.google.gson.Gson;
import com.keyeswest.bake.models.Recipe;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsonUnitTests {

    private String mRecipeJsonString;

    private final String[] mRecipeNames = {"Nutella Pie", "Brownies", "Yellow Cake", "Cheesecake"};

    private final int[] mIngredientCounts = {9, 10, 10, 9};

    //note recipe 3 Yellow Cake does not have a step 7
    private final int[] mStepCounts = {7, 10, 13, 13};

    private final int[] mServingCounts = {8, 8, 8, 8};


    private final int mNumberRecipes = 4;

    @Before
    public void setup() throws IOException{
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("recipe.json");
        assertTrue(resource.getPath().endsWith("recipe.json"));

        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        mRecipeJsonString = new String(encoded, StandardCharsets.UTF_8 );
    }

    @Test
    public void jsonValidTest(){
        assertNotNull(mRecipeJsonString);
    }

    @Test
    public void recipeDeserializationTest(){
        Gson gson = new Gson();
        Recipe[] recipes = gson.fromJson(mRecipeJsonString, Recipe[].class);
        assertNotNull(recipes);
        assertEquals(mNumberRecipes, recipes.length);
        for (int i=0; i< mNumberRecipes; i++){
            assertEquals(mRecipeNames[i], recipes[i].getName());
            assertEquals(mIngredientCounts[i], recipes[i].getIngredients().size());
            assertEquals(mStepCounts[i], recipes[i].getSteps().size());
            assertEquals(mServingCounts[i], recipes[i].getServings());
        }
    }
}
