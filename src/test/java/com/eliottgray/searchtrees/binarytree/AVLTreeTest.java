package com.eliottgray.searchtrees.binarytree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class AVLTreeTest {


    /**
     * Test construction of an AVL Tree, adding a key, and testing for presence.
     */
    @Test
    public void testConstor_insert_contains() throws InvalidSearchTreeException{
        // Initial tree is empty.
        AVLTree<Integer> avlTree = new AVLTree<>();
        assertTrue(avlTree.isEmpty());
        assertFalse(avlTree.contains(3));

        // Construct new tree with added value.
        AVLTree<Integer> avlTreeTwo = avlTree.insert(3);

        // Old tree is still empty.
        assertTrue(avlTree.isEmpty());
        assertFalse(avlTree.contains(3));

        // New tree correctly has value.
        assertFalse(avlTreeTwo.isEmpty());
        assertTrue(avlTreeTwo.contains(3));

        avlTreeTwo.validate();
    }

    /**
     * Delete node from the tree.
     *
     *                  Delete
     *           [2]             3
     *           / \            / \
     *          1   3     ->   1   4
     *               \
     *                4
     */
    @Test
    public void testDelete() throws InvalidSearchTreeException{
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree = avlTree.insert(2);
        avlTree = avlTree.insert(1);
        avlTree = avlTree.insert(3);
        avlTree = avlTree.insert(4);
        assertTrue(avlTree.contains(4));
        avlTree = avlTree.delete(4);

        assertFalse(avlTree.contains(4));

        avlTree.validate();
    }

    /**
     * Test size changes as nodes increase and decrease.
     */
    @Test
    public void testGetSize() throws InvalidSearchTreeException{
        AVLTree<Integer> avlTree = new AVLTree<>();
        assertEquals(0, avlTree.size());

        // Insert new values and test size.
        avlTree = avlTree.insert(3);
        assertEquals(1, avlTree.size());
        avlTree = avlTree.insert(4);
        assertEquals(2, avlTree.size());

        // Inserting duplicate value does not increase size.
        avlTree = avlTree.insert(3);
        assertEquals(2, avlTree.size());

        // Deleting value not in tree does not decrease size.
        avlTree = avlTree.delete(999);
        assertEquals(2, avlTree.size());

        // Deleting values decreases size back down to zero.
        avlTree = avlTree.delete(4);
        assertEquals(1, avlTree.size());
        avlTree = avlTree.delete(3);
        assertEquals(0, avlTree.size());

        avlTree.validate();
    }

    /**
     * Test representation of tree as ascending, ordered list.
     */
    @Test
    public void testToSortedArray() throws InvalidSearchTreeException{
        AVLTree<Integer> avlTree = new AVLTree<>();

        List<Integer> emptyList = avlTree.toAscendingList();
        assertTrue(emptyList.isEmpty());

        List<Integer> inputValues = new ArrayList<Integer>(){{add(50); add(2); add(10); add(4); add(1);}};
        for (Integer integer : inputValues) {
            avlTree = avlTree.insert(integer);
        }

        Collections.sort(inputValues);
        List<Integer> sortedList = avlTree.toAscendingList();
        assertEquals(inputValues, sortedList);

        avlTree.validate();
    }

    /**
     * Test retrieval of Values corresponding to an inclusive range of Keys.
     */
    @Test
    public void testGetRange(){
        AVLTree<Integer> avlTree = new AVLTree<>();

        // Test that an empty AVLTree returns no values.
        List<Integer> shouldBeEmpty = avlTree.getRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertTrue(shouldBeEmpty.isEmpty());

        // Insert values.
        List<Integer> inputValues = new ArrayList<Integer>(){{add(1); add(2); add(3); add(4); add(5);}};
        for (Integer integer : inputValues) {
            avlTree = avlTree.insert(integer);
        }

        // Test correct retrieval of a specific range.
        List<Integer> expectedRange = new ArrayList<Integer>(){{add(2); add(3); add(4);}};
        Integer start = 2;
        Integer end = 4;
        List<Integer> actualRange = avlTree.getRange(start, end);
        assertEquals(expectedRange, actualRange);
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

        AVLTree<Integer> avlTree = new AVLTree<>(customComparator);

        // Insert values.
        List<Integer> inputValues = new ArrayList<Integer>(){{add(-900); add(-800); add(1); add(2); add(3);}};
        for (Integer integer : inputValues) {
            avlTree = avlTree.insert(integer);
        }

        // Due to custom comparator sorting based on Absolute Value, larger negative values should come after smaller positive values.
        List<Integer> expectedOrder = new ArrayList<Integer>(){{add(1); add(2); add(3); add(-800); add(-900);}};
        List<Integer> actualOrder = avlTree.toAscendingList();

        assertEquals(expectedOrder, actualOrder);
    }
}
