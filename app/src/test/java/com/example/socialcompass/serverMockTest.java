package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class serverMockTest {

    @Test
    public void test_default() {

        ServerAPI serverAPI = ServerAPI.provide();
        assertEquals("https://socialcompass.goto.ucsd.edu/location/", serverAPI.getSERVERURL());

    }


    @Test
    public void test_changeUrl() {
        ServerAPI serverAPI = ServerAPI.provide();
        ServerAPI.mockServerUrl("hi");
        assertEquals("hi", serverAPI.getSERVERURL());
    }




}