package com.example.socialcompass.Server;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

//subset of Friend, for JSON conversion purposes
public class PutFriend {
    @SerializedName("private_code")
    @NonNull
    //this is the UUID of the Friend this object is generated from
    public String privateCode;

    @SerializedName("label")
    @NonNull
    //this is the label of the Friend this object is generated from
    public String label;

    @SerializedName(value = "latitude")
    //this is the latitude of the Friend this object is generated from
    public float latitude;

    @SerializedName(value = "longitude")
    //this is the longitude of the Friend this object is generated from
    public float longitude;


    //private constructor
    //(PutFriends should only be generated via CreateNewFromFriend)
    private PutFriend(String privateCode, String label, float latitude, float longitude)
    {
        this.privateCode = privateCode;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    //generates PutFriend from Friend
    public static PutFriend createNewFromFriend(Friend friend)
    {
        PutFriend pf = new PutFriend("", "", 0, 0);
        pf.privateCode = friend.getUUIDAsString();
        pf.label = friend.label;
        pf.latitude = friend.latitude;
        pf.longitude = friend.longitude;
        return pf;
    }

    @NonNull
    //returns the private code of this PutFriend
    public String getPrivateCode()
    {
        return this.privateCode;
    }

    //uses GSON to convert this PutFriend to a JSON
    public String toJSON() {
        return new Gson().toJson(this);
    }
}
