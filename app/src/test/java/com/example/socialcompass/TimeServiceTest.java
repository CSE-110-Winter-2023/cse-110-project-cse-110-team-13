package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import androidx.lifecycle.MutableLiveData;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class TimeServiceTest {


    @Test
    public void test_time_service() {
        var mockDataSource = new MutableLiveData<Long>();
        var test = 2222L;
        var timeService = TimeService.singleton();
        timeService.setMockTimeSource(mockDataSource);

        mockDataSource.setValue(test);
        var expected = Utilities.formatTime(test);

        assertEquals("2 s", expected);

    }

}
