package com.example.socialcompass;

import android.app.Activity;
import android.app.AlertDialog;

import java.util.Locale;

public class Utilities {
    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("I understand", (dialog, i) -> {dialog.cancel();})
                .setCancelable(true);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static String formatTime(long time) {
       long seconds = (time /1000) % 60;
       long minutes = (time / (1000 * 60)) % 60;
       long hours = (time / (1000 * 60 * 6)) % 24;

        if (hours > 0) {
            return String.format("%d hr", hours);
        }
        if (minutes > 0) {
            return String.format("%d m", minutes);
        }
        if (seconds > 0) {
            return String.format("%d s", seconds);
        }
        return "0";

    }
}
