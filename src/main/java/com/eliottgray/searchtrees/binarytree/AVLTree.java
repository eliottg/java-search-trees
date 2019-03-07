package com.eliottgray.searchtrees.binarytree;

import java.util.ArrayList;
import java.util.List;

/**
 * Why use Binary Search Trees over, say, HashTables?
 *
 * 1) Range queries.  Given a tree, find all elements between range X, Y.
 *      a) variations on this for multiple dimensions, e.g. R-Tree, K-DTree, etc.
 * 2) Space Complexity.  Given that each new data point creates a separate object, more memory that is necessary will not be allocated.
 * 3) Easy ordering.  Traversal of all elements in-order results in an ordered list; not something typically done with Hash Tables.
 * */
public class AVLTree<Key extends Comparable<Key>, Value> {

    private AVLNode<Key, Value> root;

    /**
     * Empty tree, for adding items later.
     */
    public AVLTree(){}

    public void delete(Key key){
        root = root.delete(key);
    }

    public void insert(Key key, Value value){
        AVLNode<Key, Value> newAVLNode = new AVLNode<>(key, value);
        if (isEmpty()){
            root = newAVLNode;
        } else {
            root = root.insert(newAVLNode);
        }
    }

    public boolean isEmpty(){
        return root == null;
    }

    public AVLNode<Key, Value> getRoot(){
        return root;
    }

    /**
     * Retrieve the value for a given key within the tree.  Return null if not available.
     * @param key     int value to search for.
     * @return          presence of value in tree.
     */
    public Value get(Key key){
        if (root == null){
            return null;
        } else {
            return root.retrieve(key);
        }
    }

    public int size(){
        if (root == null) {
            return 0;
        } else {
            return root.getSize();
        }
    }

    public List<Value> toAscendingList(){
        if (root == null){
            return new ArrayList<>();
        } else {
            return root.inOrderTraversal();
        }
    }

    public List<Value> toDescendingList(){
        if (root == null){
            return new ArrayList<>();
        } else {
            return root.outOrderTraversal();
        }
    }
}
