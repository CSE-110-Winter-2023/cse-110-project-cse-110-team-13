package com.example.socialcompass;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.ViewHolder> {

    private List<Marker> markers = Collections.emptyList();

    public void setMarkers(List<Marker> markers) {
        this. markers.clear();
        this. markers = markers;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.marker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkerAdapter.ViewHolder holder, int position) {
        holder.setMarker(markers.get(position));
    }

    @Override
    public int getItemCount() {
        return markers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView labelView;

        private int angle = 0;

        private Marker marker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.Marker);
            this.labelView = itemView.findViewById(R.id.Label);

            Log.d("test1", "creating view holders");

        }

        public Marker getTodoItem() {
            return marker;
        }

        public void setMarker(Marker marker ){
            this.marker = marker;
            this.labelView.setText(marker.getLabel());
            Log.d("test1", "Setting Marker with label:" + String.valueOf(marker.getLabel()));


            //SET MARKER ANGLE HERE
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.circleAngle = (float) angle;
            angle += 90;
            imageView.setLayoutParams(layoutParams);

            //hm dont think this works
            this.marker.setLabelID(R.id.Marker);
            this.marker.setLocationID(R.id.Label);


        }


    }
}
