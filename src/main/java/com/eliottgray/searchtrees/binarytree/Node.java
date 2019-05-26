package com.eliottgray.searchtrees.binarytree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

abstract class Node <Key extends Comparable<Key>> {

    abstract Node<Key> getLeft();
    abstract Node<Key> getRight();
    abstract int getHeight();
    abstract int getSize();
    abstract Key getKey();
    boolean hasLeft(){ return getLeft() != null; }
    boolean hasRight(){ return getRight() != null; }

    /**
     * Describes the relative height of the left and right subtrees.
     * If right tree is greater, balance factor is positive.
     * If left tree is greater, balance factor is negative.
     * Else, balance factor is zero.
     * @return      integer describing the balance factor.
     */
    int getBalanceFactor(){ return (hasRight() ? getRight().getHeight() : 0) - (hasLeft() ? getLeft().getHeight() : 0); }

    /**
     * Retrieve the value for a given key if present within the tree; return null otherwise.
     * @param key   Key to find.
     * @return      Value for key; else null.
     */
    boolean contains(Key key, Comparator<Key> comparator){
        Node<Key> current = this;
        Boolean contains = null;
        while(contains == null){
            int comparison = comparator.compare(key, current.getKey());
            if (comparison == 0){
                contains = true;
            } else if (current.hasLeft() && comparison < 0){
                current = current.getLeft();
            } else if (current.hasRight() && comparison > 0){
                current = current.getRight();
            } else {
                contains = false;
            }
        }
        return contains;
    }


    List<Key> getRange(Key start, Key end, Comparator<Key> comparator){
        List<Key> result = new ArrayList<>();
        return this.getRange(start, end, result, comparator);
    }

    private List<Key> getRange(Key start, Key end, List<Key> result, Comparator<Key> comparator){
        boolean isLessThan = comparator.compare(start, this.getKey()) <= 0;
        boolean isGreaterThan = comparator.compare(end, this.getKey()) >= 0;
        if (isLessThan && this.hasLeft()){
            result = getLeft().getRange(start, end, result, comparator);
        }
        if (isLessThan && isGreaterThan){
            result.add(this.getKey());
        }
        if (isGreaterThan && this.hasRight()){
            result = getRight().getRange(start, end, result, comparator);
        }
        return result;
    }

    List<Key> inOrderTraversal(){
        List<Key> result = new ArrayList<>(this.getSize());
        return this.inOrderTraversal(result);
    }

    private List<Key> inOrderTraversal(List<Key> result){
        if (this.hasLeft()){
            result = getLeft().inOrderTraversal(result);
        }
        result.add(this.getKey());
        if (this.hasRight()){
            result = getRight().inOrderTraversal(result);
        }
        return result;
    }

}
