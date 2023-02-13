    package com.example.socialcompass;

    import static org.junit.Assert.assertEquals;

    import androidx.test.ext.junit.runners.AndroidJUnit4;
    import org.junit.Assert;
    import org.junit.Test;
    import org.junit.runner.RunWith;

    @RunWith(AndroidJUnit4.class)
    public class LocationInputTest {
        // Tests for typeCheck
        LocationInput tester = new LocationInput();
        @Test
        public void test_typeCheck() {
            assertEquals(false, tester.typeCheck("933,2032,384"));
            assertEquals(false, tester.typeCheck(""));
            assertEquals(false, tester.typeCheck("123$,56&"));
            assertEquals(false, tester.typeCheck("home, house"));
            assertEquals(false, tester.typeCheck(" , "));
            assertEquals(false, tester.typeCheck("15.3.2, 13.2.3.4"));
            assertEquals(true, tester.typeCheck("13.456, 12.435"));
            assertEquals(true, tester.typeCheck("45, 50"));
        }
        // Tests for bounding coordinates
        @Test
        public void test_validCoordinateCheck() {
            assertEquals(false, tester.validCoordinateCheck((float) 40, (float) -180.0));
            assertEquals(false, tester.validCoordinateCheck((float) -90.0, (float) 40));
            assertEquals(false, tester.validCoordinateCheck((float) 0, (float) 180.000000));
            assertEquals(false, tester.validCoordinateCheck((float) 90.0000, (float) 40));
            assertEquals(true, tester.validCoordinateCheck((float)89.99999, (float)179.999));
        }
    }
