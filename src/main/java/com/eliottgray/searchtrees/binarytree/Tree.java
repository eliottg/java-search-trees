package com.eliottgray.searchtrees.binarytree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Tree <Key extends Comparable<Key>> {

    final Comparator<Key> comparator;

    /**
     * Empty tree. Comparison of Keys to be performed with default compareTo method.
     */
    public Tree(){
        this.comparator = Comparable::compareTo;
    }

    /**
     * Empty tree, with comparator override.
     * @param comparator    Comparison function with which to override default compareTo of Key.
     */
    public Tree(Comparator<Key> comparator){
        this.comparator = comparator;
    }

    abstract Node<Key> getRoot();

    public boolean isEmpty(){
        return getRoot() == null;
    }

    public int size(){
        if (isEmpty()) {
            return 0;
        } else {
            return getRoot().getSize();
        }
    }

    /**
     * Retrieve the value for a given key within the tree.  Return null if not available.
     * @param key     int value to search for.
     * @return          presence of value in tree.
     */
    public boolean contains(Key key){
        if (isEmpty()){
            return false;
        } else {
            return getRoot().contains(key, comparator);
        }
    }

    public List<Key> toAscendingList(){
        if (isEmpty()){
            return new ArrayList<>();
        } else {
            return getRoot().inOrderTraversal();
        }
    }

    public List<Key> getRange(Key start, Key end){
        if (isEmpty()){
            return new ArrayList<>();
        } else {
            return getRoot().getRange(start, end, comparator);
        }
    }




}
