package com.example.socialcompass;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MarkerBuilder {

    Marker currMarker;
    ServerAPI server = ServerAPI.provide();

    public MarkerBuilder(){

    }

    public Marker createMarker(String UID){
        currMarker = new Marker(UID);
        var futureFriend = server.getFriendAsync(UID);
        Friend friend = null;
        try{
            friend  = futureFriend.get();
        }
        catch(Exception e){}

        currMarker.setLabel(friend.getLabel());
        currMarker.setCoordinate( String.valueOf(friend.getLatitude()) + "," + String.valueOf(friend.getLongitude()));

        return currMarker;
    }

    public MarkerBuilder addUIElements(int index, Marker marker, float angle, Activity activity){
        LayoutInflater vi = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.marker, null);

        // fill in any details dynamically here
        TextView textView = (TextView) v.findViewById(R.id.Label);
        textView.setText(marker.getMarkerLabel());

        ImageView imageView = (ImageView) v.findViewById(R.id.Marker);

        imageView.setImageResource(R.drawable.compass_face);

        // insert into main view
        ViewGroup insertPoint = (ViewGroup) activity.findViewById(R.id.compass);
        insertPoint.addView(v, index, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //go through the ViewGroups displayed on the compass activity until get to view-group holding marker and label
        var markerView = ((ViewGroup)((ViewGroup)insertPoint.getChildAt(index)).getChildAt(0));
        ConstraintLayout.LayoutParams imageLayout = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        imageLayout.circleAngle = angle;
        imageLayout.circleRadius = 400;
        imageView.setLayoutParams(imageLayout);
        marker.setLabel ((TextView) markerView.getChildAt(1));
        marker.setLocation ((ImageView) markerView.getChildAt(0));
        return this;
    }



}
