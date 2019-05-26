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
public class AVLTree<Key extends Comparable<Key>> extends Tree<Key>{

    final AVLNode<Key> root;
    private final Comparator<Key> comparator;

    /**
     * Empty tree. Comparison of Keys to be performed with default compareTo method.
     */
    public AVLTree(){
        this.root = null;
        this.comparator = Comparable::compareTo;
    }

    /**
     * Empty tree, with comparator override.
     * @param comparator    Comparison function with which to override default compareTo of Key.
     */
    public AVLTree(Comparator<Key> comparator){
        this.root = null;
        this.comparator = comparator;
    }

    /**
     * Construct a new tree from an older tree.
     * @param root          Existing root node.
     * @param comparator    Comparator corresponding to current root node.
     */
    private AVLTree(AVLNode<Key> root, Comparator<Key> comparator){
        this.root = root;
        this.comparator = comparator;
    }

    Node<Key> getRoot(){ return root; }
    Comparator<Key> getComparator(){ return comparator; }

    public AVLTree<Key> delete(Key key){
        if (root == null){
            return this;
        } else {
            AVLNode<Key> newRoot = root.delete(key, comparator);
            return new AVLTree<>(newRoot, comparator);
        }
    }

    public AVLTree<Key> insert(Key key){
        if (root == null){
            AVLNode<Key> newRoot = new AVLNode<>(key);
            return new AVLTree<>(newRoot, comparator);
        } else {
            AVLNode<Key> newRoot = root.insert(key, comparator);
            return new AVLTree<>(newRoot, comparator);
        }
    }
}
