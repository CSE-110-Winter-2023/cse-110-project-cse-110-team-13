package com.example.socialcompass;

import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(RobolectricTestRunner.class)
public class RoboLocationInputTest {
    /*
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

     */

    @Test
    public void testServerGet()
    {
        String uuid = "16724533";
        ServerAPI api = ServerAPI.provide();
        Friend friend;
        try {
            var future = api.getFriendAsync(uuid);
            friend = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating friend");
        }
        if (friend == null)
            throw new RuntimeException("GET request failed");
        assertEquals(uuid, friend.getUUIDAsString());
        assertEquals("Point Nemo", friend.getLabel());
        assertEquals(-48.876667, friend.getLatitude(), 0.01);
        assertEquals(-123.393333, friend.getLongitude(), 0.01);
        assertFalse(friend.isPublicFriend());
        assertEquals("2023-03-07T01:20:46Z", friend.getCreatedAt());
        assertEquals("2023-03-07T01:20:46Z", friend.getUpdatedAt());
    }

    @Test
    public void testServerPut()
    {
        ServerAPI api = ServerAPI.provide();
        Friend friend = new Friend("1234567890", "Team 13 PUT Friend Test",
            -15, 15, false,
                Instant.now().toString(), Instant.now().toString());
        int httpCode = -1;
        try {
            httpCode = api.putFriendAsync(friend).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error putting friend");
        }
        if (httpCode == -1)
            assertEquals(1, 2);
        assertEquals(200, httpCode);
    }

    @Test
    public void testServerDelete()
    {
        Friend friend = new Friend("1234567890", "Team 13 DELETE Friend Test",
                -15, 15, false,
                Instant.now().toString(), Instant.now().toString());
        ServerAPI api = ServerAPI.provide();
        Future<Integer> put = api.putFriendAsync(friend);
        while(!put.isDone());
        Future<Integer> delete = api.deleteFriendAsync(friend);
        while(!delete.isDone());
        try {
            friend = api.getFriendAsync("1234567890").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating friend");
        }
        if (friend != null)
            System.out.println(friend.toJSON());
        assertNull(friend);
    }

    @Test
    public void testServerPatchLocation()
    {
        Friend friend = new Friend("1234567890", "Team 13 PATCH Friend Test",
                -15, 15, false,
                Instant.now().toString(), Instant.now().toString());
        ServerAPI api = ServerAPI.provide();
        Future<Integer> put = api.putFriendAsync(friend);
        while(!put.isDone());
        Future<Integer> patch = api.patchFriendAsync("1234567890", -20, 20);
        while(!patch.isDone());
        try {
            assertEquals(200, (long) patch.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            friend = api.getFriendAsync("1234567890").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating friend");
        }
        assertEquals(-20, friend.getLatitude(), 0.01);
        assertEquals(20, friend.getLongitude(), 0.01);
    }

    @Test
    public void testServerPatchLabel()
    {
        Friend friend = new Friend("1234567890", "Team 13 PATCH Friend Test",
                -15, 15, false,
                Instant.now().toString(), Instant.now().toString());
        ServerAPI api = ServerAPI.provide();
        Future<Integer> put = api.putFriendAsync(friend);
        while(!put.isDone());
        Future<Integer> patch = api.patchFriendAsync("1234567890",
                    "Team 13 PATCH Friend Test Renamed!");
        while(!patch.isDone());
        try {
            assertEquals(200, (long) patch.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            friend = api.getFriendAsync("1234567890").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating friend");
        }
        assertEquals("Team 13 PATCH Friend Test Renamed!", friend.getLabel());
    }

    @Test
    public void testServerPatchPublicness()
    {
        Friend friend = new Friend("1234567890", "Team 13 PATCH Friend Test",
                -15, 15, false,
                Instant.now().toString(), Instant.now().toString());
        ServerAPI api = ServerAPI.provide();
        Future<Integer> put = api.putFriendAsync(friend);
        while(!put.isDone());
        Future<Integer> patch = api.patchFriendAsync("1234567890",
                true);
        while(!patch.isDone());
        try {
            assertEquals(200, (long) patch.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            friend = api.getFriendAsync("1234567890").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating friend");
        }
        assertTrue(friend.isPublicFriend());
    }
}
