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
    public void testConstor_insert_contains(){
        AVLTree<Integer> avlTree = new AVLTree<>();
        assertTrue(avlTree.isEmpty());
        assertFalse(avlTree.contains(3));

        avlTree.insert(3);
        assertFalse(avlTree.isEmpty());
        assertTrue(avlTree.contains(3));

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
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.insert(2);
        avlTree.insert(1);
        avlTree.insert(3);
        avlTree.insert(4);
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
        AVLTree<Integer> avlTree = new AVLTree<>();
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
        AVLTree<Integer> avlTree = new AVLTree<>();

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
            avlTree.insert(integer);
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
        Comparator<Integer> customComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer one, Integer two) {
                Integer absoluteValueOne = Math.abs(one);
                Integer absoluteValueTwo = Math.abs(two);
                return absoluteValueOne.compareTo(absoluteValueTwo);
            }
        };

        AVLTree<Integer> avlTree = new AVLTree<>(customComparator);

        // Insert values.
        List<Integer> inputValues = new ArrayList<Integer>(){{add(-900); add(-800); add(1); add(2); add(3);}};
        for (Integer integer : inputValues) {
            avlTree.insert(integer);
        }

        // Due to custom comparator sorting based on Absolute Value, larger negative values should come after smaller positive values.
        List<Integer> expectedOrder = new ArrayList<Integer>(){{add(1); add(2); add(3); add(-800); add(-900);}};
        List<Integer> actualOrder = avlTree.toAscendingList();

        assertEquals(expectedOrder, actualOrder);
    }
}
