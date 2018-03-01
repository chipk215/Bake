package com.keyeswest.bake.models;


import android.content.Context;

import com.keyeswest.bake.R;


import static java.lang.Math.abs;

public class IngredientViewModel {

    // public access for testing
    public final static String CUP = "CUP";
    public final static String GRAM = "G";
    public final static String TABLE_SPOON = "TBLSP";
    public final static String TEASPOON = "TSP";
    public final static String KILOGRAM = "K";
    public final static String OUNCE = "OZ";
    public final static String UNIT = "UNIT";

    private final static double EPSILON = 1E-6;

    private Ingredient mIngredient;

    private Context mContext;


    public IngredientViewModel(Context context, Ingredient ingredient){
        mIngredient = ingredient;
        mContext = context;

    }

    public String getName(){
        return mIngredient.getIngredientName();
    }

    public String getQuantity(){
        return Float.toString(mIngredient.getQuantity());
    }

    public String getMeasure(){

        switch (mIngredient.getMeasure()){
            case GRAM :

                return mContext.getResources().getQuantityString(R.plurals.gram_plural,
                        getPluralQuantity(mIngredient.getQuantity()));

            case CUP:

                return mContext.getResources().getQuantityString(R.plurals.cup_plural,
                        getPluralQuantity(mIngredient.getQuantity()));

            case TABLE_SPOON:
                return mContext.getResources().getQuantityString(R.plurals.tablespoon_plural,
                        getPluralQuantity(mIngredient.getQuantity()));

            case TEASPOON:
                return mContext.getResources().getQuantityString(R.plurals.teaspoon_plural,
                        getPluralQuantity(mIngredient.getQuantity()));

            case KILOGRAM:
                return mContext.getResources().getQuantityString(R.plurals.kilogram_plural,
                        getPluralQuantity(mIngredient.getQuantity()));

            case OUNCE:
                return mContext.getResources().getQuantityString(R.plurals.ounce_plural,
                        getPluralQuantity(mIngredient.getQuantity()));

            case UNIT: return "";

            default: return mIngredient.getMeasure();


        }
    }


    private boolean isSingular(double value){

        return (abs(value - 1.0d) < EPSILON) ? true : false;

    }

    private int getPluralQuantity(double value){

       return isSingular(value) ? 1 : 2;

    }
}
