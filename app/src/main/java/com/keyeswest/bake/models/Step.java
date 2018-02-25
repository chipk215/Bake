package com.keyeswest.bake.models;


import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("id")
    private String mId;

    @SerializedName("shortDescription")
    private String mShortDescription;

    @SerializedName("Description")
    private String mDescription;

    @SerializedName("videoURL")
    private String mVideoURL;

    @SerializedName("thumbnailURL")
    private String mThumbnailURL;

    public String getId ()
    {
        return mId;
    }

    public void setId (String id)
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

    @Override
    public String toString()
    {
        return "Step [mId = "+ mId +", mShortDescription = "+ mShortDescription +", mDescription = "+ mDescription +", mVideoURL = "+ mVideoURL +", mThumbnailURL = "+ mThumbnailURL +"]";
    }
}
