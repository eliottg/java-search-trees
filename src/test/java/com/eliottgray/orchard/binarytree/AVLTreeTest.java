package com.eliottgray.orchard.binarytree;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class AVLTreeTest {


    @Test
    public void testConstructor_emptyTree(){
        AVLTree avlTree = new AVLTree();
        assertTrue(avlTree.isEmpty());
        assertFalse(avlTree.contains(3));

        avlTree.insert(3);
        assertFalse(avlTree.isEmpty());
        assertTrue(avlTree.contains(3));

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    @Test
    public void testConstructor_withInitialArray_emptyArray(){
        Integer[] array = new Integer[]{};
        AVLTree avlTree = new AVLTree(array);
        assertTrue(avlTree.isEmpty());
        assertFalse(avlTree.contains(3));

        avlTree.insert(3);
        assertFalse(avlTree.isEmpty());
        assertTrue(avlTree.contains(3));

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    @Test
    public void testConstructor_withInitialArray_singletonArray(){
        Integer[] array = new Integer[]{1};
        AVLTree avlTree = new AVLTree(array);
        assertFalse(avlTree.isEmpty());
        assertTrue(avlTree.contains(1));
        assertFalse(avlTree.contains(3));

        avlTree.insert(3);
        assertFalse(avlTree.isEmpty());
        assertTrue(avlTree.contains(3));

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    /**
     * Add initial data which forces a left rotation.
     *      1
     *       \            2
     *        2    ->    / \
     *         \        1   3
     *          3
     */
    @Test
    public void testConstructor_withInitialArray_orderedArray_leftRotation(){
        Integer[] array = new Integer[]{1, 2, 3};
        AVLTree avlTree = new AVLTree(array);
        assertFalse(avlTree.isEmpty());
        assertTrue(avlTree.contains(1));
        assertTrue(avlTree.contains(2));
        assertTrue(avlTree.contains(3));
        assertFalse(avlTree.contains(4));

        // Verify that tree rotates left to 2 as root.
        assertEquals(2, avlTree.getRoot().getKey());

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    /**
     * Add initial data which forces a right rotation.
     *        3
     *       /             2
     *      2      ->     / \
     *     /             1   3
     *    1
     */
    @Test
    public void testConstructor_withInitialArray_orderedArray_rightRotation(){
        Integer[] array = new Integer[]{3, 2, 1};
        AVLTree avlTree = new AVLTree(array);
        assertFalse(avlTree.isEmpty());
        assertTrue(avlTree.contains(1));
        assertTrue(avlTree.contains(2));
        assertTrue(avlTree.contains(3));
        assertFalse(avlTree.contains(4));

        // Verify that tree rotates right to 2 as root.
        assertEquals(2, avlTree.getRoot().getKey());

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    /**
     * Add initial data which forces a right rotation.
     *
     *                  Delete
     *           [2]             3
     *           / \            / \
     *          1   3     ->   1   4
     *               \
     *                4
     */
    @Test
    public void testDelete(){
        Integer[] array = new Integer[]{3, 2, 1, 4};
        AVLTree avlTree = new AVLTree(array);

        assertTrue(avlTree.contains(4));
        avlTree.delete(4);

        assertFalse(avlTree.contains(4));

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    /**
     * Test size changes as nodes increase and decrease.
     */
    @Test
    public void testGetSize(){
        AVLTree avlTree = new AVLTree();
        assertEquals(0, avlTree.size());

        // Insert new values and test size.
        avlTree.insert(3);
        assertEquals(1, avlTree.size());
        avlTree.insert(4);
        assertEquals(2, avlTree.size());

        // Inserting duplicate value does not increase size.
        avlTree.insert(3);
        assertEquals(2, avlTree.size());

        // Deleting value not in tree does not decrease size.
        avlTree.delete(999);
        assertEquals(2, avlTree.size());

        // Deleting values decreases size back down to zero.
        avlTree.delete(4);
        assertEquals(1, avlTree.size());
        avlTree.delete(3);
        assertEquals(0, avlTree.size());

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    /**
     * Test representation of tree as ascending, ordered list.
     */
    @Test
    public void testToSortedArray(){
        AVLTree avlTree = new AVLTree();

        List<Integer> emptyList = avlTree.toAscendingList();
        assertTrue(emptyList.isEmpty());

        List<Integer> inputValues = new ArrayList<Integer>(){{add(50); add(2); add(10); add(4); add(1);}};
        for (Integer integer : inputValues) {
            avlTree.insert(integer);
        }

        Collections.sort(inputValues);
        List<Integer> sortedList = avlTree.toAscendingList();
        assertEquals(inputValues, sortedList);

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    /**
     * Test representation of tree as a descending, ordered list.
     */
    @Test
    public void testToReversedArray(){
        AVLTree avlTree = new AVLTree();

        List<Integer> emptyList = avlTree.toDescendingList();
        assertTrue(emptyList.isEmpty());

        List<Integer> inputValues = new ArrayList<Integer>(){{add(50); add(2); add(10); add(4); add(1);}};
        for (Integer integer : inputValues) {
            avlTree.insert(integer);
        }

        Collections.sort(inputValues);
        Collections.reverse(inputValues);
        List<Integer> sortedList = avlTree.toDescendingList();
        assertEquals(inputValues, sortedList);

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    /**
     * Construct a tree with random keys, and periodically validate that the state of the tree is valid.
     */
    @Test
    public void testRandomTree(){

        // Collect a number of random keys with which to populate the tree.
        int size = 1000000;
        Set<Integer> previousEntries = new HashSet<>();
        for (int i = 0; i < size; i++){
            int randomNum = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            previousEntries.add(randomNum);
        }

        // Populate a new tree.
        AVLTree AVLTree = new AVLTree();
        for (Integer entry : previousEntries){
            AVLTree.insert(entry);
        }

        // Validate that the expected number of keys are in the tree.
        assertTrue(UnitTestUtilities.validateAVLTree(AVLTree));
        assertEquals(previousEntries.size(), AVLTree.size());

        // Validate that all expected keys are within the tree.
        for (Integer entry : previousEntries){
            assertTrue(AVLTree.contains(entry));
        }

        // Iterate through entries, remove one at a time, and periodically check size and validate tree.
        int checkThreshold = previousEntries.size() / 10;  // Check 10 times or so.
        int counter = previousEntries.size();
        for (Integer entry : previousEntries){
            AVLTree.delete(entry);
            counter -= 1;
            if (counter % checkThreshold == 0){
                assertTrue(UnitTestUtilities.validateAVLTree(AVLTree));
                assertEquals(AVLTree.size(), counter);
            }
            assertFalse(AVLTree.contains(entry));

        }
        assertTrue(AVLTree.isEmpty());
        assertTrue(UnitTestUtilities.validateAVLTree(AVLTree));
    }
}
