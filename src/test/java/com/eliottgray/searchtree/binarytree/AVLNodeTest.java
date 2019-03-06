package com.eliottgray.searchtree.binarytree;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class AVLNodeTest {
    private AVLNode root;
    private AVLNode zero;
    private AVLNode one;
    private AVLNode two;
    private AVLNode three;
    private AVLNode four;
    private AVLNode five;
    private AVLNode six;

    /**
     * Set up test fixtures.
     */
    @Before
    public void setUp(){
        // Create test nodes for insertion, deletion, etc.
        zero = new AVLNode(0);
        one = new AVLNode(1);
        two = new AVLNode(2);
        three = new AVLNode(3);
        four = new AVLNode(4);
        five = new AVLNode(5);
        six = new AVLNode(6);
    }

    /**
     * Construct a single node, and test properties.
     */
    @Test
    public void testSingleNode(){
        int value = 400;
        root = new AVLNode(value);
        assertFalse(root.hasLeft());
        assertFalse(root.hasRight());
        assertFalse(root.hasParent());
        assertEquals(value, root.getKey());
        assertEquals(1, root.getSize());
        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Manually construct a small graph of nodes, and test that Height and BalanceFactor properties are maintained.
     *
     *          3
     *         / \
     *       1    4
     *             \
     *              5
     */
    @Test
    public void testInsertions_withoutUnbalancingTree(){
        // Start graph with initial node.
        root = three;

        // Add left to root and test relationships.
        root = root.insert(one);
        assertEquals(three, root);
        assertEquals(one, root.getLeft());
        assertEquals(three, one.getParent());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Add right to root and test properties.
        root = root.insert(four);
        assertEquals(three, root);
        assertEquals(one, root.getLeft());
        assertEquals(four, root.getRight());
        assertEquals(three, one.getParent());
        assertEquals(three, four.getParent());
        assertEquals(3, root.getSize());
        UnitTestUtilities.validateAVLNode(root);


        // Add right child to right child of root.
        root = root.insert(five);
        assertEquals(one, root.getLeft());
        assertEquals(four, root.getRight());
        assertEquals(five, four.getRight());
        assertEquals(three, one.getParent());
        assertEquals(three, four.getParent());
        assertEquals(four, five.getParent());
        assertEquals(4, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

    }

    /**
     * Test behavior with duplicate key added.
     */
    @Test
    public void testDuplicateKey(){
        /// @todo When Value object is provided for a Key (instead of key = value) test replacing Value when Key is duplicated.

        // Establish initial graph.
        root = five;
        root = root.insert(one);
        assertEquals(2, root.getSize());

        // Add duplicate nodes.
        AVLNode duplicateOne = new AVLNode(1);
        root = root.insert(duplicateOne);
        AVLNode duplicateFive = new AVLNode(5);
        root = root.insert(duplicateFive);

        // Test graph for duplicates.
        assertEquals(five, root);
        assertTrue(five.hasLeft());
        assertFalse(five.hasRight());
        assertEquals(five.getLeft(), one);
        assertFalse(five.hasParent());
        assertTrue(one.hasParent());
        assertEquals(one.getParent(), five);
        assertFalse(one.hasLeft());
        assertFalse(one.hasRight());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Test behavior when insert() method is called on a non-root com.eliottgray.orchard.binarytree.AVLNode.
     */
    @Test(expected=IllegalStateException.class)
    public void testInvalidInsert_nonRootNode(){
        // Establish initial graph.
        root = new AVLNode(5);    // Root.
        AVLNode one = new AVLNode(1);     // Not root.
        root.insert(one);

        // Insert new node at non-root node.
        AVLNode ten = new AVLNode(10);
        one.insert(ten);  // This should cause exception.
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
        root = root.insert(two);
        root = root.insert(three);

        // Verify first rotation
        assertEquals(two, root);
        assertEquals(one, root.getLeft());
        assertEquals(three, root.getRight());
        assertEquals(3, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(five);
        root = root.insert(six);

        // Verify second rotation
        assertEquals(two, root);
        assertEquals(one, root.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(three, five.getLeft());
        assertEquals(six, five.getRight());
        assertEquals(5, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(four);

        // Verify complex rotation.
        assertEquals(three, root);
        assertEquals(two, root.getLeft());
        assertEquals(one, two.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(four, five.getLeft());
        assertEquals(six, five.getRight());
        assertEquals(6, root.getSize());
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
        root = root.insert(five);
        root = root.insert(four);

        // Verify first rotation
        assertEquals(five, root);
        assertEquals(six, root.getRight());
        assertEquals(four, root.getLeft());
        assertEquals(3, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(two);
        root = root.insert(one);

        // Verify second rotation
        assertEquals(five, root);
        assertEquals(two, root.getLeft());
        assertEquals(six, root.getRight());
        assertEquals(one, two.getLeft());
        assertEquals(four, two.getRight());
        assertEquals(5, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(three);

        // Verify complex rotation.
        assertEquals(four, root);
        assertEquals(two, root.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(six, five.getRight());
        assertEquals(one, two.getLeft());
        assertEquals(three, two.getRight());
        assertEquals(6, root.getSize());
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
     *    [2]   5    ->   [2]  5   ->   3  [5]  ->   3     ->       ->   [empty]
     *       \   \          \
     *        3  [6]         3
     */
    @Test
    public void testDelete_oneOrTwoChildren(){
        root = four;
        root = root.insert(two);
        root = root.insert(five);
        root = root.insert(three);
        root = root.insert(six);
        assertEquals(5, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of a node with no children.
        root = root.delete(6);
        assertNull(five.getRight());
        assertEquals(0, five.getHeight());
        assertEquals(four, root);
        assertEquals(2, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of a node with one child.
        root = root.delete(2);
        assertEquals(four, root);
        assertEquals(three, root.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(1, root.getHeight());
        assertEquals(0, three.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of a node with no children.
        root = root.delete(5);
        assertEquals(four, root);
        assertNull(root.getRight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of Root with one child.
        root = root.delete(4);
        assertEquals(three, root);
        assertNull(root.getLeft());
        assertNull(root.getRight());
        assertEquals(0, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of Root with no child.
        root = root.delete(3);
        assertNull(root);
    }

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
        root = root.insert(four);
        root = root.insert(two);
        root = root.insert(one);

        assertEquals(three, root);
        assertEquals(2, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Deletion should cause rotation.
        root = root.delete(four.getKey());
        assertEquals(two, root);
        assertEquals(one, root.getLeft());
        assertEquals(three, root.getRight());
        assertEquals(1, root.getHeight());
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
        root = root.insert(four);
        root = root.insert(two);
        root = root.insert(five);

        assertEquals(three, root);
        assertEquals(2, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Deletion should cause rotation.
        root = root.delete(two.getKey());
        assertEquals(four, root);
        assertEquals(three, root.getLeft());
        assertEquals(five, root.getRight());
        assertEquals(1, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

    }

    /// @todo Test deletion of node with two children NOT at root.
    /**
     * Test deletion of root node, with two children.
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
    public void testDelete_rootWithTwoChildren(){
        root = four;
        root = root.insert(one);
        root = root.insert(six);
        root = root.insert(three);
        root = root.insert(zero);
        root = root.insert(five);
        root = root.insert(two);


        assertEquals(four, root);
        assertEquals(3, root.getHeight());
        assertEquals(-1, root.getBalanceFactor());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of root when LEFT subtree is longer.
        root = root.delete(4);
        assertEquals(three, root);
        assertEquals(one, three.getLeft());
        assertEquals(six, three.getRight());
        assertEquals(2, three.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of root when Right subtree is longer OR subtrees are same size.
        root = root.delete(3);
        assertEquals(five, root);
        assertEquals(one, root.getLeft());
        assertEquals(six, root.getRight());
        assertEquals(2, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of non-root.
        root = root.delete(one.getKey());
        assertEquals(five, root);
        assertEquals(two, root.getLeft());
        assertEquals(six, root.getRight());
        assertEquals(zero, two.getLeft());
        assertEquals(2, root.getHeight());
        assertEquals(1, two.getHeight());
        UnitTestUtilities.validateAVLNode(root);
    }


    /**
     * Attempt to delete keys which are not in the tree.
     */
    @Test
    public void testDelete_whenNothingToDelete(){
        root = four;
        root = root.insert(one);

        // Verify state of tree.
        assertEquals(root, four);
        assertEquals(one, root.getLeft());
        assertNull(root.getRight());
        UnitTestUtilities.validateAVLNode(root);

        // Delete value that isn't in the tree, larger than largest node.
        root = root.delete(99);

        // Verify same results as before the deletion.
        assertEquals(root, four);
        assertEquals(one, root.getLeft());
        assertNull(root.getRight());
        UnitTestUtilities.validateAVLNode(root);

        // Delete value that isn't in the tree, smaller than smallest node.
        root = root.delete(-199);

        // Verify same results as before the deletion.
        assertEquals(root, four);
        assertEquals(one, root.getLeft());
        assertNull(root.getRight());
        UnitTestUtilities.validateAVLNode(root);

        // Delete value that isn't in the tree, between the available node values.
        root = root.delete(3);

        // Verify same results as before the deletion.
        assertEquals(root, four);
        assertEquals(one, root.getLeft());
        assertNull(root.getRight());
        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Test behavior when a delete is attempted at any node other than the root.
     */
    @Test(expected=IllegalStateException.class)
    public void testDelete_notAtRoot(){
        root = five;
        root = root.insert(four);
        four.delete(four.getKey());   // Should cause exception, due to being called on non-root.
    }

    /**
     * Test ability to search tree for specific Key.
     */
    @Test
    public void testContains(){
        root = five;
        root = root.insert(four);
        root = root.insert(six);

        // Test if root is found.
        assertTrue(root.contains(five.getKey()));

        // Test if left leaf is found.
        assertTrue(root.contains(four.getKey()));

        // Test if right leaf is found.
        assertTrue(root.contains(six.getKey()));

        // Test if unavailable node is reported as unavailable.
        assertFalse(root.contains(zero.getKey()));
    }

    /**
     * Test behavior when Contains is called on a non-root node.
     */
    @Test(expected=IllegalStateException.class)
    public void testContains_notAtRoot(){
        root = five;
        root = root.insert(four);
        four.contains(four.getKey());  // Should cause exception, due to being called on non-root.
    }

    /**
     * Test traversal to in-order and out-order sorted lists.
     */
    @Test
    public void testInOrderTraversal_outOrderTraversal(){
        root = five;
        root = root.insert(six);
        root = root.insert(zero);
        root = root.insert(one);
        root = root.insert(two);
        root = root.insert(three);

        // Test in order.
        List<Integer> expectedList = Arrays.asList(0, 1, 2, 3, 5, 6);
        List<Integer> actualInOrderList = root.inOrderTraversal();
        assertEquals(expectedList, actualInOrderList);

        // Test out order.
        Collections.reverse(expectedList);
        List<Integer> actualOutOrderList = root.outOrderTraversal();
        assertEquals(expectedList, actualOutOrderList);

        UnitTestUtilities.validateAVLNode(root);
    }
}
