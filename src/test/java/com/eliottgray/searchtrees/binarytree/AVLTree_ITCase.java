package com.eliottgray.searchtrees.binarytree;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class AVLTree_ITCase {

    /**
     * Construct a tree with random keys and values, and periodically validate that the state of the tree is valid.
     */
    @Test
    public void testRandomTree_Integers(){

        AVLTree<Integer> avlTree = new AVLTree<>();

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
                avlTree.insert(randomNum);
                previousEntries.add(randomNum);
            }

            // Validate incrementally.
            boolean meetsValidationCriteria = previousEntries.size() % checkThreshold == 0;
            if (meetsValidationCriteria){
                assertTrue(UnitTestUtilities.validateAVLTree(avlTree));
                assertEquals(previousEntries.size(), avlTree.size());
            }
        }

        // Validate completed tree.
        assertTrue(UnitTestUtilities.validateAVLTree(avlTree));
        assertEquals(previousEntries.size(), avlTree.size());

        // Validate in-order.
        List<Integer> expectedValues = new ArrayList<>(previousEntries);
        Collections.sort(expectedValues);
        List<Integer> actualInOrderValues = avlTree.toAscendingList();
        assertEquals(expectedValues, actualInOrderValues);

        // Validate out-order.
        Collections.reverse(expectedValues);
        List<Integer> actualOutOrderValues = avlTree.toDescendingList();
        assertEquals(expectedValues, actualOutOrderValues);


        // Validate tree after
        assertTrue(UnitTestUtilities.validateAVLTree(avlTree));
        assertEquals(previousEntries.size(), avlTree.size());

        // Delete keys one at a time until tree is empty; validating incrementally.
        int remainingKeys = previousEntries.size();
        for (Integer key : previousEntries){

            // Validate that value is still in tree, before deletion.
            assertTrue(avlTree.contains(key));

            // Delete key from tree AND cache of previous entries.
            avlTree.delete(key);
            remainingKeys -= 1;

            // Validate that value is gone from tree after deletion.
            assertFalse(avlTree.contains(key));

            // Validate incrementally.
            boolean meetsValidationCriteria = remainingKeys % checkThreshold == 0;
            if (meetsValidationCriteria){
                assertTrue(UnitTestUtilities.validateAVLTree(avlTree));
                assertEquals(remainingKeys, avlTree.size());
            }
        }
    }
}
