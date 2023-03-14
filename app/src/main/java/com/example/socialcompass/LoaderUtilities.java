package com.example.socialcompass;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import java.util.Map;

interface Loader {
    static String getCoordinate() {
        return null;
    }

    static String getLabel() {
        return null;
    }

    static void updateLabel() {
        return;
    }

    static void updateCoordinate() {
        return;
    }
}
