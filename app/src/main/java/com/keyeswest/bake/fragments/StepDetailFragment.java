package com.keyeswest.bake.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.keyeswest.bake.R;
import com.keyeswest.bake.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Detail instructions for recipe including video when available.
 */
public class StepDetailFragment  extends Fragment {

    private static final String TAG="StepDetailFragment";

    private static final String SAVE_STEP_KEY = "saveStepKey";

    private static final String CURRENT_WINDOW_TAG = "CURRENT_WINDOW_TAG";
    private static final String PLAY_BACK_TAG = "PLAY_BACK_TAG";

    private static final float HIDE_VIDEO_PLAYER_PERCENT = 0.1f;

    public static StepDetailFragment newInstance(Step step){
        Bundle args = new Bundle();
        args.putParcelable(SAVE_STEP_KEY, step);

        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private OnStepNavigation mHostActivityCallback;

    public interface OnStepNavigation {
        void onNextSelected(String currentStepId);
        void onPreviousSelected(String currentStepId);
    }

    private OnCompletionStateChange mOnHostActivityCompletionCallback;
    public interface OnCompletionStateChange
    {
        void onCompletionStateChange(Step step);
    }

    private Step mStep;
    private Unbinder mUnbinder;


    @BindView(R.id.step_description_tv)TextView mDescriptionTextView;
    @BindView(R.id.prev_button)Button mPreviousButton;
    @BindView(R.id.next_button)Button mNextButton;
    @BindView(R.id.video_view) SimpleExoPlayerView mPlayerView;

    @Nullable
    @BindView(R.id.horizontalHalf)Guideline mGuideline;

    private int mCurrentWindow =0;
    private long mPlaybackPosition =0;
    private SimpleExoPlayer mPlayer;
    private boolean mPlayWhenReady = true;

    @Nullable
    @BindView(R.id.step_complete_cb)CheckBox mStepCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mStep = getArguments().getParcelable(SAVE_STEP_KEY);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mHostActivityCallback = (OnStepNavigation) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepNavigation");
        }

        try {
            mOnHostActivityCompletionCallback = (OnCompletionStateChange) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCompletionStateChange");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_step_detail,
                container, false);

        mUnbinder = ButterKnife.bind(this, view);
        mDescriptionTextView.setText(mStep.getDescription());

        // requires the step ids to start at 0 and increase by 1
        mPreviousButton.setEnabled(mStep.getId() != 0);

        mNextButton.setEnabled((mStep.getId()+1) < mStep.getNumberOfStepsInRecipe());

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivityCallback.onPreviousSelected(mStep.getUniqueId());
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivityCallback.onNextSelected(mStep.getUniqueId());
            }
        });

        if (mStepCheckBox != null) {
            mStepCheckBox.setChecked(mStep.getCheckedState());

            mStepCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStep.setCheckedState(((CheckBox) v).isChecked());
                    mOnHostActivityCompletionCallback.onCompletionStateChange(mStep);

                }
            });
        }


        if (savedInstanceState != null){
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW_TAG, 0);
            mPlaybackPosition = savedInstanceState.getLong(PLAY_BACK_TAG, 0);
            Log.d(TAG, "Restoring mCurrentWindow= " + mCurrentWindow);
            Log.d(TAG, "Restoring mPlaybackPosition= " + mPlaybackPosition);

        }

        setupVideoPlayerView();


        return view;
    }



    //=== Media Player Code Attribution
    //    https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            Log.d(TAG,"onStart invoking initializePlayer()");
            initializePlayer();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
      //  hideSystemUi();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            Log.d(TAG,"onResume invoking initializePlayer()");
            initializePlayer();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            Log.d(TAG,"onPause invoking releasePlayer()");
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            Log.d(TAG,"onStop invoking releasePlayer()");
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        if (mPlayer != null) {
            mCurrentWindow =mPlayer.getCurrentWindowIndex();
            mPlaybackPosition = mPlayer.getCurrentPosition();
        }

        Log.d(TAG, "mCurrentWindow= " + mCurrentWindow);
        Log.d(TAG, "mPlaybackPosition= " + mPlaybackPosition);
        outState.putInt(CURRENT_WINDOW_TAG, mCurrentWindow);
        outState.putLong(PLAY_BACK_TAG, mPlaybackPosition);

        super.onSaveInstanceState(outState);

    }



    private void initializePlayer(){

        if (! "".equals(mStep.getVideoURL())) {

            mPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            mPlayerView.setPlayer(mPlayer);

            mPlayer.setPlayWhenReady(mPlayWhenReady);
            mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);


            Uri uri = Uri.parse(mStep.getVideoURL());
            MediaSource mediaSource = buildMediaSource(uri);
            mPlayer.prepare(mediaSource, true, false);

        }
    }

    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-keyeswest-bake"))
                .createMediaSource(uri);

    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    private void releasePlayer() {
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }


    // Hide the video player if the step has no corresponding video
    private void setupVideoPlayerView(){

        if ("".equals(mStep.getVideoURL())){
            // no video to show
            mPlayerView.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params =
                    (ConstraintLayout.LayoutParams) mGuideline.getLayoutParams();

            // shift the step instruction up the screen

            params.guidePercent = HIDE_VIDEO_PLAYER_PERCENT;

        }
        else{
            mPlayerView.setVisibility(View.VISIBLE);

        }
    }

}
