package com.keyeswest.bake.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.keyeswest.bake.interfaces.HasUniqueId;

public class Step implements Parcelable,HasUniqueId{

    @SerializedName("id")
    private int mId;

    @SerializedName("shortDescription")
    private String mShortDescription;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("videoURL")
    private String mVideoURL;

    @SerializedName("thumbnailURL")
    private String mThumbnailURL;

    private int mNumberOfStepsInRecipe;

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>(){
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size){
            return new Step[size];
        }
    };

    public Step(){}

    private Step(Parcel in){
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
        mNumberOfStepsInRecipe = in.readInt();
    }

    public int getId ()
    {
        return mId;
    }

    public void setId (int id)
    {
        this.mId = id;
    }

    public String getShortDescription ()
    {
        return mShortDescription;
    }

    public void setShortDescription (String shortDescription)
    {
        this.mShortDescription = shortDescription;
    }

    public String getDescription ()
    {
        return mDescription;
    }

    public void setDescription (String description)
    {
        this.mDescription = description;
    }

    public String getVideoURL ()
    {
        return mVideoURL;
    }

    public void setVideoURL (String videoURL)
    {
        this.mVideoURL = videoURL;
    }

    public String getThumbnailURL ()
    {
        return mThumbnailURL;
    }

    public void setThumbnailURL (String thumbnailURL)
    {
        this.mThumbnailURL = thumbnailURL;
    }

    public int getNumberOfStepsInRecipe() {
        return mNumberOfStepsInRecipe;
    }

    public void setNumberOfStepsInRecipe(int numberOfStepsInRecipe) {
        mNumberOfStepsInRecipe = numberOfStepsInRecipe;
    }

    @Override
    public String toString()
    {
        return "Step [mId = "+ mId +", mShortDescription = "+ mShortDescription
                +", mDescription = "+ mDescription +", mVideoURL = "+ mVideoURL
                +", mThumbnailURL = "+ mThumbnailURL +"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeInt(mId);
       dest.writeString(mShortDescription);
       dest.writeString(mDescription);
       dest.writeString(mVideoURL);
       dest.writeString(mThumbnailURL);
       dest.writeInt(mNumberOfStepsInRecipe);

    }

    @Override
    public String getUniqueId() {
        return Integer.toString(mId);
    }
}
