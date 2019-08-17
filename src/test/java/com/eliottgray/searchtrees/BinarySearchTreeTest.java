package com.eliottgray.searchtrees;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class BinarySearchTreeTest extends TreeTestSkeleton {

    @Override
    public BinarySearchTree<Integer> buildEmptyTree(Comparator<Integer> comparator){
        return new BinarySearchTree<>(comparator);
    }

    private BinarySearchTree<Integer> testTree = new BinarySearchTree<>();

    @Before
    public void setUp(){
        testTree = buildEmptyTree(Integer::compareTo);
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
}
