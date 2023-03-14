package com.example.socialcompass;

import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.socialcompass.Activities.NameInput;
import com.example.socialcompass.Server.Friend;
import com.example.socialcompass.Server.ServerAPI;

import org.junit.Test;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
