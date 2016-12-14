package tests;

import shadowverse.SVCardMath;

import junit.framework.TestCase;

public class SVCardMathTest extends TestCase {
    
    public void testGetDuplicatesFromDB() throws Exception {
        // todo - test the getDuplicatesFromDB method
        // for this one, you'll need a mock database; insert known data,
        // run the query, and
        // assert that the number of duplicates found is correct.

    }

    public void testVialsInStandard() throws Exception {

        int testDuplicates = 2;     //what would be reasonable data?
        int testBronzeDups = 1;
        int testSilverDups = 1;
        int testGoldDups = 1;
        double vialsCalculated = SVCardMath.vialsInStandard(testDuplicates, testGoldDups, testSilverDups, testBronzeDups);
        double vialsExpected = 1234.67;  // do the actual math and figure out what is expected

        double delta = 0.001; // double values are never absolutely exactly the same. How close do they need to be to be considered the same?
        assertEquals(vialsExpected, vialsCalculated, delta);

        //todo Repeat with a reasonable selection of input duplicate values

        //todo test with invalid data and assert that the program behaves correctly e.g. throws correct error

        //You can write several test methods to test one method in your code - break this down if needed.
    }

    public void testVialsInDarkness() throws Exception {

        //todo write tests for this method
    }
}