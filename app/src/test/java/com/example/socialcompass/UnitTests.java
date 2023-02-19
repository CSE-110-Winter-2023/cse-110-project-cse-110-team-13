package com.example.socialcompass;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Random;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {
    //should return 90 degrees for a difference of 0 degrees
    @Test
    public void returnsCorrectAngleForZeroDegrees()
    {
        double out = AngleUtil.compassCalculateAngle("0,0", "1,0");
        assertEquals(90, out, 0.1);
    }

    //should work for an arbitrary input
    @Test
    public void returnsCorrectAngleForArbitraryAngle()
    {
        Random rng = new Random();
        float xdiff = Math.abs(rng.nextFloat() % 90);
        float ydiff = Math.abs(rng.nextFloat() % 180);
        float expected = (float) -(Math.toDegrees(Math.atan(ydiff/xdiff)) - 90);
        double actual = AngleUtil.compassCalculateAngle("0,0",
                xdiff + "," + ydiff);
        assertEquals(expected, actual, 0.1);
    }

    //should invert result for a negative input
    @Test
    public void returnsInvertedAngleOnNegativeInput()
    {
        float xdiff = -1;
        float ydiff = -1;
        float expected = -((float) Math.toDegrees(Math.atan(ydiff/xdiff) + Math.PI) - 90);
        double actual = AngleUtil.compassCalculateAngle("0,0",
                xdiff + "," + ydiff);
        assertEquals(expected, actual, 0.1);
    }
}