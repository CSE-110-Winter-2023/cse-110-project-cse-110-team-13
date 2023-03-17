package com.example.socialcompass;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import static org.junit.Assert.assertEquals;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class GPSSignalTest {

    private Activity activity;
    private LocationService locationService;
    private OrientationService orientationService;
    private TimeService timeService;
    private Device device;
    private MockDeviceObserver obs;

    @Test
    public void testNoSignal(){

        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            locationService = new LocationService(activity, true);
            orientationService = new OrientationService(activity);
            timeService = new TimeService();
            obs = new MockDeviceObserver();
            device = new Device(activity, locationService, orientationService, timeService);
            device.registerDeviceObserver(obs);

            assertEquals(false, obs.isGPSEnabled());
        });

    }

}
