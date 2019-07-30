package com.eliottgray.searchtrees.binarytree;

import java.util.Comparator;

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
    private BinarySearchNode(Key key, BinarySearchNode<Key> left, BinarySearchNode<Key> right){
        super(key, left, right);
        this.left = left;
        this.right = right;
    }

    @Override
    Node<Key> getLeft() {
        return this.left;
    }

    @Override
    Node<Key> getRight() {
        return this.right;
    }

    /**
     * Perform recursive insertion.
     * @param key           Key to insert.
     * @return              Root node of tree.
     */
    @Override
    BinarySearchNode<Key> insert(Key key, Comparator<Key> comparator){
        // This position in the tree is currently occupied by current node.
        BinarySearchNode<Key> root;
        int comparison = comparator.compare(key, this.key);
        // If key is to left of current:
        if (comparison < 0){
            if (this.left != null) {
                // Insert down left subtree, contains new left subtree, and attach here.
                BinarySearchNode<Key> newLeft = this.left.insert(key, comparator);
                root = new BinarySearchNode<>(this.key, newLeft, this.right);
            } else {
                // I have no left, so I simply set it here.
                BinarySearchNode<Key> newLeft = new BinarySearchNode<>(key);
                root= new BinarySearchNode<>(this.key, newLeft, this.right);
            }

        // If key is to right of current:
        } else if (comparison > 0){
            // Insert down right subtree, contains new subtree head, and attach here.
            if (this.right != null){
                BinarySearchNode<Key> newRight = this.right.insert(key, comparator);
                root = new BinarySearchNode<>(this.key, this.left, newRight);
            } else {
                // I have no right, so I simply set it here.
                BinarySearchNode<Key> newRight = new BinarySearchNode<>(key);
                root = new BinarySearchNode<>(this.key, this.left, newRight);
            }
        } else {
            // Duplicate key found; replace this.
            root = new BinarySearchNode<>(key, this.left, this.right);
        }

        // Return whatever occupies this position of the tree, which may still be me, or not.
        return root;
    }

    /**
     * Recursive deletion.
     * Given a key to delete, remove the corresponding node from the tree.
     * @param key       Key to delete.
     * @return          Root node.
     */
    @Override
    BinarySearchNode<Key> delete(Key key, Comparator<Key> comparator){
        BinarySearchNode<Key> root;
        int comparison = comparator.compare(key, this.key);
        if (comparison < 0) {
            if (this.left != null) {
                BinarySearchNode<Key> newLeft = this.left.delete(key, comparator);
                root = new BinarySearchNode<>(this.key, newLeft, this.right);

            } else {
                // Key is not in this tree; no need for change.
                root = this;
            }
        } else if (comparison > 0){
            if (this.right != null){
                BinarySearchNode<Key> newRight = this.right.delete(key, comparator);
                root = new BinarySearchNode<>(this.key, this.left, newRight);

            } else {
                // Key is not in this tree; no need for change.
                root = this;
            }
        } else {
            // Found key!  Now to delete. (delete = return left child, right child, find a replacement from further down, or null;
            if (hasLeft() && hasRight()){
                // Two children!  Find a replacement for this node from the longer subtree, which itself will have 1 or no children.
                Key replacementKey = this.findDeletionReplacement();

                // Delete replacement child from this node's subtree, preparing it to take over for this node.
                root = this.delete(replacementKey, comparator);

                // Replace this with copy of replacement child.
                root = new BinarySearchNode<>(replacementKey, root.left, root.right);

            } else {
                if (hasLeft()){
                    root = this.left;
                } else if (hasRight()){
                    root = this.right;
                } else {
                    root = null;
                }
            }
        }
        return root;
    }

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
