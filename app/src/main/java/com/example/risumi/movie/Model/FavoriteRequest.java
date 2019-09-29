package com.example.risumi.movie.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavoriteRequest implements Parcelable
{

    @SerializedName("media_type")
    @Expose
    public String mediaType;
    @SerializedName("media_id")
    @Expose
    public Integer mediaId;
    @SerializedName("favorite")
    @Expose
    public Boolean favorite;
    public final static Parcelable.Creator<FavoriteRequest> CREATOR = new Creator<FavoriteRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FavoriteRequest createFromParcel(Parcel in) {
            return new FavoriteRequest(in);
        }

        public FavoriteRequest[] newArray(int size) {
            return (new FavoriteRequest[size]);
        }

    }
            ;

    protected FavoriteRequest(Parcel in) {
        this.mediaType = ((String) in.readValue((String.class.getClassLoader())));
        this.mediaId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.favorite = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public FavoriteRequest() {
    }

    /**
     *
     * @param mediaId
     * @param favorite
     * @param mediaType
     */
    public FavoriteRequest(String mediaType, Integer mediaId, Boolean favorite) {
        super();
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.favorite = favorite;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mediaType);
        dest.writeValue(mediaId);
        dest.writeValue(favorite);
    }

    public int describeContents() {
        return 0;
    }

}