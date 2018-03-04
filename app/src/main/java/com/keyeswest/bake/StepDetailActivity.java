package com.keyeswest.bake;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyeswest.bake.fragments.StepDetailFragment;
import com.keyeswest.bake.models.Step;

public class StepDetailActivity extends AppCompatActivity {

    public static final String EXTRA_STEP_BUNDLE = "com.keyeswest.bake.step";

    public static Intent newIntent(Context packageContext, Step step){
        Intent intent = new Intent(packageContext, StepDetailActivity.class);
        intent.putExtra(EXTRA_STEP_BUNDLE, step);
        return intent;
    }

    private Step mStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout file activity_step_detail.xml
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState != null){

        }else{
            mStep = getIntent().getParcelableExtra(EXTRA_STEP_BUNDLE);
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mStep);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment)
                    .commit();

        }


    }
}
