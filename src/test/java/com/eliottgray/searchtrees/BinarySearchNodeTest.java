package com.eliottgray.searchtrees;

import org.junit.Test;

import static org.junit.Assert.*;

public class BinarySearchNodeTest {

    /**
     * Test construction: Create a single node, and test properties.
     */
    @Test
    public void constructSingleNode() {
        BinarySearchNode<Integer> root = new BinarySearchNode<>(0);
        assertFalse(root.hasLeft());
        assertFalse(root.hasRight());
        assertNull(root.left);
        assertNull(root.right);
        assertEquals(root.key, Integer.valueOf(0));
        assertEquals(1, root.size);
        assertEquals(1, root.height);
        assertEquals(0, root.getBalanceFactor());
    }

    /**
     * Construct a small graph of nodes, and test properties.
     */
    @Test
    public void constructSmallGraph(){
        BinarySearchNode<Integer> leaf = new BinarySearchNode<>(20);
        BinarySearchNode<Integer> root = new BinarySearchNode<>(10, leaf, null);
        assertTrue(root.hasLeft());
        assertFalse(root.hasRight());
        assertEquals(leaf, root.getLeft());
        assertNull(root.getRight());
        assertEquals(10, root.key.intValue());
        assertEquals(20, root.getLeft().key.intValue());
        assertEquals(2, root.size);
        assertEquals(2, root.height);
        assertEquals(-1, root.getBalanceFactor());
    }
}
