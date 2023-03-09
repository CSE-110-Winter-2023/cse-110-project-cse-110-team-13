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
        public void test_validUId() {
            assertEquals(false, tester.validUId("933,2032,384"));
            assertEquals(false, tester.validUId("1"));
            assertEquals(false, tester.validUId("#"));
            assertEquals(false, tester.validUId("abc"));
            assertEquals(false, tester.validUId("213"));
            assertEquals(true, tester.validUId("11111111111111111111111111111111"));
            assertEquals(true, tester.validUId("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa12f"));
        }

        // Tests for bounding coordinates
        @Test
        public void test_empty() {
            assertEquals(true, tester.empty(""));
            assertEquals(false, tester.empty(" "));
        }

        @Test
        public void test_inputCheck() {
            assertEquals(false, tester.inputCheck("933,2032,384"));
            assertEquals(false, tester.inputCheck("1"));
            assertEquals(false, tester.inputCheck("#"));
            assertEquals(false, tester.inputCheck("abc"));
            assertEquals(false, tester.inputCheck("213"));
            assertEquals(false, tester.inputCheck(""));
            assertEquals(true, tester.inputCheck("11111111111111111111111111111111"));
            assertEquals(true, tester.inputCheck("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa12f"));
        }
    }
