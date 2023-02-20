package com.example.socialcompass;

import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Dialog;
import android.location.Location;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowLooper;

@RunWith(RobolectricTestRunner.class)
public class RoboLocationInputTest {
    //test all empty fields
    @Test
    public void testEmptyInput() {
        ActivityScenario<LocationInput> scenario = ActivityScenario.launch(LocationInput.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            Button submitButton = activity.findViewById(R.id.SubmitButton);

            submitButton.performClick();
            assertEquals(1, ShadowDialog.getShownDialogs().size());
            Dialog dialog = ShadowAlertDialog.getLatestDialog();
            TextView rootView = dialog.findViewById(android.R.id.message);
            assertEquals("Please input at least one location!", rootView.getText().toString());

        });

    }
    //testing label without coordinates
    @Test
    public void testLabelWithEmptyCoordinate() {
        ActivityScenario<LocationInput> scenario = ActivityScenario.launch(LocationInput.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            Button submitButton = activity.findViewById(R.id.SubmitButton);
            TextView myHomeLocation = activity.findViewById(R.id.MyLocation);
            TextView myHomeLabel = activity.findViewById(R.id.MyLabel);
            TextView familyLocation = activity.findViewById(R.id.FamilyLocation);
            TextView familyLabel = activity.findViewById(R.id.FamilyLabel);

            myHomeLabel.setText("Hello");
            submitButton.performClick();
            ShadowLooper.runUiThreadTasks();
            Dialog dialog = ShadowAlertDialog.getLatestDialog();
            TextView rootView = dialog.findViewById(android.R.id.message);
            assertEquals("Please input at least one location!", rootView.getText().toString());

            dialog.findViewById(android.R.id.button2).performClick();
            ShadowLooper.runUiThreadTasks();
            assertFalse(dialog.isShowing());
            myHomeLocation.setText("12.43,12.43");
            familyLabel.setText("Mom and Dad");

            submitButton.performClick();
            ShadowLooper.runUiThreadTasks();
            Dialog newDialog = ShadowAlertDialog.getLatestDialog();
            //assertTrue(newDialog.isShowing());
            TextView newRootView = newDialog.findViewById(android.R.id.message);

            assertEquals("One of your label doesn't have a location assigned to it!", newRootView.getText().toString());

        });
    }
    //not floats test
    @Test
    public void testInvalidInput() {
        ActivityScenario<LocationInput> scenario = ActivityScenario.launch(LocationInput.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            Button submitButton = activity.findViewById(R.id.SubmitButton);
            TextView myHomeLocation = activity.findViewById(R.id.MyLocation);
            TextView myHomeLabel = activity.findViewById(R.id.MyLabel);
            TextView familyLocation = activity.findViewById(R.id.FamilyLocation);
            TextView familyLabel = activity.findViewById(R.id.FamilyLabel);

            myHomeLocation.setText("12.34,43.54a");
            submitButton.performClick();
            ShadowLooper.runUiThreadTasks();
            Dialog dialog = ShadowAlertDialog.getLatestDialog();
            TextView rootView = dialog.findViewById(android.R.id.message);

            assertEquals("One or more of your coordinates input are invalid", rootView.getText().toString());
        });
    }
    //too many arguments test
    @Test
    public void testInvalidInput2() {
        ActivityScenario<LocationInput> scenario = ActivityScenario.launch(LocationInput.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            Button submitButton = activity.findViewById(R.id.SubmitButton);
            TextView myHomeLocation = activity.findViewById(R.id.MyLocation);
            TextView myHomeLabel = activity.findViewById(R.id.MyLabel);
            TextView familyLocation = activity.findViewById(R.id.FamilyLocation);
            TextView familyLabel = activity.findViewById(R.id.FamilyLabel);

            myHomeLocation.setText("12.34,43.54,89.34");
            submitButton.performClick();
            ShadowLooper.runUiThreadTasks();
            Dialog dialog = ShadowAlertDialog.getLatestDialog();
            TextView rootView = dialog.findViewById(android.R.id.message);

            assertEquals("One or more of your coordinates input are invalid", rootView.getText().toString());
        });
    }
    //test invalid coordinates
    @Test
    public void testInvalidInput3() {
        ActivityScenario<LocationInput> scenario = ActivityScenario.launch(LocationInput.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            Button submitButton = activity.findViewById(R.id.SubmitButton);
            TextView myHomeLocation = activity.findViewById(R.id.MyLocation);
            TextView myHomeLabel = activity.findViewById(R.id.MyLabel);
            TextView familyLocation = activity.findViewById(R.id.FamilyLocation);
            TextView familyLabel = activity.findViewById(R.id.FamilyLabel);

            myHomeLocation.setText("12.34,189.32");
            submitButton.performClick();
            ShadowLooper.runUiThreadTasks();
            Dialog dialog = ShadowAlertDialog.getLatestDialog();
            TextView rootView = dialog.findViewById(android.R.id.message);

            assertEquals("One or more of your coordinates input are invalid", rootView.getText().toString());
        });
    }

}
