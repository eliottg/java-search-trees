package com.eliottgray.searchtrees.binarytree;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AVLNodeTest {
    private AVLNode<Integer> root;
    private AVLNode<Integer> zero;
    private AVLNode<Integer> one;
    private AVLNode<Integer> two;
    private AVLNode<Integer> three;
    private AVLNode<Integer> four;
    private AVLNode<Integer> five;
    private AVLNode<Integer> six;
    private Comparator<Integer> comparator;

    /**
     * Set up test fixtures.
     */
    @Before
    public void setUp(){
        // Create test nodes for insertion, deletion, etc.
        zero = new AVLNode<>(0);
        one = new AVLNode<>(1);
        two = new AVLNode<>(2);
        three = new AVLNode<>(3);
        four = new AVLNode<>(4);
        five = new AVLNode<>(5);
        six = new AVLNode<>(6);

        comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer one, Integer two) {
                return one.compareTo(two);
            }
        };
    }

    /**
     * Construct a single node, and test properties.
     */
    @Test
    public void testSingleNode(){
        root = zero;
        assertFalse(root.hasLeft());
        assertFalse(root.hasRight());
        assertNull(root.getLeft());
        assertNull(root.getRight());
        assertEquals(root.getKey(), new Integer(0));
        assertEquals(1, root.getSize());
        assertEquals(1, root.getHeight());
        assertTrue(root.contains(zero.getKey(), comparator));
        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Manually construct a small graph of nodes, and test properties.
     *
     *          3
     *         /
     *       1
     */
    @Test
    public void testInsertions_withoutUnbalancingTree(){
        // Start graph with initial node.
        root = three;

        // Add left to root and test relationships.
        root = root.insert(one, comparator);
        assertEquals(three, root);
        assertTrue(root.hasLeft());
        assertFalse(root.hasRight());
        assertEquals(one, root.getLeft());
        assertFalse(one.hasLeft() && one.hasRight());
        assertEquals(2, root.getSize());
        assertEquals(2, root.getHeight());
        assertTrue(root.contains(one.getKey(), comparator));
        assertTrue(root.contains(three.getKey(), comparator));
        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Test that adding a duplicate node results in replacement of that node with the new one.
     */
    @Test
    public void testDuplicateKey(){

        // Establish initial graph.
        root = five;
        root = root.insert(one, comparator);

        // Add duplicate leaf node.
        AVLNode<Integer> duplicateOne = new AVLNode<>(1);
        root = root.insert(duplicateOne, comparator);

        // Test graph for duplicates.
        assertEquals(five, root);
        assertTrue(five.hasLeft());
        assertFalse(five.hasRight());
        assertEquals(five.getLeft(), duplicateOne);
        assertFalse(duplicateOne.hasLeft() && duplicateOne.hasRight());
        assertEquals(2, root.getSize());
        assertEquals(2, root.getHeight());
        assertTrue(root.contains(one.getKey(), comparator));
        assertTrue(root.contains(five.getKey(), comparator));
        UnitTestUtilities.validateAVLNode(root);

        // Add duplicate root node.
        AVLNode<Integer> duplicateFive = new AVLNode<>(5);
        root = root.insert(duplicateFive, comparator);

        // Test graph for duplicates.
       assertEquals(duplicateFive, root);
       assertTrue(duplicateFive.hasLeft());
       assertFalse(duplicateFive.hasRight());
       assertEquals(duplicateFive.getLeft(), duplicateOne);
       assertFalse(duplicateOne.hasLeft() && duplicateOne.hasRight());
       assertEquals(2, root.getSize());
       assertEquals(2, root.getHeight());
       assertTrue(root.contains(one.getKey(), comparator));
       assertTrue(root.contains(five.getKey(), comparator));
       UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Add multiple nodes to the far right branch, until the tree rotates 3 times.
     *
     *  { Simple Rotation }        { Simple Rotation }              { Complex Rotation }
     *
     *         Left        Insert          Left        Insert         Right        Left
     *    1            2           2               2             2          2             3
     *     \    ->    / \    ->   / \      ->     / \    ->     / \   ->   / \     ->    / \
     *      2        1   3       1   3           1   5         1   5      1   3         2   5
     *       \                        \             / \           / \          \       /   / \
     *        3                        5           3   6         3   6          5     1   4   6
     *                                  \                         \            / \
     *                                   6                         4          4   6
     */
    @Test
    public void testAddValues_repeatedRotations_left(){
        root = one;

        // Unbalance tree with insertions.
        root = root.insert(two, comparator);
        root = root.insert(three, comparator);

        // Verify first rotation
        assertEquals(two, root);
        assertEquals(one, root.getLeft());
        assertEquals(three, root.getRight());
        assertEquals(3, root.getSize());
        assertEquals(2, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(five, comparator);
        root = root.insert(six, comparator);

        // Verify second rotation
        assertEquals(two, root);
        assertEquals(one, root.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(three, five.getLeft());
        assertEquals(six, five.getRight());
        assertEquals(5, root.getSize());
        assertEquals(3, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(four, comparator);

        // Verify complex rotation.
        assertEquals(three, root);
        assertEquals(two, root.getLeft());
        assertEquals(one, two.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(four, five.getLeft());
        assertEquals(six, five.getRight());
        assertEquals(6, root.getSize());
        assertEquals(3, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

    }

    /**
     * Add multiple nodes to the far left branch, until the tree rotates 3 times.
     *
     *  { Simple Rotation }        { Simple Rotation }              { Complex Rotation }
     *
     *         Right       Insert          Right        Insert         Left        Right
     *       6         5            5               5             5          5             4
     *      /   ->    / \    ->    / \      ->     / \    ->     / \   ->   / \     ->    / \
     *     5         4   6        4   6           2   6         2   6      4   6         2   5
     *    /                      /               / \           / \        /             / \   \
     *   4                      2               1   4         1   4      2             1   3   6
     *                         /                                 /      / \
     *                        1                                 3      1   3
     */
    @Test
    public void testAddValues_repeatedRotations_right(){
        root = six;

        // Unbalance tree with insertions.
        root = root.insert(five, comparator);
        root = root.insert(four, comparator);

        // Verify first rotation
        assertEquals(five, root);
        assertEquals(six, root.getRight());
        assertEquals(four, root.getLeft());
        assertEquals(3, root.getSize());
        assertEquals(2, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(two, comparator);
        root = root.insert(one, comparator);

        // Verify second rotation
        assertEquals(five, root);
        assertEquals(two, root.getLeft());
        assertEquals(six, root.getRight());
        assertEquals(one, two.getLeft());
        assertEquals(four, two.getRight());
        assertEquals(5, root.getSize());
        assertEquals(3, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(three, comparator);

        // Verify complex rotation.
        assertEquals(four, root);
        assertEquals(two, root.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(six, five.getRight());
        assertEquals(one, two.getLeft());
        assertEquals(three, two.getRight());
        assertEquals(6, root.getSize());
        assertEquals(3, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

    }

    /**
     * Test deletion of multiple nodes, with varying number of children.
     *
     * Nodes to be deleted in brackets: []
     *
     *              Delete        Delete       Delete     Delete    Delete
     *        4              4            4           [4]      [3]
     *       / \            / \          / \          /
     *      2   5    ->   [2]  5   ->   3  [5]  ->   3     ->       ->   [empty]
     *       \   \          \
     *        3  [6]         3
     */
    @Test
    public void testDelete_zeroOrOneChild(){
        // Construct initial tree.
        root = four;
        root = root.insert(two, comparator);
        root = root.insert(five, comparator);
        root = root.insert(three, comparator);
        root = root.insert(six, comparator);
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of a node with no children.
        root = root.delete(six.getKey(), comparator);
        assertEquals(four, root);
        assertEquals(five, four.getRight());
        assertFalse(five.hasRight());
        assertEquals(1, five.getSize());
        assertEquals(1, five.getHeight());
        assertEquals(3, root.getHeight());
        assertEquals(4, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of a node with one child.
        root = root.delete(2, comparator);
        assertEquals(four, root);
        assertEquals(three, root.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(2, root.getHeight());
        assertEquals(3, root.getSize());
        assertEquals(1, three.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of a node with no children.
        root = root.delete(5, comparator);
        assertEquals(four, root);
        assertNull(root.getRight());
        assertEquals(2, root.getHeight());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of Root with one child.
        root = root.delete(4, comparator);
        assertEquals(three, root);
        assertFalse(root.hasLeft() && root.hasRight());
        assertEquals(1, root.getSize());
        assertEquals(1, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of Root with no child.
        root = root.delete(3, comparator);
        assertNull(root);
    }
//
    /**
     * Test deletion which causes right rotation of tree.
     *
     *             Delete     Rotate
     *        3            3         2
     *       / \          /         / \
     *      2  [4]  ->   2    ->   1   3
     *     /            /
     *    1            1
     */
    @Test
    public void testDelete_rightRotation(){
        root = three;
        root = root.insert(four, comparator);
        root = root.insert(two, comparator);
        root = root.insert(one, comparator);

        assertEquals(three, root);

        // Deletion should cause rotation.
        root = root.delete(four.getKey(), comparator);
        assertEquals(two, root);
        assertEquals(one, root.getLeft());
        assertEquals(three, root.getRight());
        assertEquals(2, root.getHeight());
        assertEquals(3, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

    }

    /**
     * Test deletion which causes left rotation of tree.
     *
     *           Delete      Rotate
     *        3          3            4
     *       / \          \          / \
     *     [2]  4    ->    4   ->   3   5
     *           \          \
     *            5          5
     */
    @Test
    public void testDelete_leftRotation(){
        root = three;
        root = root.insert(four, comparator);
        root = root.insert(two, comparator);
        root = root.insert(five, comparator);

        assertEquals(three, root);

        // Deletion should cause rotation.
        root = root.delete(two.getKey(), comparator);
        assertEquals(four, root);
        assertEquals(three, root.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(2, root.getHeight());
        assertEquals(3, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

    }

    /**
     * Test deletion of nodes with two children
     *
     *              Delete        Delete        Delete
     *        [4]             [3]             5             5
     *       /   \           /   \           / \           / \
     *     1      6    ->   1     6    ->  [1]  6    ->   2   6
     *    / \    /         / \    /        / \           /
     *   0  (3) 5         0   2 (5)       0  (2)        0
     *      /
     *     2
     */
    @Test
    public void testDelete_nodeWithTwoChildren(){
        root = four;
        root = root.insert(one, comparator);
        root = root.insert(six, comparator);
        root = root.insert(three, comparator);
        root = root.insert(zero, comparator);
        root = root.insert(five, comparator);
        root = root.insert(two, comparator);

        assertEquals(four, root);
        assertEquals(4, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of root when LEFT subtree is longer.
        root = root.delete(4, comparator);
        assertEquals(three, root);
        assertEquals(one, three.getLeft());
        assertEquals(six, three.getRight());
        assertEquals(five, six.getLeft());
        assertEquals(zero, one.getLeft());
        assertEquals(two, one.getRight());
        assertEquals(3, three.getHeight());
        assertEquals(6, three.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of root when Right subtree is longer OR subtrees are same size.
        root = root.delete(3, comparator);
        assertEquals(five, root);
        assertEquals(one, root.getLeft());
        assertEquals(six, root.getRight());
        assertEquals(zero, one.getLeft());
        assertEquals(two, one.getRight());
        assertEquals(3, root.getHeight());
        assertEquals(5, three.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of non-root with two children.
        root = root.delete(one.getKey(), comparator);
        assertEquals(five, root);
        assertEquals(two, root.getLeft());
        assertEquals(six, root.getRight());
        assertEquals(zero, two.getLeft());
        assertEquals(3, root.getHeight());
        assertEquals(4, root.getSize());
        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Attempt to delete keys which are not in the tree.
     */
    @Test
    public void testDelete_whenNothingToDelete(){
        root = four;
        root = root.insert(one, comparator);

        // Delete value that isn't in the tree, larger than largest node.
        root = root.delete(99, comparator);

        // Verify tree is unchanged.
        assertEquals(root, four);
        assertEquals(one, root.getLeft());
        assertEquals(2, root.getHeight());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Delete value that isn't in the tree, smaller than smallest node.
        root = root.delete(-199, comparator);

        // Verify tree is unchanged.
        assertEquals(root, four);
        assertEquals(one, root.getLeft());
        assertEquals(2, root.getHeight());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Delete value that isn't in the tree, between the available node values.
        root = root.delete(3, comparator);
        // Verify tree is unchanged.
        // Verify same results as before the deletion.
        assertEquals(root, four);
        assertEquals(one, root.getLeft());
        assertEquals(2, root.getHeight());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Test ability to search tree for specific Key.
     */
    @Test
    public void testContains(){
        root = five;
        root = root.insert(four, comparator);
        root = root.insert(six, comparator);

        // Test if root is found.
        assertTrue(root.contains(five.getKey(), comparator));

        // Test if left leaf is found.
        assertTrue(root.contains(four.getKey(), comparator));

        // Test if right leaf is found.
        assertTrue(root.contains(six.getKey(), comparator));

        // Test if unavailable node is reported as unavailable.
        assertFalse(root.contains(zero.getKey(), comparator));
    }

    /**
     * Test traversal to in-order sorted list.
     */
    @Test
    public void testInOrderTraversal(){
        root = five;
        root = root.insert(six, comparator);
        root = root.insert(zero, comparator);
        root = root.insert(one, comparator);
        root = root.insert(two, comparator);
        root = root.insert(three, comparator);

        // Test in order.
        List<Integer> expectedList = Arrays.asList(zero.getKey(), one.getKey(), two.getKey(), three.getKey(), five.getKey(), six.getKey());
        List<Integer> actualInOrderList = root.inOrderTraversal();
        assertEquals(expectedList, actualInOrderList);

        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Test correct retrieval of a subset of Values based on a range of Keys.
     */
    @Test
    public void testGetRangeList_presentInTree(){
        root = one;

        root = root.insert(two, comparator);
        root = root.insert(three, comparator);
        root = root.insert(four, comparator);
        root = root.insert(five, comparator);

        List<Integer> expectedValues = new ArrayList<Integer>(){{add(two.getKey()); add(three.getKey()); add(four.getKey());}};
        List<Integer> actualValues = root.getRange(two.getKey(), four.getKey(), comparator);
        assertEquals(expectedValues, actualValues);
    }

    /**
     * Test correct retrieval of a subset of Values based on a range of Keys.
     */
    @Test
    public void testGetRangeList_notPresentInTree(){
        root = one;

        List<Integer> expectedValues = new ArrayList<>();
        List<Integer> actualValues = root.getRange(two.getKey(), four.getKey(), comparator);
        assertEquals(expectedValues, actualValues);
    }
}
