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
public class AVLTree<Key extends Comparable<Key>>{

    private AVLNode<Key> root;
    private Comparator<Key> comparator;

    /**
     * Empty tree, for adding items later.
     */
    public AVLTree(){
        this.comparator = new Comparator<Key>() {
            @Override
            public int compare(Key key1, Key key2) {
                return key1.compareTo(key2);
            }
        };
    }

    public AVLTree(Comparator<Key> comparator){
        this.comparator = comparator;
    }

    /**
     * Construct a new tree.
     * @param root          Existing root node.
     * @param comparator    Comparator corresponding to current root node.
     */
    private AVLTree(AVLNode<Key> root, Comparator<Key> comparator){
        this.root = root;
        this.comparator = comparator;
    }

    public AVLTree<Key> delete(Key key){
        AVLNode<Key> newRoot = root.delete(key, comparator);
        return new AVLTree<>(newRoot, comparator);
    }

    public AVLTree<Key> insert(Key key){
        if (isEmpty()){
            AVLNode<Key> newRoot = new AVLNode<>(key);
            return new AVLTree<>(newRoot, comparator);
        } else {
            AVLNode<Key> newRoot = root.insert(key, comparator);
            return new AVLTree<>(newRoot, comparator);
        }
    }

    public boolean isEmpty(){
        return root == null;
    }

    AVLNode<Key> getRoot(){
        return root;
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
            return root.getSize();
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
