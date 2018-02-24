package com.keyeswest.bake;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyeswest.bake.services.RecipeFirebaseJobService;

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // RecipeFirebaseJobService.scheduleRecipeFetcher(this);
    }



    @Override
    protected void onStop(){
       // RecipeFirebaseJobService.stopFetching(this);
        super.onStop();

    }

}
