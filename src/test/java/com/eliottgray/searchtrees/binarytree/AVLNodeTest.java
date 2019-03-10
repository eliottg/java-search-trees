package com.eliottgray.searchtrees.binarytree;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AVLNodeTest {
    private AVLNode<Integer> root;
    private Integer zero = 0;
    private Integer one = 1;
    private Integer two = 2;
    private Integer three = 3;
    private Integer four = 4;
    private Integer five = 5;
    private Integer six = 6;
    private Comparator<Integer> comparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer one, Integer two) {
            return one.compareTo(two);
        }
    };

    /**
     * Construct a single node, and test properties.
     */
    @Test
    public void testSingleNode(){
        root = new AVLNode<>(zero);
        assertFalse(root.hasLeft());
        assertFalse(root.hasRight());
        assertNull(root.getLeft());
        assertNull(root.getRight());
        assertEquals(root.getKey(), new Integer(0));
        assertEquals(1, root.getSize());
        assertEquals(1, root.getHeight());
        assertTrue(root.contains(root.getKey(), comparator));
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
        root = new AVLNode<>(three);

        // Add left to root and test relationships.
        root = root.insert(one, comparator);
        assertEquals(three, root.getKey());
        assertTrue(root.hasLeft());
        assertFalse(root.hasRight());
        assertEquals(one, root.getLeft().getKey());
        assertFalse(root.getLeft().hasLeft() && root.getLeft().hasRight());
        assertEquals(2, root.getSize());
        assertEquals(2, root.getHeight());
        assertTrue(root.contains(root.getLeft().getKey(), comparator));
        assertTrue(root.contains(root.getKey(), comparator));
        UnitTestUtilities.validateAVLNode(root);
    }

