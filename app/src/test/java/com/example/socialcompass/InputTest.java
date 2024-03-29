  /**  package com.example.socialcompass;

    import static org.junit.Assert.assertEquals;

    import androidx.test.ext.junit.runners.AndroidJUnit4;

    import org.junit.Test;
    import org.junit.runner.RunWith;


    @RunWith(AndroidJUnit4.class)
    public class InputTest {
        // Tests for typeCheck
        LocationInput tester = new LocationInput();
        @Test
        public void test_validUId() {
            assertEquals(false, tester.validUId("933,2032,384"));
            assertEquals(false, tester.validUId("1ljksfdalkdjslkafjldsjaklfjklsdjfklajklkl"));
            assertEquals(true, tester.validUId("11111111111111111111111111111111"));
            assertEquals(true, tester.validUId("qwerty"));
        }

        // Tests for bounding coordinates
        @Test
        public void test_empty() {
            assertEquals(true, tester.empty(""));
            assertEquals(false, tester.empty(" "));
        }




    }
   */
