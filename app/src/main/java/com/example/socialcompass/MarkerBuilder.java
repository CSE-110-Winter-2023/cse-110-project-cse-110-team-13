package com.example.socialcompass;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        currMarker.setCoordinate( String.valueOf(friend.getLatitude()) + String.valueOf(friend.getLongitude()));

        return currMarker;
    }

    public MarkerBuilder addUIElements(int index, Marker marker, Activity activity){
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

        var markerView = ((ViewGroup)((ViewGroup)insertPoint.getChildAt(index)).getChildAt(0));
        var locationId = markerView.getChildAt(0);
        var labelId = markerView.getChildAt(1);
        ((TextView)markerView.getChildAt(1)).setText(String.valueOf(index));

        marker.setLabel ((TextView) markerView.getChildAt(1));
        marker.setLocation ((ImageView) markerView.getChildAt(0));

        Log.d("test3","insert point: " +String.valueOf(insertPoint));
        Log.d("test3","Child 1 : " +String.valueOf(insertPoint.getChildAt(index)) + " at index " + index);
        Log.d("test3","child 2: " +String.valueOf(((ViewGroup)insertPoint.getChildAt(index)).getChildAt(0)));
        Log.d("test3","child 3: " +String.valueOf(markerView.getChildAt(1)));
        Log.d("test3", "location ID: " + String.valueOf(locationId));

        return this;
    }



}
