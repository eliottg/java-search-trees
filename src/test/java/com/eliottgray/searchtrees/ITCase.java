package com.eliottgray.searchtrees;

import com.eliottgray.searchtrees.binarytree.AVLTree;
import com.eliottgray.searchtrees.binarytree.BinarySearchTree;
import com.eliottgray.searchtrees.binarytree.InvalidSearchTreeException;
import com.eliottgray.searchtrees.binarytree.Tree;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class ITCase {

    private static int TARGET_SIZE = 1000000;
    private static Set<Integer> TEST_SET = new LinkedHashSet<>();

    @BeforeClass
    public static void setUpOnce(){
        // Generate N key-value pairs, and store for later adding.
        while (TEST_SET.size() < TARGET_SIZE){
            Integer randomNum = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            TEST_SET.add(randomNum);
        }
    }

    @Test
    public void basicBinarySearchTree() throws InvalidSearchTreeException{
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        testSearchTree(binarySearchTree);
    }

    @Test
    public void avlTree() throws InvalidSearchTreeException{
        AVLTree<Integer> avlTree = new AVLTree<>();
        testSearchTree(avlTree);
    }

    private void testSearchTree(Tree<Integer> treeToTest) throws InvalidSearchTreeException{

        assertTrue(treeToTest.isEmpty());

        int addedIntegers = 0;
        int checkThreshold = TARGET_SIZE / 10;  // Check 10 times or so every loop.

        for (Integer integer : TEST_SET){
            treeToTest = treeToTest.insert(integer);
            addedIntegers += 1;

            // Validate incrementally.
            boolean meetsValidationCriteria = addedIntegers % checkThreshold == 0;
            if (meetsValidationCriteria){
                treeToTest.validate();
                assertEquals(addedIntegers, treeToTest.size());
            }
        }

        // Validate completed tree.
        treeToTest.validate();

        // Validate in-order traversal.
        List<Integer> expectedValues = new ArrayList<>(TEST_SET);
        Collections.sort(expectedValues);
        List<Integer> actualInOrderValues = treeToTest.toAscendingList();
        assertEquals(expectedValues, actualInOrderValues);

        // Validate size.
        assertEquals(TARGET_SIZE, treeToTest.size());

        // Validate Min and Max.
        assertEquals(expectedValues.get(0), treeToTest.getMin());
        assertEquals(expectedValues.get(TARGET_SIZE - 1), treeToTest.getMax());

        // Validate Range getter.
        List<Integer> expectedRangeValues = new ArrayList<Integer>(){{
            add(expectedValues.get(0));
            add(expectedValues.get(1));
            add(expectedValues.get(2));
        }};
        List<Integer> actualRangeValues = treeToTest.getRange(expectedRangeValues.get(0), expectedRangeValues.get(2));
        assertEquals(expectedRangeValues, actualRangeValues);

        // Delete keys one at a time until tree is empty; validating incrementally.
        int remainingKeys = TARGET_SIZE;
        for (Integer key : TEST_SET){

            // Validate that value is still in tree, before deletion.
            assertTrue(treeToTest.contains(key));

            // Delete key from tree AND cache of previous entries.
            treeToTest = treeToTest.delete(key);
            remainingKeys -= 1;

            // Validate that value is gone from tree after deletion.
            assertFalse(treeToTest.contains(key));

            // Validate incrementally.
            boolean meetsValidationCriteria = remainingKeys % checkThreshold == 0;
            if (meetsValidationCriteria){
                treeToTest.validate();
                assertEquals(remainingKeys, treeToTest.size());
            }
        }
    }
}
