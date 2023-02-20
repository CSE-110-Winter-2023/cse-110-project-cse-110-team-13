package com.example.socialcompass;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDialog;

import static org.junit.Assert.*;
import android.app.Dialog;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(RobolectricTestRunner.class)
public class CurrentStateUnitTests {
    //should return 90 degrees for a difference of 0 degrees
    /*
    @Test
    public void returnsCorrectAngleForZeroDegrees()
    {
        double out = AngleUtil.compassCalculateAngle("0,0", "1,0");
        assertEquals(90, out, 0.1);
    }

    //should work for an arbitrary input
    @Test
    public void returnsCorrectAngleForArbitraryAngle()
    {
        Random rng = new Random();
        float xdiff = Math.abs(rng.nextFloat() % 90);
        float ydiff = Math.abs(rng.nextFloat() % 180);
        float expected = (float) -(Math.toDegrees(Math.atan(ydiff/xdiff)) - 90);
        double actual = AngleUtil.compassCalculateAngle("0,0",
                xdiff + "," + ydiff);
        assertEquals(expected, actual, 0.1);
    }

    //should invert result for a negative input
    @Test
    public void returnsInvertedAngleOnNegativeInput()
    {
        float xdiff = -1;
        float ydiff = -1;
        float expected = -((float) Math.toDegrees(Math.atan(ydiff/xdiff) + Math.PI) - 90);
        double actual = AngleUtil.compassCalculateAngle("0,0",
                xdiff + "," + ydiff);
        assertEquals(expected, actual, 0.1);
    }
    */
/*

    @Test
    public void currentStateConstructorTest()
    {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);


        scenario.onActivity(activity -> {
            assertEquals(3, activity.currentState.numOfLocations);
            for(int i = 0; i < 3; i++) {
                assertEquals("", activity.currentState.markerList.get(i).getCoordinate());
                assertEquals("", activity.currentState.markerList.get(i).getLabel());
                assertEquals(Integer.valueOf(0), activity.currentState.markerList.get(i).getLabelID());
                assertEquals(Integer.valueOf(0), activity.currentState.markerList.get(i).getLocationID());
            }

        });

    }
*/

}