package com.eliottgray.searchtrees.binarytree;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class AVLTreeTest {


    @Test
    public void testConstructor_emptyTree(){
        AVLTree<Integer, String> avlTree = new AVLTree<>();
        assertTrue(avlTree.isEmpty());
        assertNull(avlTree.get(3));

        avlTree.insert(3, "Three");
        assertFalse(avlTree.isEmpty());
        assertEquals("Three", avlTree.get(3));

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
    public void testConstructor_orderedArray_leftRotation(){
        AVLTree<Integer, String> avlTree = new AVLTree<>();
        avlTree.insert(1, "One");
        avlTree.insert(2, "Two");
        avlTree.insert(3, "Three");
        assertFalse(avlTree.isEmpty());
        assertEquals("One", avlTree.get(1));
        assertEquals("Two", avlTree.get(2));
        assertEquals("Three", avlTree.get(3));
        assertNull(avlTree.get(4));

        // Verify that tree rotates left to 2 as root.
        Integer expected = 2;
        assertEquals(expected, avlTree.getRoot().getKey());

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
    public void testConstructor_orderedArray_rightRotation(){
        AVLTree<Integer, String> avlTree = new AVLTree<>();
        avlTree.insert(3, "Three");
        avlTree.insert(2, "Two");
        avlTree.insert(1, "One");
        assertFalse(avlTree.isEmpty());
        assertEquals("One", avlTree.get(1));
        assertEquals("Two", avlTree.get(2));
        assertEquals("Three", avlTree.get(3));
        assertNull(avlTree.get(4));

        // Verify that tree rotates right to 2 as root.
        Integer expected = 2;
        assertEquals(expected, avlTree.getRoot().getKey());

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
        AVLTree<Integer, String> avlTree = new AVLTree<>();
        avlTree.insert(2, "Two");
        avlTree.insert(1, "One");
        avlTree.insert(3, "Three");
        avlTree.insert(4, "Four");
        assertEquals("Four", avlTree.get(4));
        avlTree.delete(4);

        assertNull(avlTree.get(4));

        UnitTestUtilities.validateAVLTree(avlTree);
    }

    /**
     * Test size changes as nodes increase and decrease.
     */
    @Test
    public void testGetSize(){
        AVLTree<Integer, Integer> avlTree = new AVLTree<>();
        assertEquals(0, avlTree.size());

        // Insert new values and test size.
        avlTree.insert(3, null);
        assertEquals(1, avlTree.size());
        avlTree.insert(4, null);
        assertEquals(2, avlTree.size());

        // Inserting duplicate value does not increase size.
        avlTree.insert(3, null);
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
        AVLTree<Integer, Integer> avlTree = new AVLTree<>();

        List<Integer> emptyList = avlTree.toAscendingList();
        assertTrue(emptyList.isEmpty());

        List<Integer> inputValues = new ArrayList<Integer>(){{add(50); add(2); add(10); add(4); add(1);}};
        for (Integer integer : inputValues) {
            avlTree.insert(integer, integer);
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
        AVLTree<Integer, Integer> avlTree = new AVLTree<>();

        List<Integer> emptyList = avlTree.toDescendingList();
        assertTrue(emptyList.isEmpty());

        List<Integer> inputValues = new ArrayList<Integer>(){{add(50); add(2); add(10); add(4); add(1);}};
        for (Integer integer : inputValues) {
            avlTree.insert(integer, integer);
        }

        Collections.sort(inputValues);
        Collections.reverse(inputValues);
        List<Integer> sortedList = avlTree.toDescendingList();
        assertEquals(inputValues, sortedList);

        UnitTestUtilities.validateAVLTree(avlTree);
    }
}
