package com.example.socialcompass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class BDDMarkerDisplayAndUpdate {
    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    //open the app, input one location (point nemo), go to compass activity expecting at least
    // 1 location
    @Test
    public void BDDInputAtLeastOneLocation()
    {
        String uuid = "16724533";
        ServerAPI api = new ServerAPI();

        //

        ActivityScenario<LocationInput> inputScenario = ActivityScenario.launch(LocationInput.class);
        inputScenario.moveToState(Lifecycle.State.CREATED);
        inputScenario.moveToState(Lifecycle.State.STARTED);

        inputScenario.onActivity(activity -> {
            Button submitButton = activity.findViewById(R.id.SubmitButton);
            TextView input = activity.findViewById(R.id.InputUId);
            TextView exit = activity.findViewById(R.id.Exit);
            input.setText("16724533");
            submitButton.performClick();
            // Assume that the user performClick here
            //We're mocking this
//            api.putFriendAsync(new Friend("16724533","Point Nemo", -48.87666f,-123.39333f, true,
//                    "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z"));
        });
        ActivityScenario<CompassActivity> compassScenario = ActivityScenario.launch(CompassActivity.class);
        compassScenario.moveToState(Lifecycle.State.CREATED);
        compassScenario.moveToState(Lifecycle.State.STARTED);

        compassScenario.onActivity(compass -> {
            assertEquals(true, (compass.currentState.markerList.size() >= 1));

            boolean checkFriendExistenceInCompass = false;
            for (int i = 0; i < compass.friends.size(); i++) {
                System.out.println(compass.friends.get(i).getUID());
                if(compass.friends.get(i).getUID().equals("16724533"))
                    checkFriendExistenceInCompass = true;
            }
            assertEquals(true, checkFriendExistenceInCompass);
        });
    }

    //BDD user input one location, open compass activity, will see that marker and its correct
    //orientation
    @Test
    public void BDDCheckOneMarkerOrientation()
    {
        String uuid = "16724533";
        ServerAPI api = new ServerAPI();
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
        //
        assertEquals(uuid, friend.getUUIDAsString());
        assertEquals("Point Nemo", friend.getLabel());
        //

        ActivityScenario<LocationInput> inputScenario = ActivityScenario.launch(LocationInput.class);
        inputScenario.moveToState(Lifecycle.State.CREATED);
        inputScenario.moveToState(Lifecycle.State.STARTED);

        inputScenario.onActivity(activity -> {
            Button submitButton = activity.findViewById(R.id.SubmitButton);
            TextView input = activity.findViewById(R.id.InputUId);
            TextView exit = activity.findViewById(R.id.Exit);
            input.setText("16724533");
            submitButton.performClick();
            // Assume that the user performClick here
            //We're mocking this
//            api.putFriendAsync(new Friend("16724533","Point Nemo", -48.87666f,-123.39333f, true,
//                    "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z"));
        });
        ActivityScenario<CompassActivity> compassScenario = ActivityScenario.launch(CompassActivity.class);
        compassScenario.moveToState(Lifecycle.State.CREATED);
        compassScenario.moveToState(Lifecycle.State.STARTED);

        compassScenario.onActivity(compass -> {
            assertEquals(true, (compass.friends.size() >= 1));
            assertEquals(true, (compass.currentState.markerList.size() >= 1));

            boolean checkFriendExistenceInCompass = false;
            int index = 0;
            for (int i = 0; i < compass.friends.size(); i++) {
                if(compass.friends.get(i).getUID().equals("16724533")) {
                    checkFriendExistenceInCompass = true;
                    index = i;
                }
            }
            assertEquals(true, checkFriendExistenceInCompass);

            ImageView friendMarker = compass.currentState.markerList.get(index).getLocation();
            String friendCoordinate = compass.currentState.markerList.get(index).getCoordinate();

            String userCurrentLocation = compass.currentState.oldLocation;
            float userCurrentOrientation = compass.currentState.oldOrientation;
            float angle = 0;
            try {
                angle = AngleUtil.compassCalculateAngle(userCurrentLocation,
                        friendCoordinate,
                        userCurrentOrientation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) friendMarker.getLayoutParams();

            assertEquals(angle, layoutParams.circleAngle, 2);
            assertEquals(View.VISIBLE, friendMarker.getVisibility());
        });
    }

}
