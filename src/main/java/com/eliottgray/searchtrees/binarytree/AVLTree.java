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

    public void delete(Key key){
        root = root.delete(key, comparator);
    }

    public void insert(Key key){
        AVLNode<Key> newAVLNode = new AVLNode<>(key);
        if (isEmpty()){
            root = newAVLNode;
        } else {
            root = root.insert(newAVLNode, comparator);
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

    public List<Key> toDescendingList(){
        if (root == null){
            return new ArrayList<>();
        } else {
            return root.outOrderTraversal();
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
