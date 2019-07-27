package com.eliottgray.searchtrees.binarytree;

import java.util.Comparator;

class AVLNode <Key extends Comparable<Key>> extends Node<Key>{

    /**
     * Construct new leaf node, with no children.
     * @param key   Comparable Key for node.
     */
    AVLNode (Key key){
        super(key);
    }

    /**
     * Construct replacement root node, with existing children.
     * @param key       Comparable Key for node.
     * @param left      Existing left child.
     * @param right     Existing right child.
     */
    private AVLNode(Key key, Node<Key> left, Node<Key> right){
        super(key, left, right);
    }

    /**
     * Perform recursive insertion.
     * @param key           Key to insert.
     * @return              Root node of tree.
     */
    AVLNode<Key> insert(Key key, Comparator<Key> comparator){
        // This position in the tree is currently occupied by current node.
        AVLNode<Key> root;
        int comparison = comparator.compare(key, this.key);
        // If key is to left of current:
        if (comparison < 0){
            if (this.hasLeft()){
                // Insert down left subtree, contains new left subtree, and attach here.
                Node<Key> newLeft = this.left.insert(key, comparator);
                root = new AVLNode<>(this.key, newLeft, this.right);

                // Rotate if necessary, replacing this node as the head of this tree.
                root = root.rotateRightIfUnbalanced();
            } else {
                // I have no left, so I simply set it here.
                AVLNode<Key> newLeft = new AVLNode<>(key);
                root= new AVLNode<>(this.key, newLeft, this.right);
            }

        // If key is to right of current:
        } else if (comparison > 0){
            // Insert down right subtree, contains new subtree head, and attach here.
            if (this.hasRight()){
                Node<Key> newRight = this.right.insert(key, comparator);
                root = new AVLNode<>(this.key, this.left, newRight);

                // Rotate if necessary, replacing this node as the head of this tree.
                root = root.rotateLeftIfUnbalanced();
            } else {
                // I have no right, so I simply set it here.
                AVLNode<Key> newRight = new AVLNode<>(key);
                root = new AVLNode<>(this.key, this.left, newRight);
            }
        } else {
            // Duplicate key found; replace this.
            root = new AVLNode<>(key, this.left, this.right);
        }

        // Return whatever occupies this position of the tree, which may still be me, or not.
        return root;
    }

    private AVLNode<Key> rotateRightIfUnbalanced(){
        // This position in the tree is currently occupied by current node.
        AVLNode<Key> root = this;

        if (root.getBalanceFactor() < -1){
            // Tree is unbalanced, so rotate right.

            // If left subtree is larger on the right, left subtree must be rotated left before this node rotates right.
            if (root.left.getBalanceFactor() > 0){
                AVLNode<Key> oldLeft = (AVLNode<Key>)root.left;  /// @todo Avoid having to cast this.  Rotatae method can belong in abstract class?
                AVLNode<Key> newLeft = oldLeft.rotateLeft();
                root = new AVLNode<>(root.key, newLeft, root.right);
            }

            root = root.rotateRight();
        }
        return root;
    }

    private AVLNode<Key> rotateLeftIfUnbalanced(){
        // This position in the tree is currently occupied by me.
        AVLNode<Key> root = this;

        if (root.getBalanceFactor() > 1){
            // Tree is unbalanced, so rotate left.

            // If right subtree is larger on the left, right subtree must be rotated right before this node rotates left.
            if (root.right.getBalanceFactor() < 0){
                AVLNode<Key> oldRight = (AVLNode<Key>)root.right;  /// @todo Avoid having to cast this.  Rotatae method can belong in abstract class?
                AVLNode<Key> newRight = oldRight.rotateRight();
                root = new AVLNode<>(root.key, root.left, newRight);
            }

            root = root.rotateLeft();
        }
        return root;
    }


    /**
     *               Left Rotation
     *
     *            30                       30
     *           /                        /
     *        [10]   <----Root--->     (15)
     *       /   \                     /  \
     *      5    (15)               [10]   20
     *    /  \   /  \               /  \
     *   2   7  12  20            5    12
     *                           / \
     *                          2   7
     */
    private AVLNode<Key> rotateLeft(){
        // Pivot is to my right.
        Node<Key> pivot = this.right;

        // Move self down and left.  My right is now pivot left.
        AVLNode<Key> newThis = new AVLNode<>(this.key, this.left, pivot.left);

        // Move pivot up and return.  I am now the new pivot's left.
        return new AVLNode<>(pivot.key, newThis, pivot.right);
    }

    /**
     *               Right Rotation
     *
     *            30                       30
     *           /                        /
     *        [10]   <----Root--->      (5)
     *       /   \                     /  \
     *     (5)    15                  2   [10]
     *    /  \   /  \                     /  \
     *   2   7  12  20                   7    15
     *                                       /  \
     *                                     12    20
     */
    private AVLNode<Key> rotateRight(){
        // Pivot is to my left.
        Node<Key> pivot = this.left;

        // Move self down and right.  My left is now pivot right.
        AVLNode<Key> newThis = new AVLNode<>(this.key, pivot.right, this.right);

        // Move pivot up and return.  I am now the new pivot's right.
        return new AVLNode<>(pivot.key, pivot.left, newThis);
    }

    /**
     * Recursive deletion.
     * Given a key to delete, remove the corresponding node from the tree.
     * @param key       Key to delete.
     * @return          Root node.
     */
    AVLNode<Key> delete(Key key, Comparator<Key> comparator){
        AVLNode<Key> root;
        int comparison = comparator.compare(key, this.key);
        if (comparison < 0) {
            if (this.hasLeft()) {
                Node<Key> newLeft = this.left.delete(key, comparator);
                root = new AVLNode<>(this.key, newLeft, this.right);

                root = root.rotateLeftIfUnbalanced();
            } else {
                // Key is not in this tree; no need for change.
                root = this;
            }
        } else if (comparison > 0){
            if (this.hasRight()){
                Node<Key> newRight = this.right.delete(key, comparator);
                root = new AVLNode<>(this.key, this.left, newRight);

                root = root.rotateRightIfUnbalanced();
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
                root = new AVLNode<>(replacementKey, root.left, root.right);

            } else {
                if (hasLeft()){
                    root = (AVLNode<Key>)this.left;  /// @todo Avoid having to cast this, and the following, new roots from left/right.
                } else if (hasRight()){
                    root = (AVLNode<Key>)this.right;
                } else {
                    root = null;
                }
            }
        }
        return root;
    }

    @Override
    void validate() throws InvalidSearchTreeException{
        super.validate();

        // Validate size.
        int leftSize = hasLeft() ? getLeft().getSize() : 0;
        int rightSize = hasRight() ? getRight().getSize() : 0;
        if ((leftSize + rightSize + 1) != getSize()){
            throw new InvalidSearchTreeException(String.format("Invalid size for key %s, size %d, left size %d, right size %d", getKey().toString(), getSize(), leftSize, rightSize));
        }

        // Validate height.
        int leftHeight = hasLeft() ? getLeft().getHeight() : 0;
        int rightHeight = hasRight() ? getRight().getHeight() : 0;
        int maxSubtreeHeight = Math.max(leftHeight, rightHeight);
        int expectedHeight = maxSubtreeHeight + 1;
        if (expectedHeight != height){
            throw new InvalidSearchTreeException(String.format("Invalid height for key %s, height %d, left height %d, right height %d", getKey().toString(), getHeight(), leftHeight, rightHeight));
        }
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
