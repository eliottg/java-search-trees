package com.eliottgray.searchtrees;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public abstract class TreeTestSkeleton {

    private Tree<Integer> testTree = buildEmptyTree();

    /**
     * @return      Instance of Tree subclass to be tested.
     */
    public abstract Tree<Integer> buildEmptyTree();

    @Before
    public void setUpAbstractTests(){
        testTree = buildEmptyTree();
    }

    /**
     * Initial tree is empty.
     */
    @Test
    public void isEmpty(){
        assertTrue(testTree.isEmpty());
    }

    /**
     * Test adding a key, and testing for presence.
     */
    @Test
    public void insert_contains() throws InvalidSearchTreeException {
        int valueToTest = 3;
        int valueNotInTree = 4;

        // Initial tree is empty.
        assertTrue(testTree.isEmpty());
        assertFalse(testTree.contains(valueToTest));
        assertFalse(testTree.contains(valueNotInTree));

        // Construct new tree with added value.
        Tree<Integer> testTreeTwo = testTree.insert(valueToTest);

        // Old tree is still empty.
        assertTrue(testTree.isEmpty());
        assertFalse(testTree.contains(valueToTest));
        assertFalse(testTree.contains(valueNotInTree));

        // New tree correctly has value.
        assertFalse(testTreeTwo.isEmpty());
        assertTrue(testTreeTwo.contains(valueToTest));
        assertFalse(testTreeTwo.contains(valueNotInTree));

        testTreeTwo.validate();
    }


    /**
     * Test inserting a key when that key was already present.
     */
    @Test
    public void insertDuplicateKey() throws InvalidSearchTreeException {
        int valueToTest = 993;

        // Add value to tree and test properties.
        testTree = testTree.insert(valueToTest);
        assertTrue(testTree.contains(valueToTest));

        // Add value again.
        testTree = testTree.insert(valueToTest);
        assertTrue(testTree.contains(valueToTest));

        testTree.validate();
    }

    /**
     * Delete node from the tree.
     */
    @Test
    public void delete_contains() throws InvalidSearchTreeException{
        testTree = testTree.insert(2);
        testTree = testTree.insert(1);
        testTree = testTree.insert(3);
        testTree = testTree.insert(4);
        assertTrue(testTree.contains(4));

        // Test tree contains all expected data.
        assertTrue(testTree.contains(1));
        assertTrue(testTree.contains(2));
        assertTrue(testTree.contains(3));
        assertTrue(testTree.contains(4));

        Tree<Integer> testTreeTwo = testTree.delete(4);

        // Old tree still contains all original data.
        assertTrue(testTree.contains(1));
        assertTrue(testTree.contains(2));
        assertTrue(testTree.contains(3));
        assertTrue(testTree.contains(4));

        // New tree is missing deleted node.
        assertTrue(testTreeTwo.contains(1));
        assertTrue(testTreeTwo.contains(2));
        assertTrue(testTreeTwo.contains(3));
        assertFalse(testTreeTwo.contains(4));

        testTree.validate();
    }


    /**
     * Test size changes as nodes increase and decrease.
     */
    @Test
    public void testGetSize() throws InvalidSearchTreeException{
        assertEquals(0, testTree.size());

        // Insert new values and test size.
        Tree<Integer> treeTwo = testTree.insert(3);
        assertEquals(0, testTree.size());
        assertEquals(1, treeTwo.size());
        Tree<Integer> treeThree = treeTwo.insert(4);
        assertEquals(0, testTree.size());
        assertEquals(1, treeTwo.size());
        assertEquals(2, treeThree.size());

        // Inserting duplicate value does not increase size.
        treeThree = treeThree.insert(3);
        assertEquals(2, treeThree.size());

        // Deleting value not in tree does not decrease size.
        treeThree = treeThree.delete(999);
        assertEquals(2, treeThree.size());

        // Deleting values decreases size back down to zero.
        Tree<Integer> treeFour = treeThree.delete(4);
        assertEquals(2, treeThree.size());
        assertEquals(1, treeFour.size());

        Tree<Integer> treeFive = treeFour.delete(3);
        assertEquals(0, treeFive.size());

        treeFive.validate();
    }

    /**
     * Test representation of tree as ascending, ordered list.
     */
    @Test
    public void testToSortedArray() throws InvalidSearchTreeException{
        List<Integer> emptyAscendingList = testTree.toAscendingList();
        assertEquals(new ArrayList<>(), emptyAscendingList);

        List<Integer> inputValues = new ArrayList<Integer>(){{add(50); add(2); add(10); add(4); add(1);}};
        for (Integer integer : inputValues) {
            testTree = testTree.insert(integer);
        }

        Collections.sort(inputValues);
        List<Integer> sortedList = testTree.toAscendingList();
        assertEquals(inputValues, sortedList);

        testTree.validate();
    }

    /**
     * Test retrieval of Values corresponding to an inclusive range of Keys.
     */
    @Test
    public void testGetRange(){
        // Test that an empty tree returns no values.
        List<Integer> shouldBeEmpty = testTree.getRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertTrue(shouldBeEmpty.isEmpty());

        // Insert values.
        List<Integer> inputValues = new ArrayList<Integer>(){{add(1); add(2); add(3); add(4); add(5);}};
        for (Integer integer : inputValues) {
            testTree = testTree.insert(integer);
        }

        // Test correct retrieval of a specific range.
        List<Integer> expectedRange = new ArrayList<Integer>(){{add(2); add(3); add(4);}};
        Integer start = 2;
        Integer end = 4;
        List<Integer> actualRange = testTree.getRange(start, end);
        assertEquals(expectedRange, actualRange);
    }

    /**
     * Test retrieval of minimum and maximum values.
     */
    @Test
    public void testGetMin_testGetMax(){
        // Empty tree returns null values.
        assertNull(testTree.getMax());
        assertNull(testTree.getMin());

        testTree = testTree.insert(1);
        testTree = testTree.insert(0);
        testTree = testTree.insert(2);
        testTree = testTree.insert(3);

        assertEquals(Integer.valueOf(0), testTree.getMin());
        assertEquals(Integer.valueOf(3), testTree.getMax());
    }

}
