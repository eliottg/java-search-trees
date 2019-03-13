package com.eliottgray.searchtrees.binarytree;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class BSTree_ITCase {

    /**
     * Construct a tree with random keys and values, and periodically validate that the state of the tree is valid.
     */
    @Test
    public void testRandomTree_Integers(){

        BSTree<Integer> bsTree = new BSTree<>();

        // Test size is N; threshold for validating the tree is about 10%;
        int targetSize = 1000000;
        int checkThreshold = targetSize / 10;  // Check 10 times or so every loop.

        // Generate N key-value pairs, and store for later adding.
        Set<Integer> previousEntries = new HashSet<>();
        while (previousEntries.size() < targetSize){
            Integer randomNum = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            boolean alreadyAdded = previousEntries.contains(randomNum);
            if (!alreadyAdded){
                // Add to tree, duplicate cache to validate against.
                bsTree = bsTree.insert(randomNum);
                previousEntries.add(randomNum);
            }

            // Validate incrementally.
            boolean meetsValidationCriteria = previousEntries.size() % checkThreshold == 0;
            if (meetsValidationCriteria){
                assertTrue(UnitTestUtilities.validateBSTree(bsTree));
                assertEquals(previousEntries.size(), bsTree.size());
            }
        }

        // Validate completed tree.
        assertTrue(UnitTestUtilities.validateBSTree(bsTree));
        assertEquals(previousEntries.size(), bsTree.size());

        // Validate in-order.
        List<Integer> expectedValues = new ArrayList<>(previousEntries);
        Collections.sort(expectedValues);
        List<Integer> actualInOrderValues = bsTree.toAscendingList();
        assertEquals(expectedValues, actualInOrderValues);

        // Validate tree after
        assertTrue(UnitTestUtilities.validateBSTree(bsTree));
        assertEquals(previousEntries.size(), bsTree.size());

        // Delete keys one at a time until tree is empty; validating incrementally.
        int remainingKeys = previousEntries.size();
        for (Integer key : previousEntries){

            // Validate that value is still in tree, before deletion.
            assertTrue(bsTree.contains(key));

            // Delete key from tree AND cache of previous entries.
            bsTree = bsTree.delete(key);
            remainingKeys -= 1;

            // Validate that value is gone from tree after deletion.
            assertFalse(bsTree.contains(key));

            // Validate incrementally.
            boolean meetsValidationCriteria = remainingKeys % checkThreshold == 0;
            if (meetsValidationCriteria){
                assertTrue(UnitTestUtilities.validateBSTree(bsTree));
                assertEquals(remainingKeys, bsTree.size());
            }
        }
    }
}
