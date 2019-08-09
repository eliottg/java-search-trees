package com.eliottgray.searchtrees;

import com.eliottgray.searchtrees.BinarySearchTree;
import com.eliottgray.searchtrees.InvalidSearchTreeException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class BinarySearchTreeTest {

    /**
     * Test construction of an Binary Search Tree, adding a key, and testing for presence.
     */
    @Test
    public void testConstor_insert_contains() throws InvalidSearchTreeException {
        // Initial tree is empty.
        BinarySearchTree<Integer> bsTree = new BinarySearchTree<>();
        assertTrue(bsTree.isEmpty());
        assertFalse(bsTree.contains(3));

        // Construct new tree with added value.
        BinarySearchTree<Integer> bsTreeTwo = bsTree.insert(3);

        // Old tree is still empty.
        assertTrue(bsTree.isEmpty());
        assertFalse(bsTree.contains(3));

        // New tree correctly has value.
        assertFalse(bsTreeTwo.isEmpty());
        assertTrue(bsTreeTwo.contains(3));

        bsTreeTwo.validate();
    }

    /**
     * Delete node from the tree.
     *
     *                  Delete
     *            2              2
     *           / \              \
     *         [1]  3              3
     *               \              \
     *                4              4
     */
    @Test
    public void testDelete() throws InvalidSearchTreeException{
        BinarySearchTree<Integer> bsTree = new BinarySearchTree<>();
        bsTree = bsTree.insert(2);
        bsTree = bsTree.insert(1);
        bsTree = bsTree.insert(3);
        bsTree = bsTree.insert(4);
        assertTrue(bsTree.contains(1));
        bsTree = bsTree.delete(1);

        assertFalse(bsTree.contains(1));

        bsTree.validate();
    }

    /**
     * Test size changes as nodes increase and decrease.
     */
    @Test
    public void testGetSize() throws InvalidSearchTreeException{
        BinarySearchTree<Integer> bsTree = new BinarySearchTree<>();
        assertEquals(0, bsTree.size());

        // Insert new values and test size.
        bsTree = bsTree.insert(3);
        assertEquals(1, bsTree.size());
        bsTree = bsTree.insert(4);
        assertEquals(2, bsTree.size());

        // Inserting duplicate value does not increase size.
        bsTree = bsTree.insert(3);
        assertEquals(2, bsTree.size());

        // Deleting value not in tree does not decrease size.
        bsTree = bsTree.delete(999);
        assertEquals(2, bsTree.size());

        // Deleting values decreases size back down to zero.
        bsTree = bsTree.delete(4);
        assertEquals(1, bsTree.size());
        bsTree = bsTree.delete(3);
        assertEquals(0, bsTree.size());

        bsTree.validate();
    }

    /**
     * Test representation of tree as ascending, ordered list.
     */
    @Test
    public void testToSortedArray() throws InvalidSearchTreeException{
        BinarySearchTree<Integer> bsTree = new BinarySearchTree<>();

        List<Integer> emptyList = bsTree.toAscendingList();
        assertTrue(emptyList.isEmpty());

        List<Integer> inputValues = new ArrayList<Integer>(){{add(50); add(2); add(10); add(4); add(1);}};
        for (Integer integer : inputValues) {
            bsTree = bsTree.insert(integer);
        }

        Collections.sort(inputValues);
        List<Integer> sortedList = bsTree.toAscendingList();
        assertEquals(inputValues, sortedList);

        bsTree.validate();
    }

    /**
     * Test retrieval of Values corresponding to an inclusive range of Keys.
     */
    @Test
    public void testGetRange(){
        BinarySearchTree<Integer> bsTree = new BinarySearchTree<>();

        // Test that an empty BSTree returns no values.
        List<Integer> shouldBeEmpty = bsTree.getRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertTrue(shouldBeEmpty.isEmpty());

        // Insert values.
        List<Integer> inputValues = new ArrayList<Integer>(){{add(1); add(2); add(3); add(4); add(5);}};
        for (Integer integer : inputValues) {
            bsTree = bsTree.insert(integer);
        }

        // Test correct retrieval of a specific range.
        List<Integer> expectedRange = new ArrayList<Integer>(){{add(2); add(3); add(4);}};
        Integer start = 2;
        Integer end = 4;
        List<Integer> actualRange = bsTree.getRange(start, end);
        assertEquals(expectedRange, actualRange);
    }

    /**
     * Test retrieval of minimum and maximum values.
     */
    @Test
    public void testGetMin_testGetMax(){
        BinarySearchTree<Integer> bsTree = new BinarySearchTree<>();

        // Empty tree returns null values.
        assertNull(bsTree.getMax());
        assertNull(bsTree.getMin());

        bsTree = bsTree.insert(0);
        bsTree = bsTree.insert(1);
        bsTree = bsTree.insert(2);
        bsTree = bsTree.insert(3);

        assertEquals(Integer.valueOf(0), bsTree.getMin());
        assertEquals(Integer.valueOf(3), bsTree.getMax());
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

        BinarySearchTree<Integer> bsTree = new BinarySearchTree<>(customComparator);

        // Insert values.
        List<Integer> inputValues = new ArrayList<Integer>(){{add(-900); add(-800); add(1); add(2); add(3);}};
        for (Integer integer : inputValues) {
            bsTree = bsTree.insert(integer);
        }

        // Due to custom comparator sorting based on Absolute Value, larger negative values should come after smaller positive values.
        List<Integer> expectedOrder = new ArrayList<Integer>(){{add(1); add(2); add(3); add(-800); add(-900);}};
        List<Integer> actualOrder = bsTree.toAscendingList();

        assertEquals(expectedOrder, actualOrder);
    }
}
