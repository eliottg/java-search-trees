package com.eliottgray.searchtrees;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public abstract class TreeTestSkeleton {

    private Tree<Integer> testTree;

    /**
     * @return      Instance of Tree subclass to be tested.
     */
    public abstract Tree<Integer> buildEmptyTree(Comparator<Integer> comparator);

    @Before
    public void setUpAbstractTests(){
        testTree = buildEmptyTree(Integer::compareTo);
    }

    /**
     * A newly-constructed Tree must be empty.
     */
    @Test
    public void emptyTree(){
        assertTrue(testTree.isEmpty());
        assertEquals(0, testTree.size());
    }

    /**
     * After inserting a Key, Tree should no longer be empty, and should report that Key is contained.
     */
    @Test
    public void insertKey() throws InvalidSearchTreeException {
        int valueOne = 3;
        int valueTwo = 2;
        int valueNotToTest = 4;

        testTree = testTree.insert(valueOne);
        assertFalse(testTree.isEmpty());
        assertEquals(1, testTree.size());
        assertTrue(testTree.contains(valueOne));
        assertFalse(testTree.contains(valueTwo));
        assertFalse(testTree.contains(valueNotToTest));

        testTree = testTree.insert(valueTwo);
        assertFalse(testTree.isEmpty());
        assertEquals(2, testTree.size());
        assertTrue(testTree.contains(valueOne));
        assertTrue(testTree.contains(valueTwo));
        assertFalse(testTree.contains(valueNotToTest));

        testTree.validate();
    }

    /**
     * Inserting a duplicate key should result in no change to the Tree.
     */
    @Test
    public void insertDuplicateKey() throws InvalidSearchTreeException {
        int valueToTest = 993;

        testTree = testTree.insert(valueToTest);
        assertTrue(testTree.contains(valueToTest));
        assertEquals(1, testTree.size());

        // Add value again.
        testTree = testTree.insert(valueToTest);
        assertTrue(testTree.contains(valueToTest));
        assertEquals(1, testTree.size());

        testTree.validate();
    }

    /**
     * After deleting a Key, Tree should report a lower Size, and should not contain Key.
     */
    @Test
    public void deleteKey() throws InvalidSearchTreeException{
        testTree = testTree.insert(1);
        testTree = testTree.insert(2);

        // Delete a specific node.
        testTree = testTree.delete(1);

        // Tree is missing deleted node.
        assertFalse(testTree.contains(1));
        assertTrue(testTree.contains(2));

        // Delete remaining node.
        testTree = testTree.delete(2);

        // Tree must now be empty.
        assertTrue(testTree.isEmpty());
        assertFalse(testTree.contains(1));
        assertFalse(testTree.contains(2));

        testTree.validate();
    }

    /**
     * Deleting a Key that is not within the tree should accomplish nothing.
     */
    @Test
    public void deleteKeyNotInTree() throws InvalidSearchTreeException{
        testTree = testTree.insert(5);

        // Attempting to delete a key not in the Tree accomplishes nothing,
        testTree = testTree.delete(4);
        assertFalse(testTree.contains(4));
        assertTrue(testTree.contains(5));
        assertEquals(1, testTree.size());
        assertFalse(testTree.isEmpty());

        testTree.validate();
    }

    /**
     * Insertion and deletion of Keys should result in a new Tree.
     */
    @Test
    public void immutability() throws InvalidSearchTreeException{
        int valueToAdd = 45;

        assertFalse(testTree.contains(valueToAdd));
        assertTrue(testTree.isEmpty());
        assertEquals(0, testTree.size());

        // Test Insertion

        Tree<Integer> postInsertTree = testTree.insert(valueToAdd);
        assertNotEquals(postInsertTree, testTree);

        assertTrue(postInsertTree.contains(valueToAdd));
        assertFalse(postInsertTree.isEmpty());
        assertEquals(1, postInsertTree.size());

        // Test Deletion

        Tree<Integer> postDeleteTree = postInsertTree.delete(valueToAdd);
        assertNotEquals(postInsertTree, postDeleteTree);

        assertFalse(postDeleteTree.contains(valueToAdd));
        assertTrue(postDeleteTree.isEmpty());
        assertEquals(0, postDeleteTree.size());

        postInsertTree.validate();
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
     * Test retrieval of Keys corresponding to an inclusive range.
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

    /**
     * Test overriding default comparator with a custom one.
     *
     * The comparator in this example stores and sorts Integers based on Absolute Value, instead of actual value.
     */
    @Test
    public void testCustomComparator(){
        // Create comparator that compares all Integers by their absolute value.
        Comparator<Integer> customComparator = (one, two) -> {
            Integer absoluteValueOne = Math.abs(one);
            Integer absoluteValueTwo = Math.abs(two);
            return absoluteValueOne.compareTo(absoluteValueTwo);
        };

        // Create tree with custom comparator.
        Tree<Integer> testTree = buildEmptyTree(customComparator);

        // Insert values.
        List<Integer> inputValues = new ArrayList<Integer>(){{add(-900); add(-800); add(1); add(2); add(3);}};
        for (Integer integer : inputValues) {
            testTree = testTree.insert(integer);
        }

        // Due to custom comparator sorting based on Absolute Value, larger negative values should come after smaller positive values.
        List<Integer> expectedOrder = new ArrayList<Integer>(){{add(1); add(2); add(3); add(-800); add(-900);}};
        List<Integer> actualOrder = testTree.toAscendingList();

        assertEquals(expectedOrder, actualOrder);
    }
}
