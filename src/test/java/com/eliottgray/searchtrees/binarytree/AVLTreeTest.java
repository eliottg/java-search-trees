package com.eliottgray.searchtrees.binarytree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class AVLTreeTest {


    @Test
    public void testConstructor_emptyTree(){
        AVLTree<Integer> avlTree = new AVLTree<>();
        assertTrue(avlTree.isEmpty());
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
    public void testConstructor_orderedArray_leftRotation(){
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.insert(1);
        avlTree.insert(2);
        avlTree.insert(3);
        assertFalse(avlTree.isEmpty());
        assertTrue(avlTree.contains(1));
        assertTrue(avlTree.contains(2));
        assertTrue(avlTree.contains(3));
        assertFalse(avlTree.contains(4));

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
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.insert(3);
        avlTree.insert(2);
        avlTree.insert(1);
        assertFalse(avlTree.isEmpty());
        assertTrue(avlTree.contains(1));
        assertTrue(avlTree.contains(2));
        assertTrue(avlTree.contains(3));
        assertFalse(avlTree.contains(4));

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
     * Test representation of tree as a descending, ordered list.
     */
    @Test
    public void testToReversedArray(){
        AVLTree<Integer> avlTree = new AVLTree<>();

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
     * Test retrieval of Values corresponding to an inclusive range of Keys.
     */
    @Test
    public void testGetRange(){
        AVLTree<Integer> avlTree = new AVLTree<>();

        List<Integer> inputValues = new ArrayList<Integer>(){{add(1); add(2); add(3); add(4); add(5);}};
        for (Integer integer : inputValues) {
            avlTree.insert(integer);
        }

        List<Integer> expectedRange = new ArrayList<Integer>(){{add(2); add(3); add(4);}};
        Integer start = 2;
        Integer end = 4;
        List<Integer> actualRange = avlTree.getRange(start, end);
        assertEquals(expectedRange, actualRange);
    }

    /**
     * Test a variety of operations with a different comparable object, and a different comparator.
     */
    @Test
    public void testCustomComparable_withDefaultComparator(){
        AVLTree<CustomComparable> defaultComparatorTree = new AVLTree<>();

        String blue = "BLUE";
        String red = "RED";
        Integer one = 1;
        Integer two = 2;
        CustomComparable blueOne = new CustomComparable(blue, one);
        CustomComparable blueTwo = new CustomComparable(blue, two);
        CustomComparable redOne = new CustomComparable(red, one);
        CustomComparable redTwo = new CustomComparable(red, two);

        defaultComparatorTree.insert(redTwo);
        defaultComparatorTree.insert(redOne);

        assertTrue(defaultComparatorTree.contains(redOne));
        assertTrue(defaultComparatorTree.contains(redTwo));
        assertFalse(defaultComparatorTree.contains(blueOne));
        assertFalse(defaultComparatorTree.contains(blueTwo));
        assertEquals(2, defaultComparatorTree.size());

        defaultComparatorTree.insert(blueTwo);
        defaultComparatorTree.insert(blueOne);

        assertTrue(defaultComparatorTree.contains(redOne));
        assertTrue(defaultComparatorTree.contains(redTwo));
        assertTrue(defaultComparatorTree.contains(blueOne));
        assertTrue(defaultComparatorTree.contains(blueTwo));
        assertEquals(4, defaultComparatorTree.size());

        defaultComparatorTree.delete(redTwo);

        assertTrue(defaultComparatorTree.contains(redOne));
        assertFalse(defaultComparatorTree.contains(redTwo));
        assertTrue(defaultComparatorTree.contains(blueOne));
        assertTrue(defaultComparatorTree.contains(blueTwo));
        assertEquals(3, defaultComparatorTree.size());



    }


    private class CustomComparable implements Comparable<CustomComparable>{

        private String color;
        private Integer number;

        private CustomComparable(String color, Integer number){
            this.color = color;
            this.number = number;
        }

        @Override
        public int compareTo(CustomComparable o) {
            int colorResult = this.color.compareTo(o.color);
            if (colorResult < 0){
                return -1;
            } else if (colorResult > 0){
                return 1;
            } else {
                return this.number.compareTo(o.number);
            }
        }
    }
}
