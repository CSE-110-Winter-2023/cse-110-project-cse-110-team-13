/**
package com.example.socialcompass;

import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
public class NameInputTest
{
    ServerAPI api;
    @Before
    public void init()
    {
        api = ServerAPI.provide();
    }

    @Test
    public void testNameInput()
    {
        String testName = "Team 13 Name Input Test";
        String newUID = NameInput.NameMock(testName);
        Future<Friend> selfFriendFuture = api.getFriendAsync(newUID);
        Friend out = null;
        try {
            out = selfFriendFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            assertEquals(1, 2);
        }
        assert out != null;
        assertEquals(out.getLabel(), testName);
        assertEquals(out.getUUIDAsString(), newUID);
    }

    @Test
    public void testNameEmptyInput()
    {
        String testName = "";
        String newUID = NameInput.NameMock(testName);
        assertEquals(newUID, "EMPTY STRING >:(");
    }
}
*/