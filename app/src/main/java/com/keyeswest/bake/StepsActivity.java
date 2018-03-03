package com.keyeswest.bake;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyeswest.bake.fragments.StepsFragment;
import com.keyeswest.bake.models.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsActivity extends SingleFragmentActivity {


    private static final String EXTRA_STEPS = "com.keyeswest.bake.steps";

    private static final String BUNDLE_STEPS_KEY = "bundleStepsKey";
    private static final String BUNDLE_HASH_KEY = "bundleHashKey";


    public static Intent newIntent(Context context, List<Step> steps, String stepHash){
        Intent intent = new Intent(context, StepsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BUNDLE_STEPS_KEY, (ArrayList<Step>) steps);
        bundle.putString(BUNDLE_HASH_KEY, stepHash);
        intent.putExtra(EXTRA_STEPS, bundle);
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        Bundle bundle = getIntent().getBundleExtra(EXTRA_STEPS);
        List<Step> steps = bundle.getParcelableArrayList(BUNDLE_STEPS_KEY);
        String stepHash = bundle.getString(BUNDLE_HASH_KEY);
        return StepsFragment.newInstance(steps, stepHash);
    }


}
