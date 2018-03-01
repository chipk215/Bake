package com.keyeswest.bake;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.IngredientViewModel;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IngredientViewModelTest {

    private Context mContext = InstrumentationRegistry.getTargetContext();


    @Test
    public void cupTest(){
        String singular = "cup";
        String plural = "cups";
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("TestIngredient");
        ingredient.setMeasure(IngredientViewModel.CUP);

        // 2 cups
        ingredient.setQuantity(2);
        IngredientViewModel viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

        // 1 cup
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(singular,viewModel.getMeasure());

        // 1 cup
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertFalse(plural.equals(viewModel.getMeasure()));

        // 0.5 cups
        ingredient.setQuantity(0.5f);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

    }

    @Test
    public void gramTest(){
        String singular = "gram";
        String plural = "grams";
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("TestIngredient");
        ingredient.setMeasure(IngredientViewModel.GRAM);

        // 2 grams
        ingredient.setQuantity(2);
        IngredientViewModel viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

        // 1 gram
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(singular,viewModel.getMeasure());

        // 1 gram
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertFalse(plural.equals(viewModel.getMeasure()));

        // 0.5 grams
        ingredient.setQuantity(0.5f);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

    }

    @Test
    public void tablespoonTest(){
        String singular = "tablespoon";
        String plural = "tablespoons";
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("TestIngredient");
        ingredient.setMeasure(IngredientViewModel.TABLE_SPOON);

        // 2 tablespoons
        ingredient.setQuantity(2);
        IngredientViewModel viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

        // 1 tablespoon
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(singular,viewModel.getMeasure());

        // 1 tablespoon
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertFalse(plural.equals(viewModel.getMeasure()));

        // 0.5 tablespoons
        ingredient.setQuantity(0.5f);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

    }

    @Test
    public void teaspoonTest(){
        String singular = "teaspoon";
        String plural = "teaspoons";
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("TestIngredient");
        ingredient.setMeasure(IngredientViewModel.TEASPOON);

        // 2 teaspoons
        ingredient.setQuantity(2);
        IngredientViewModel viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

        // 1 teaspoon
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(singular,viewModel.getMeasure());

        // 1 teaspoon
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertFalse(plural.equals(viewModel.getMeasure()));

        // 0.5 teaspoons
        ingredient.setQuantity(0.5f);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

    }


    @Test
    public void kilogramTest(){
        String singular = "kilogram";
        String plural = "kilograms";
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("TestIngredient");
        ingredient.setMeasure(IngredientViewModel.KILOGRAM);

        // 2 kilograms
        ingredient.setQuantity(2);
        IngredientViewModel viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

        // 1 kilogram
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(singular,viewModel.getMeasure());

        // 1 kilogram
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertFalse(plural.equals(viewModel.getMeasure()));

        // 0.5 kilograms
        ingredient.setQuantity(0.5f);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

    }

    @Test
    public void ounceTest(){
        String singular = "ounce";
        String plural = "ounces";
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("TestIngredient");
        ingredient.setMeasure(IngredientViewModel.OUNCE);

        // 2 ounces
        ingredient.setQuantity(2);
        IngredientViewModel viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

        // 1 ounce
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(singular,viewModel.getMeasure());

        // 1 ounce
        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertFalse(plural.equals(viewModel.getMeasure()));

        // 0.5 ounces
        ingredient.setQuantity(0.5f);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

    }

    @Test
    public void unitTest(){
        String singular = "";
        String plural = "";
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("TestIngredient");
        ingredient.setMeasure(IngredientViewModel.UNIT);

        ingredient.setQuantity(2);
        IngredientViewModel viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

        ingredient.setQuantity(1);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(singular,viewModel.getMeasure());

        ingredient.setQuantity(0.5f);
        viewModel = new IngredientViewModel(mContext, ingredient);
        Assert.assertEquals(plural,viewModel.getMeasure());

    }

}
