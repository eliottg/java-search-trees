package com.eliottgray.searchtree.binarytree;

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
public class AVLTree {
    private AVLNode root;

    /**
     * Empty tree, for adding items later.
     */
    public AVLTree(){}

    /**
     * Tree with initial data.
     * @param initialData
     */
    public AVLTree(Integer[] initialData){ /// @todo accept different basic Java collections types - arrays, arrayList, sets, collections, etc.
        if (initialData.length == 0){
            return;
        } else {
            root = new AVLNode(initialData[0]);
        }
        for (int i = 1; i < initialData.length; i++){
            int value = initialData[i];
            AVLNode newAVLNode = new AVLNode(value);
            root = root.insert(newAVLNode);
        }
    }

    public void delete(int value){
        root = root.delete(value);
    }

    public void insert(int value){
        AVLNode newAVLNode = new AVLNode(value);
        if (isEmpty()){
            root = newAVLNode;
        } else {
            root = root.insert(newAVLNode);
        }
    }

    public boolean isEmpty(){
        return root == null;
    }

    public AVLNode getRoot(){
        return root;
    }

    /**
     * Determine if a node representing the given value is contained within the tree.
     * @param value     int value to search for.
     * @return          presence of value in tree.
     */
    public boolean contains(int value){
        if (root == null){
            return false;
        } else {
            return root.contains(value);
        }
    }

    public int size(){
        if (root == null) {
            return 0;
        } else {
            return root.getSize();
        }
    }

    public List<Integer> toAscendingList(){
        if (root == null){
            return new ArrayList<>();
        } else {
            return root.inOrderTraversal();
        }
    }

    public List<Integer> toDescendingList(){
        if (root == null){
            return new ArrayList<>();
        } else {
            return root.outOrderTraversal();
        }
    }
}
