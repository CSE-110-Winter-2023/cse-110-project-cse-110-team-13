/**package com.example.socialcompass;

import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import okhttp3.internal.Util;

@RunWith(RobolectricTestRunner.class)
public class TimeServiceTest {

    @Rule
    public ActivityScenarioRule<CompassActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(CompassActivity.class);
    @Test
    public void test_time_service() {
        var mockDataSource = new MutableLiveData<Long>();
        var test = 2222L;
        var timeService = TimeService.singleton();
        timeService.setMockTimeSource(mockDataSource);
        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            mockDataSource.setValue(test);
            var expected = Utilities.formatTime(test);
            TextView textView = activity.findViewById(R.id.timeLastOnline);
            var observed = textView.getText().toString();

            assert observed == expected;

        });
    }
}
*/