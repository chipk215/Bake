package com.keyeswest.bake.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class StepDetailFragment  extends Fragment {

    private static final String SAVE_STEP_KEY = "saveStepKey";

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
    @BindView(R.id.video_view) SimpleExoPlayerView playerView;

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

        //this is hacky ...revisit
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

        return view;
    }


    //=== Media Player Code Attribution
    //    https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
      //  hideSystemUi();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }



    private void initializePlayer(){
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(mPlayer);

        mPlayer.setPlayWhenReady(mPlayWhenReady);
        mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);

        if (mStep.getVideoURL() != null){
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
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
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
            mPlayer.release();
            mPlayer = null;
        }
    }



}
