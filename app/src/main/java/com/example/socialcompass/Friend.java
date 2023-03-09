package com.example.socialcompass;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.UUID;

//class to store all of the data a Friend has
//(location, creation time, update time, label, public)
public class Friend {
    //all the instance variables you get from a GET request
    @SerializedName("public_code")
    @NonNull
    public String uuid;

    @SerializedName("label")
    @NonNull
    public String label;

    @SerializedName(value = "latitude")
    public float latitude;

    @SerializedName(value = "longitude")
    public float longitude;

    @SerializedName(value = "is_listed_publicly")
    public boolean publicFriend;

    @SerializedName(value = "created_at")
    @NonNull
    public String createdAt;

    @SerializedName(value = "updated_at")
    @NonNull
    public String updatedAt;

    //constructor
    public Friend(String uuid, String label, float latitude, float longitude,
                  boolean publicFriend, String createdAt, String updatedAt)
    {
        this.uuid = uuid;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.publicFriend = publicFriend;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //String to UUID converter
    public UUID getUUID() {
        //get midpoint of UUID string
        final int midpt = uuid.length() / 2;
        //split UUID in half
        String[] uuidParts = {uuid.substring(0, midpt), uuid.substring(midpt)};
        //generate UUID
        return new UUID(Long.parseLong(uuidParts[0]), Long.parseLong(uuidParts[1]));
    }

    //returns UUID
    public String getUUIDAsString()
    {
        return uuid;
    }

    //sets UUID to an input UUID
    //UUID to String converter
    public void setUUID(UUID uuid)
    {
        this.uuid = String.valueOf(uuid.getMostSignificantBits()) + String.valueOf(uuid.getLeastSignificantBits());
    }

    //sets UUID to an input String
    public void setUUID(String uuid)
    {
        this.uuid = uuid;
    }

    //gets friend label
    @NonNull
    public String getLabel()
    {
        return this.label;
    }

    //gets latitude
    public float getLatitude()
    {
        return this.latitude;
    }

    //gets longitude
    public float getLongitude() {
        return this.longitude;
    }

    //gets creation time as String
    @NonNull
    public String getCreatedAt() {
        return createdAt;
    }

    //gets creation time as Instant
    public Instant getCreationTime()
    {
        return Instant.parse(createdAt);
    }

    //gets update time as String
    @NonNull
    public String getUpdatedAt() {
        return createdAt;
    }

    //gets update time as Instant
    public Instant getUpdateTime()
    {
        return Instant.parse(createdAt);
    }


    //gets whether friend shares location publicly or not
    public boolean isPublicFriend() {
        return publicFriend;
    }

    //compares creation time of this with another friend
    public int compareCreatedTimeTo(@NonNull Friend other)
    {
        return Instant.parse(createdAt).compareTo(Instant.parse(other.createdAt));
    }

    //compares update time of this with another friend
    public int compareUpdatedTimeTo(@NonNull Friend other)
    {
        return Instant.parse(updatedAt).compareTo(Instant.parse(other.updatedAt));
    }

    //uses GSON to construct a Friend from JSON
    public static Friend fromJSON(String json) {
        return new Gson().fromJson(json, Friend.class);
    }

    //uses GSON to convert this to a JSON
    public String toJSON() {
        return new Gson().toJson(this);
    }
}