//    /**
//     * Test that adding a duplicate node results in replacement of that node with the new one.
//     */
//    @Test
//    public void testDuplicateKey(){
//
//        // Establish initial graph.
//        root = new AVLNode<>(five);
//        root = root.insert(one, comparator);
//
//        // Add duplicate leaf node.
//        root = root.insert(one, comparator);
//
//        // Test graph for duplicates.
//        assertEquals(five, root.getKey());
//        assertTrue(root.hasLeft());
//        assertFalse(root.hasRight());
//        assertEquals(root.getLeft().getKey(), one);
//        assertFalse(duplicateOne.hasLeft() && duplicateOne.hasRight());
//        assertEquals(2, root.getSize());
//        assertEquals(2, root.getHeight());
//        assertTrue(root.contains(one.getKey(), comparator));
//        assertTrue(root.contains(root.getKey(), comparator));
//        UnitTestUtilities.validateAVLNode(root);
//
//        // Add duplicate root node.
//        root = root.insert(five, comparator);
//
//        // Test graph for duplicates.
//       assertEquals(duplicateFive, root);
//       assertTrue(duplicateFive.hasLeft());
//       assertFalse(duplicateFive.hasRight());
//       assertEquals(duplicateFive.getLeft(), duplicateOne);
//       assertFalse(duplicateOne.hasLeft() && duplicateOne.hasRight());
//       assertEquals(2, root.getSize());
//       assertEquals(2, root.getHeight());
//       assertTrue(root.contains(one.getKey(), comparator));
//       assertTrue(root.contains(five.getKey(), comparator));
//       UnitTestUtilities.validateAVLNode(root);
//    }

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
        root = new AVLNode<>(one);

        // Unbalance tree with insertions.
        root = root.insert(two, comparator);
        root = root.insert(three, comparator);

        // Verify first rotation
        assertEquals(two, root.getKey());
        assertEquals(one, root.getLeft().getKey());
        assertEquals(three, root.getRight().getKey());
        assertEquals(3, root.getSize());
        assertEquals(2, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(five, comparator);
        root = root.insert(six, comparator);

        // Verify second rotation
        assertEquals(two, root.getKey());
        assertEquals(one, root.getLeft().getKey());
        assertEquals(five, root.getRight().getKey());
        assertEquals(three, root.getRight().getLeft().getKey());
        assertEquals(six, root.getRight().getRight().getKey());
        assertEquals(5, root.getSize());
        assertEquals(3, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(four, comparator);

        // Verify complex rotation.
        assertEquals(three, root.getKey());
        assertEquals(two, root.getLeft().getKey());
        assertEquals(one, root.getLeft().getLeft().getKey());
        assertEquals(five, root.getRight().getKey());
        assertEquals(four, root.getRight().getLeft().getKey());
        assertEquals(six, root.getRight().getRight().getKey());
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
        root = new AVLNode<>(six);

        // Unbalance tree with insertions.
        root = root.insert(five, comparator);
        root = root.insert(four, comparator);

        // Verify first rotation
        assertEquals(five, root.getKey());
        assertEquals(six, root.getRight().getKey());
        assertEquals(four, root.getLeft().getKey());
        assertEquals(3, root.getSize());
        assertEquals(2, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(two, comparator);
        root = root.insert(one, comparator);

        // Verify second rotation
        assertEquals(five, root.getKey());
        assertEquals(two, root.getLeft().getKey());
        assertEquals(six, root.getRight().getKey());
        assertEquals(one, root.getLeft().getLeft().getKey());
        assertEquals(four, root.getLeft().getRight().getKey());
        assertEquals(5, root.getSize());
        assertEquals(3, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Unbalance tree with insertions.
        root = root.insert(three, comparator);

        // Verify complex rotation.
        assertEquals(four, root.getKey());
        assertEquals(two, root.getLeft().getKey());
        assertEquals(five, root.getRight().getKey());
        assertEquals(six, root.getRight().getRight().getKey());
        assertEquals(one, root.getLeft().getLeft().getKey());
        assertEquals(three, root.getLeft().getRight().getKey());
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
        root = new AVLNode<>(four);
        root = root.insert(two, comparator);
        root = root.insert(five, comparator);
        root = root.insert(three, comparator);
        root = root.insert(six, comparator);
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of a node with no children.
        root = root.delete(six, comparator);
        assertEquals(four, root.getKey());
        assertEquals(five, root.getRight().getKey());
        assertFalse(root.getRight().hasRight());
        assertEquals(1, root.getRight().getSize());
        assertEquals(1, root.getRight().getHeight());
        assertEquals(3, root.getHeight());
        assertEquals(4, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of a node with one child.
        root = root.delete(two, comparator);
        assertEquals(four, root.getKey());
        assertEquals(three, root.getLeft().getKey());
        assertEquals(five, root.getRight().getKey());
        assertEquals(2, root.getHeight());
        assertEquals(3, root.getSize());
        assertEquals(1, root.getLeft().getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of a node with no children.
        root = root.delete(five, comparator);
        assertEquals(four, root.getKey());
        assertNull(root.getRight());
        assertEquals(2, root.getHeight());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of Root with one child.
        root = root.delete(four, comparator);
        assertEquals(three, root.getKey());
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
        root = new AVLNode<>(three);
        root = root.insert(four, comparator);
        root = root.insert(two, comparator);
        root = root.insert(one, comparator);

        assertEquals(three, root.getKey());

        // Deletion should cause rotation.
        root = root.delete(four, comparator);
        assertEquals(two, root.getKey());
        assertEquals(one, root.getLeft().getKey());
        assertEquals(three, root.getRight().getKey());
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
        root = new AVLNode<>(three);
        root = root.insert(four, comparator);
        root = root.insert(two, comparator);
        root = root.insert(five, comparator);

        assertEquals(three, root.getKey());

        // Deletion should cause rotation.
        root = root.delete(two, comparator);
        assertEquals(four, root.getKey());
        assertEquals(three, root.getLeft().getKey());
        assertEquals(five, root.getRight().getKey());
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
        root = new AVLNode<>(four);
        root = root.insert(one, comparator);
        root = root.insert(six, comparator);
        root = root.insert(three, comparator);
        root = root.insert(zero, comparator);
        root = root.insert(five, comparator);
        root = root.insert(two, comparator);

        assertEquals(four, root.getKey());
        assertEquals(4, root.getHeight());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of root when LEFT subtree is longer.
        root = root.delete(4, comparator);
        assertEquals(three, root.getKey());
        assertEquals(one, root.getLeft().getKey());
        assertEquals(six, root.getRight().getKey());
        assertEquals(five, root.getRight().getLeft().getKey());
        assertEquals(zero, root.getLeft().getLeft().getKey());
        assertEquals(two, root.getLeft().getRight().getKey());
        assertEquals(3, root.getHeight());
        assertEquals(6, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of root when Right subtree is longer OR subtrees are same size.
        root = root.delete(3, comparator);
        assertEquals(five, root.getKey());
        assertEquals(one, root.getLeft().getKey());
        assertEquals(six, root.getRight().getKey());
        assertEquals(zero, root.getLeft().getLeft().getKey());
        assertEquals(two, root.getLeft().getRight().getKey());
        assertEquals(3, root.getHeight());
        assertEquals(5, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Test deletion of non-root with two children.
        root = root.delete(one, comparator);
        assertEquals(five, root.getKey());
        assertEquals(two, root.getLeft().getKey());
        assertEquals(six, root.getRight().getKey());
        assertEquals(zero, root.getLeft().getLeft().getKey());
        assertEquals(3, root.getHeight());
        assertEquals(4, root.getSize());
        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Attempt to delete keys which are not in the tree.
     */
    @Test
    public void testDelete_whenNothingToDelete(){
        root = new AVLNode<>(four);
        root = root.insert(one, comparator);

        // Delete value that isn't in the tree, larger than largest node.
        root = root.delete(99, comparator);

        // Verify tree is unchanged.
        assertEquals(root.getKey(), four);
        assertEquals(one, root.getLeft().getKey());
        assertEquals(2, root.getHeight());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Delete value that isn't in the tree, smaller than smallest node.
        root = root.delete(-199, comparator);

        // Verify tree is unchanged.
        assertEquals(root.getKey(), four);
        assertEquals(one, root.getLeft().getKey());
        assertEquals(2, root.getHeight());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);

        // Delete value that isn't in the tree, between the available node values.
        root = root.delete(3, comparator);
        // Verify tree is unchanged.
        // Verify same results as before the deletion.
        assertEquals(root.getKey(), four);
        assertEquals(one, root.getLeft().getKey());
        assertEquals(2, root.getHeight());
        assertEquals(2, root.getSize());
        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Test ability to search tree for specific Key.
     */
    @Test
    public void testContains(){
        root = new AVLNode<>(five);
        root = root.insert(four, comparator);
        root = root.insert(six, comparator);

        // Test if root is found.
        assertTrue(root.contains(five, comparator));

        // Test if left leaf is found.
        assertTrue(root.contains(four, comparator));

        // Test if right leaf is found.
        assertTrue(root.contains(six, comparator));

        // Test if unavailable node is reported as unavailable.
        assertFalse(root.contains(zero, comparator));
    }

    /**
     * Test traversal to in-order sorted list.
     */
    @Test
    public void testInOrderTraversal(){
        root = new AVLNode<>(five);
        root = root.insert(six, comparator);
        root = root.insert(zero, comparator);
        root = root.insert(one, comparator);
        root = root.insert(two, comparator);
        root = root.insert(three, comparator);

        // Test in order.
        List<Integer> expectedList = Arrays.asList(zero, one, two, three, five, six);
        List<Integer> actualInOrderList = root.inOrderTraversal();
        assertEquals(expectedList, actualInOrderList);

        UnitTestUtilities.validateAVLNode(root);
    }

    /**
     * Test correct retrieval of a subset of Values based on a range of Keys.
     */
    @Test
    public void testGetRangeList_presentInTree(){
        root = new AVLNode<>(one);

        root = root.insert(two, comparator);
        root = root.insert(three, comparator);
        root = root.insert(four, comparator);
        root = root.insert(five, comparator);

        List<Integer> expectedValues = new ArrayList<Integer>(){{add(two); add(three); add(four);}};
        List<Integer> actualValues = root.getRange(two, four, comparator);
        assertEquals(expectedValues, actualValues);
    }

    /**
     * Test correct retrieval of a subset of Values based on a range of Keys.
     */
    @Test
    public void testGetRangeList_notPresentInTree(){
        root = new AVLNode<>(one);

        List<Integer> expectedValues = new ArrayList<>();
        List<Integer> actualValues = root.getRange(two, four, comparator);
        assertEquals(expectedValues, actualValues);
    }
}
