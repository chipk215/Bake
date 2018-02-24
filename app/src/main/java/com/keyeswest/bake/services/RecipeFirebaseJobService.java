package com.keyeswest.bake.services;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;


import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.keyeswest.bake.utilities.NetworkUtilities;
import com.keyeswest.bake.utilities.RecipeFetcher;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class RecipeFirebaseJobService extends JobService {
    private final static String TAG= "R_FirebaseJobService";

    private static boolean sInitialized;

    private AsyncTask<Void, Void, String> mBackgroundTask;

    private static final String RECIPE_JOB_TAG = "recipe_job_tag";
    //daily
    private static final int REMINDER_INTERVAL_MINUTES =  24 * 60;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    //15 minutes
    private static final int SYNC_FLEXTIME_SECONDS = 15*60;


    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mBackgroundTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String recipeUrlString = RecipeFetcher.RECIPE_URL;
                try {
                    URL recipeUrl = new URL(recipeUrlString);
                    String jsonRecipes = new NetworkUtilities().getResponseFromHttpsUrl(recipeUrl);
                    return jsonRecipes;
                }catch(IOException ioe) {
                    ioe.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                  /*
                 * Once the AsyncTask is finished, the job is finished. To inform JobManager that
                 * you're done, you call jobFinished with the jobParameters that were passed to your
                 * job and a boolean representing whether the job needs to be rescheduled. This is
                 * usually if something didn't work and you want the job to try running again.
                 */
                  Log.i(TAG, "Fetched Recipe Data via scheduled job");

                jobFinished(jobParameters, false);
            }
        };
        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }



    synchronized public static void scheduleRecipeFetcher(@NonNull final Context context) {


        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        // COMPLETED (19) Create a new FirebaseJobDispatcher with the driver
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);


        Job constraintReminderJob = dispatcher.newJobBuilder()
                /* The Service that will be used to write to preferences */
                .setService(RecipeFirebaseJobService.class)
                /*
                 * Set the UNIQUE tag used to identify this Job.
                 */
                .setTag(RECIPE_JOB_TAG)
                /*
                 * Network constraints on which this Job should run. In this app, we're using the
                 * device charging constraint so that the job only executes if the device is
                 * charging.
                 *
                 * In a normal app, it might be a good idea to include a preference for this,
                 * as different users may have different preferences on when you should be
                 * syncing your application's data.
                 */
                 .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                // change to boot for now
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the reminders to happen every 15 minutes or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();


        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(constraintReminderJob);


        /* The job has been initialized */
        sInitialized = true;
    }

    synchronized public static void stopFetching(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        dispatcher.cancel(RECIPE_JOB_TAG);

    }
}
