package com.example.socialcompass.Utilities;

import android.app.Activity;
import android.app.AlertDialog;

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
}
