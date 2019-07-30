package com.eliottgray.searchtrees.binarytree;

import java.util.Comparator;

/**
 * Why use Binary Search Trees over, say, HashTables?
 *
 * 1) Range queries.  Given a tree, find all elements between range X, Y.
 *      a) variations on this for multiple dimensions, e.g. R-Tree, K-DTree, etc.
 * 2) Space Complexity.  Given that each new data point creates a separate object, more memory that is necessary will not be allocated.
 * 3) Easy ordering.  Traversal of all elements in-order results in an ordered list; not something typically done with Hash Tables.
 * */
public class BinarySearchTree<Key extends Comparable<Key>> extends Tree<Key> {

    private final BinarySearchNode<Key> root;

    /**
     * Empty tree. Comparison of Keys to be performed with default compareTo method.
     */
    public BinarySearchTree(){
        super();
        root = null;
    }

    /**
     * Empty tree, with comparator override.
     * @param comparator    Comparison function with which to override default compareTo of Key.
     */
    public BinarySearchTree(Comparator<Key> comparator){
        super(comparator);
        root = null;
    }

    /**
     * Construct a new tree from an older tree.
     * @param root          Existing root node.
     * @param comparator    Comparator corresponding to current root node.
     */
    private BinarySearchTree(BinarySearchNode<Key> root, Comparator<Key> comparator){
        super(comparator);
        this.root = root;
    }

    Node<Key> getRoot(){ return root; }

    public BinarySearchTree<Key> delete(Key key){
        if (root == null){
            return this;
        } else {
            BinarySearchNode<Key> newRoot = root.delete(key, comparator);
            return new BinarySearchTree<>(newRoot, comparator);
        }
    }

    public BinarySearchTree<Key> insert(Key key){
        if (root == null){
            BinarySearchNode<Key> newRoot = new BinarySearchNode<>(key);
            return new BinarySearchTree<>(newRoot, comparator);
        } else {
            BinarySearchNode<Key> newRoot = root.insert(key, comparator);
            return new BinarySearchTree<>(newRoot, comparator);
        }
    }
}
