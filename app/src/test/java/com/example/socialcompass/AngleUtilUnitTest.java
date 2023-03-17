package com.example.socialcompass;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.checkerframework.checker.units.qual.Angle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

//These tests are for checking orientations (north relative) and distances from angleutil
@RunWith(AndroidJUnit4.class)
public class AngleUtilUnitTest {

    // User somewhere south of HCMC, facing direcly north to HCMC
    @Test
    public void southOfHCMC() throws Exception {
        String HCMC = "10.824159655,106.65386249";
        String userLocation = "9.902758087, 106.65902522";
        assertEquals(0, Utilities.compassCalculateAngle(userLocation, HCMC, 0), 1);
    }

    @Test
    public void southOfHCMCOrientatingEast() throws Exception {
        String HCMC = "10.824159655,106.65386249";
        String userLocation = "9.902758087, 106.65902522";
        assertEquals(-115, Utilities.compassCalculateAngle(userLocation, HCMC, 90), 2);
    }

}
