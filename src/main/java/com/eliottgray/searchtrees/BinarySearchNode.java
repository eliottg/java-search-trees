package com.eliottgray.searchtrees;

class BinarySearchNode<Key extends Comparable<Key>> extends Node<Key>{

    final BinarySearchNode<Key> left;
    final BinarySearchNode<Key> right;

    /**
     * Construct new leaf node, with no children.
     * @param key   Comparable Key for node.
     */
    BinarySearchNode(Key key){
        super(key);
        left = null;
        right = null;
    }

    /**
     * Construct replacement root node, with existing children.
     * @param key       Comparable Key for node.
     * @param left      Existing left child.
     * @param right     Existing right child.
     */
    BinarySearchNode(Key key, BinarySearchNode<Key> left, BinarySearchNode<Key> right){
        super(key, left, right);
        this.left = left;
        this.right = right;
    }

    BinarySearchNode<Key> getLeft() { return this.left; }
    BinarySearchNode<Key> getRight() { return this.right; }
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

//    @Override
//    public String toString() {
//        /// @todo add Value when available.
//        /// @todo add references to children keys.
//        return this.getClass().toString() +
//                "[" +
//                "Key=" +
//                key +
//                "]";
//    }
}
