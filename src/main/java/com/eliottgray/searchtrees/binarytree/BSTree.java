package com.eliottgray.searchtrees.binarytree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Why use Binary Search Trees over, say, HashTables?
 *
 * 1) Range queries.  Given a tree, find all elements between range X, Y.
 *      a) variations on this for multiple dimensions, e.g. R-Tree, K-DTree, etc.
 * 2) Space Complexity.  Given that each new data point creates a separate object, more memory that is necessary will not be allocated.
 * 3) Easy ordering.  Traversal of all elements in-order results in an ordered list; not something typically done with Hash Tables.
 * */
public class BSTree<Key extends Comparable<Key>>{

    final BSTNode<Key> root;
    private final Comparator<Key> comparator;

    /**
     * Empty tree. Comparison of Keys to be performed with default compareTo method.
     */
    public BSTree(){
        this.root = null;
        this.comparator = Comparable::compareTo;
    }

    /**
     * Empty tree, with comparator override.
     * @param comparator    Comparison function with which to override default compareTo of Key.
     */
    public BSTree(Comparator<Key> comparator){
        this.root = null;
        this.comparator = comparator;
    }

    /**
     * Construct a new tree from an older tree.
     * @param root          Existing root node.
     * @param comparator    Comparator corresponding to current root node.
     */
    private BSTree(BSTNode<Key> root, Comparator<Key> comparator){
        this.root = root;
        this.comparator = comparator;
    }

    public BSTree<Key> delete(Key key){
        if (root == null){
            return this;
        } else {
            BSTNode<Key> newRoot = root.delete(key, comparator);
            return new BSTree<>(newRoot, comparator);
        }
    }

    public BSTree<Key> insert(Key key){
        if (root == null){
            BSTNode<Key> newRoot = new BSTNode<>(key);
            return new BSTree<>(newRoot, comparator);
        } else {
            BSTNode<Key> newRoot = root.insert(key, comparator);
            return new BSTree<>(newRoot, comparator);
        }
    }

    public boolean isEmpty(){
        return root == null;
    }

    /**
     * Retrieve the value for a given key within the tree.  Return null if not available.
     * @param key     int value to search for.
     * @return          presence of value in tree.
     */
    public boolean contains(Key key){
        if (root == null){
            return false;
        } else {
            return root.contains(key, comparator);
        }
    }

    public int size(){
        if (root == null) {
            return 0;
        } else {
            return root.size;
        }
    }

    public List<Key> toAscendingList(){
        if (root == null){
            return new ArrayList<>();
        } else {
            return root.inOrderTraversal();
        }
    }

    public List<Key> getRange(Key start, Key end){
        if (root == null){
            return new ArrayList<>();
        } else {
            return root.getRange(start, end, comparator);
        }
    }
}
