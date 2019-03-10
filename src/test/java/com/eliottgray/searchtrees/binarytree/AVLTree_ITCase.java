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
    public void testRandomTree_IntegersAndStrings(){

        AVLTree<Integer, String> avlTree = new AVLTree<>();

        // Test size is N; threshold for validating the tree is about 10%;
        int targetSize = 1000000;
        int checkThreshold = targetSize / 10;  // Check 10 times or so every loop.

        // Generate N key-value pairs, and store for later validation.
        Map<Integer, String> previousEntries = new HashMap<>();
        while (previousEntries.size() < targetSize){
            Integer randomNum = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            boolean alreadyAdded = previousEntries.containsKey(randomNum);
            if (!alreadyAdded){
                // Add to tree, duplicate cache to validate against.
                String stringValue = randomNum.toString();
                avlTree.insert(randomNum, stringValue);
                previousEntries.put(randomNum, stringValue);
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
        List<String> expectedValues = new ArrayList<>(previousEntries.values());
        Collections.sort(expectedValues, (o1, o2) -> {   // Sort Strings by their Integer representations.
            Integer one = Integer.parseInt(o1);
            Integer two = Integer.parseInt(o2);
            if (one < two){
                return -1;
            } else if (one > two){
                return 1;
            } else {
                return 0;
            }
        });
        List<String> actualInOrderValues = avlTree.toAscendingList();
        assertEquals(expectedValues, actualInOrderValues);

        // Validate out-order.
        Collections.reverse(expectedValues);
        List<String> actualOutOrderValues = avlTree.toDescendingList();
        assertEquals(expectedValues, actualOutOrderValues);

        // Overwrite a random subset of key-value pairs with new values.
        List<Integer> keyArray = new ArrayList<>(previousEntries.keySet());
        int numberToOverwrite = targetSize / 100;  // Overwrite 1%
        for (int i = 0; i < numberToOverwrite; i++){
            // Validate old value in tree.
            Integer keyToOverwrite = keyArray.get(i);
            String oldValue = previousEntries.get(keyToOverwrite);
            String retrievedOldValue =  avlTree.get(keyToOverwrite);
            assertEquals(oldValue, retrievedOldValue);

            // Build new value and populate.
            String newValue = oldValue + "_OVERRIDE.";
            previousEntries.put(keyToOverwrite, newValue);
            avlTree.insert(keyToOverwrite, newValue);

            // Validate new value in tree.
            String retrievedNewValue = avlTree.get(keyToOverwrite);
            assertEquals(newValue, retrievedNewValue);
        }

        // Validate tree after
        assertTrue(UnitTestUtilities.validateAVLTree(avlTree));
        assertEquals(previousEntries.size(), avlTree.size());

        // Delete keys one at a time until tree is empty; validating incrementally.
        int remainingKeys = previousEntries.size();
        for (Integer key : previousEntries.keySet()){

            // Validate that value is still in tree, before deletion.
            String expectedValue = previousEntries.get(key);
            assertEquals(expectedValue, avlTree.get(key));

            // Delete key from tree AND cache of previous entries.
            avlTree.delete(key);
            remainingKeys -= 1;

            // Validate that value is gone from tree after deletion.
            assertNull(avlTree.get(key));

            // Validate incrementally.
            boolean meetsValidationCriteria = remainingKeys % checkThreshold == 0;
            if (meetsValidationCriteria){
                assertTrue(UnitTestUtilities.validateAVLTree(avlTree));
                assertEquals(remainingKeys, avlTree.size());
            }
        }
    }
}
