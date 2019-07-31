package com.eliottgray.searchtrees.binarytree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Tree <Key extends Comparable<Key>> {

    final Comparator<Key> comparator;

    /**
     * Create an empty tree.
     * Comparison of Keys will be performed with the default compareTo method of the Key.
     */
    public Tree(){
        this.comparator = Comparable::compareTo;
    }

    /**
     * Create an empty tree, with overridden comparator.
     * The default compareTo method of the Key class will not be used.
     * @param comparator    Key comparison method.
     */
    public Tree(Comparator<Key> comparator){
        this.comparator = comparator;
    }

    /**
     * @return  Root Node of Tree..
     */
    protected abstract Node<Key> getRoot();

    /**
     * Insert a new Key into the Tree.
     * A new Tree is returned which contains the change.
     * If the inserted Key duplicates the same sorted location as an existing Key, the existing Key will be overwritten.
     * @param key   Key to insert.
     * @return      Updated Tree.
     */
    public abstract Tree<Key> insert(Key key);

    /**
     * Delete a Key from the Tree.
     * A new Tree is returned which contains the change.
     * If the given Key is not contained within the Tree, the returned Tree will be the same object as the original.
     * @param key   Key to insert.
     * @return      Updated Tree.
     */
    public abstract Tree<Key> delete(Key key);

    /**
     * @return  Whether the Tree is empty or not.
     */
    public boolean isEmpty(){
        return getRoot() == null;
    }

    /**
     * @return  Number of Keys in the tree.
     */
    public int size(){
        if (isEmpty()) {
            return 0;
        } else {
            return getRoot().getSize();
        }
    }

    /**
     * Determine whether or not the given Key is contained within the tree.
     * @param key   Key to search for.
     * @return      Presence of Key in tree.
     */
    public boolean contains(Key key){
        if (isEmpty()){
            return false;
        } else {
            return getRoot().contains(key, comparator);
        }
    }

    /**
     * @return  List of Keys in ascending order.
     */
    public List<Key> toAscendingList(){
        if (isEmpty()){
            return new ArrayList<>();
        } else {
            return getRoot().inOrderTraversal();
        }
    }

    /**
     * Return a List of Keys between the given start and end, inclusive.
     * @param start     Start Key.
     * @param end       End Key.
     * @return          List of Keys within range, inclusive.
     */
    public List<Key> getRange(Key start, Key end){
        if (isEmpty()){
            return new ArrayList<>();
        } else {
            return getRoot().getRange(start, end, comparator);
        }
    }

    /**
     * @return  Minimum Key.
     */
    public Key getMin(){
        if (isEmpty()){
            return null;
        } else {
            Node<Key> current = getRoot();
            while (current.hasLeft()){
                current = current.getLeft();
            }
            return current.getKey();
        }
    }

    /**
     * @return  Maximum Key.
     */
    public Key getMax(){
        if (isEmpty()){
            return null;
        } else {
            Node<Key> current = getRoot();
            while (current.hasRight()){
                current = current.getRight();
            }
            return current.getKey();
        }
    }

    public void validate () throws InvalidSearchTreeException{
        if (!isEmpty()) {
            getRoot().validate();
        }
    }
}
