package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialcompass.UI.Marker;
import com.example.socialcompass.UI.MarkerBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class LabelTruncationTest {
    // Tests for typeCheck

    MarkerBuilder testBuilder= new MarkerBuilder();;
    @Test
    public void test_tooLong() {
        Marker tester = new Marker();

        testBuilder.addLabel(tester, "jkafhjlsdlfallkfsdajkl");
        assertEquals("jkafhjl...",tester.getMarkerLabel());

        testBuilder.addLabel(tester, "12345678901");
        assertEquals( "1234567...",tester.getMarkerLabel());


    }

    // Tests for bounding coordinates
    @Test
    public void test_empty() {
        Marker tester = new Marker();
        testBuilder.addLabel(tester, "");
        assertEquals( "",tester.getMarkerLabel());
    }

    @Test
    public void test_correctLength() {
        Marker tester = new Marker();
        testBuilder.addLabel(tester, "jk");
        assertEquals("jk",tester.getMarkerLabel());
        testBuilder.addLabel(tester, "jkafhjlsdl");
        assertEquals("jkafhjlsdl",tester.getMarkerLabel());

    }
}
