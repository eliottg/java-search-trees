package com.eliottgray.searchtrees;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class AVLTreeTest {

    private AVLTree<Integer> testTree = new AVLTree<>();

    @Before
    public void setUp(){
        testTree = new AVLTree<>();
    }

    /**
     * Initial tree is empty.
     */
    @Test
    public void isEmpty(){
        // Initial tree is empty.
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
     * Add multiple nodes to the far right branch, until the tree rotates 3 times.
     *
     *  { Simple Rotation }        { Simple Rotation }              { Complex Rotation }
     *
     *         Left        Insert          Left        Insert         Right        Left
     *    1            2           2               2             2          2             3
     *     \          / \         / \             / \           / \        / \           / \
     *      2        1   3       1   3           1   5         1   5      1   3         2   5
     *       \                        \             / \           / \          \       /   / \
     *        3                        5           3   6         3   6          5     1   4   6
     *                                  \                         \            / \
     *                                   6                         4          4   6
     */
    @Test
    public void insert_repeatedRotations_left() throws InvalidSearchTreeException{
        // Unbalance tree with insertions.
        testTree = testTree.insert(1);
        testTree = testTree.insert(2);
        testTree = testTree.insert(3);

        // Verify first rotation
        assertEquals(2, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().getRight().key.intValue());
        assertEquals(3, testTree.getRoot().size);
        assertEquals(2, testTree.getRoot().height);
        testTree.validate();

        // Unbalance tree with insertions.
        testTree = testTree.insert(5);
        testTree = testTree.insert(6);

        // Verify second rotation
        assertEquals(2, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(3, testTree.getRoot().getRight().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().getRight().key.intValue());
        assertEquals(5, testTree.getRoot().size);
        assertEquals(3, testTree.getRoot().height);
        testTree.validate();

        // Unbalance tree with insertions.
        testTree = testTree.insert(4);

        // Verify complex rotation.
        assertEquals(3, testTree.getRoot().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(4, testTree.getRoot().getRight().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().getRight().key.intValue());
        assertEquals(6, testTree.getRoot().size);
        assertEquals(3, testTree.getRoot().height);
        testTree.validate();
    }

    /**
     * Add multiple nodes to the far left branch, until the tree rotates 3 times.
     *
     *  { Simple Rotation }        { Simple Rotation }              { Complex Rotation }
     *
     *         Right       Insert          Right        Insert         Left        Right
     *       6         5            5               5             5          5             4
     *      /         / \          / \             / \           / \        / \           / \
     *     5         4   6        4   6           2   6         2   6      4   6         2   5
     *    /                      /               / \           / \        /             / \   \
     *   4                      2               1   4         1   4      2             1   3   6
     *                         /                                 /      / \
     *                        1                                 3      1   3
     */
    @Test
    public void insert_repeatedRotations_right() throws InvalidSearchTreeException{
        // Unbalance tree with insertions.
        testTree = testTree.insert(6);
        testTree = testTree.insert(5);
        testTree = testTree.insert(4);

        // Verify first rotation
        assertEquals(5, testTree.getRoot().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(4, testTree.getRoot().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().size);
        assertEquals(2, testTree.getRoot().height);
        testTree.validate();

        // Unbalance tree with insertions.
        testTree = testTree.insert(2);
        testTree = testTree.insert(1);

        // Verify second rotation
        assertEquals(5, testTree.getRoot().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(4, testTree.getRoot().getLeft().getRight().key.intValue());
        assertEquals(5, testTree.getRoot().size);
        assertEquals(3, testTree.getRoot().height);
        testTree.validate();

        // Unbalance tree with insertions.
        testTree = testTree.insert(3);

        // Verify complex rotation.
        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().getRight().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().getLeft().getRight().key.intValue());
        assertEquals(6, testTree.getRoot().size);
        assertEquals(3, testTree.getRoot().height);
        testTree.validate();
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
     * Test deletion of multiple nodes, with varying number of children.
     *
     * Nodes to be deleted in brackets: []
     *
     *              Delete        Delete       Delete     Delete    Delete
     *        4              4            4           [4]      [3]
     *       / \            / \          / \          /
     *      2   5         [2]  5        3  [5]       3                   [empty]
     *       \   \          \
     *        3  [6]         3
     */
    @Test
    public void testDelete_zeroOrOneChild() throws InvalidSearchTreeException{
        // Fill initial tree.
        testTree = testTree.insert(4);
        testTree = testTree.insert(2);
        testTree = testTree.insert(5);
        testTree = testTree.insert(3);
        testTree = testTree.insert(6);
        testTree.validate();

        // Test deletion of a node with no children.
        testTree = testTree.delete(6);
        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertFalse(testTree.getRoot().getRight().hasRight());
        assertEquals(1, testTree.getRoot().getRight().size);
        assertEquals(1, testTree.getRoot().getRight().height);
        assertEquals(3, testTree.getRoot().height);
        assertEquals(4, testTree.getRoot().size);
        testTree.validate();

        // Test deletion of a node with one child.
        testTree = testTree.delete(2);
        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(3, testTree.getRoot().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(2, testTree.getRoot().height);
        assertEquals(3, testTree.getRoot().size);
        assertEquals(1, testTree.getRoot().getLeft().height);
        testTree.validate();

        // Test deletion of a node with no children.
        testTree = testTree.delete(5);
        assertEquals(4, testTree.getRoot().key.intValue());
        assertNull(testTree.getRoot().right);
        assertEquals(2, testTree.getRoot().height);
        assertEquals(2, testTree.getRoot().size);
        testTree.validate();

        // Test deletion of Root with one child.
        testTree = testTree.delete(4);
        assertEquals(3, testTree.getRoot().key.intValue());
        assertFalse(testTree.getRoot().hasLeft() && testTree.getRoot().hasRight());
        assertEquals(1, testTree.getRoot().size);
        assertEquals(1, testTree.getRoot().height);
        testTree.validate();

        // Test deletion of Root with no child.
        testTree = testTree.delete(3);
        assertTrue(testTree.isEmpty());

        // Test deletion of value from empty tree.
        testTree = testTree.delete(3);
        assertTrue(testTree.isEmpty());
    }

    /**
     * Test deletion of nodes with two children
     *
     *              Delete        Delete        Delete
     *        [4]             [3]             5             5
     *       /   \           /   \           / \           / \
     *     1      6         1     6        [1]  6         2   6
     *    / \    /         / \    /        / \           /
     *   0  (3) 5         0   2 (5)       0  (2)        0
     *      /
     *     2
     */
    @Test
    public void testDelete_nodeWithTwoChildren() throws InvalidSearchTreeException{
        testTree = testTree.insert(4);
        testTree = testTree.insert(1);
        testTree = testTree.insert(6);
        testTree = testTree.insert(3);
        testTree = testTree.insert(0);
        testTree = testTree.insert(5);
        testTree = testTree.insert(2);

        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(4, testTree.getRoot().height);
        testTree.validate();

        // Test deletion of root when LEFT subtree is longer.
        testTree = testTree.delete(4);
        assertEquals(3, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().getLeft().key.intValue());
        assertEquals(0, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().getRight().key.intValue());
        assertEquals(3, testTree.getRoot().height);
        assertEquals(6, testTree.getRoot().size);
        testTree.validate();

        // Test deletion of root when Right subtree is longer OR subtrees are same size.
        testTree = testTree.delete(3);
        assertEquals(5, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(0, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().getRight().key.intValue());
        assertEquals(3, testTree.getRoot().height);
        assertEquals(5, testTree.getRoot().size);
        testTree.validate();

        // Test deletion of non-root with two children.
        testTree = testTree.delete(1);
        assertEquals(5, testTree.getRoot().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(0, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().height);
        assertEquals(4, testTree.getRoot().size);
        testTree.validate();
    }

    /**
     * Attempt to delete keys which are not in the tree.
     */
    @Test
    public void testDelete_whenNothingToDelete() throws InvalidSearchTreeException{
        testTree = testTree.insert(4);
        testTree = testTree.insert(1);

        // Delete value that isn't in the tree, larger than largest node.
        testTree = testTree.delete(99);

        // Verify tree is unchanged.
        assertEquals(testTree.getRoot().key.intValue(), 4);
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(2, testTree.getRoot().height);
        assertEquals(2, testTree.getRoot().size);
        testTree.validate();

        // Delete value that isn't in the tree, smaller than smallest node.
        testTree = testTree.delete(-199);

        // Verify tree is unchanged.
        assertEquals(testTree.getRoot().key.intValue(), 4);
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(2, testTree.getRoot().height);
        assertEquals(2, testTree.getRoot().size);
        testTree.validate();

        // Delete value that isn't in the tree, between the available node values.
        testTree = testTree.delete(3);
        // Verify tree is unchanged.
        // Verify same results as before the deletion.
        assertEquals(testTree.getRoot().key.intValue(), 4);
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(2, testTree.getRoot().height);
        assertEquals(2, testTree.getRoot().size);
        testTree.validate();
    }

    /**
     * Test deletion which causes right rotation of tree.
     *
     *             Delete     Rotate
     *        3            3         2
     *       / \          /         / \
     *      2  [4]       2         1   3
     *     /            /
     *    1            1
     */
    @Test
    public void testDelete_rightRotation() throws InvalidSearchTreeException{
        testTree = testTree.insert(3);
        testTree = testTree.insert(4);
        testTree = testTree.insert(2);
        testTree = testTree.insert(1);

        assertEquals(3, testTree.getRoot().key.intValue());

        // Deletion should cause rotation.
        testTree = testTree.delete(4);
        assertEquals(2, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().getRight().key.intValue());
        assertEquals(2, testTree.getRoot().height);
        assertEquals(3, testTree.getRoot().size);
        testTree.validate();

    }

    /**
     * Test deletion which causes left rotation of tree.
     *
     *           Delete      Rotate
     *        3          3            4
     *       / \          \          / \
     *     [2]  4          4        3   5
     *           \          \
     *            5          5
     */
    @Test
    public void testDelete_leftRotation() throws InvalidSearchTreeException{
        testTree = testTree.insert(3);
        testTree = testTree.insert(4);
        testTree = testTree.insert(2);
        testTree = testTree.insert(5);

        assertEquals(3, testTree.getRoot().key.intValue());

        // Deletion should cause rotation.
        testTree = testTree.delete(2);
        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(3, testTree.getRoot().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(2, testTree.getRoot().height);
        assertEquals(3, testTree.getRoot().size);
        testTree.validate();

    }

    /**
     * Test that deletion of nodes properly results in replacement of all direct parent nodes.
     */
    @Test
    public void testDelete_maintainsImmutability(){
        testTree = testTree.insert(4);
        testTree = testTree.insert(1);
        testTree = testTree.insert(6);

        // Delete left child of root, leaving root and right child.
        AVLTree<Integer> postDelete = testTree.delete(1);

        // Ensure that root now has a new identity, but right child remains the same.
        assertEquals(testTree.getRoot().key, postDelete.getRoot().key);
        assertNotEquals(testTree.getRoot(), postDelete.getRoot());
        assertEquals(testTree.getRoot().right, postDelete.getRoot().right);
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
        Tree<Integer> testTree = new AVLTree<>(customComparator);

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
