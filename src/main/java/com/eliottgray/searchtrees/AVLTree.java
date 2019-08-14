package com.eliottgray.searchtrees;

import java.util.Comparator;

/**
 * Why use Binary Search Trees over, say, HashTables?
 *
 * 1) Range queries.  Given a tree, find all elements between range X, Y.
 *      a) variations on this for multiple dimensions, e.g. R-Tree, K-DTree, etc.
 * 2) Space Complexity.  Given that each new data point creates a separate object, more memory that is necessary will not be allocated.
 * 3) Easy ordering.  Traversal of all elements in-order results in an ordered list; not something typically done with Hash Tables.
 * */
public class AVLTree<Key extends Comparable<Key>> extends BinarySearchTree<Key>{

    /**
     * Empty tree. Comparison of Keys to be performed with default compareTo method.
     */
    public AVLTree(){
        super();
    }

    /**
     * Empty tree, with comparator override.
     * @param comparator    Comparison function with which to override default compareTo of Key.
     */
    public AVLTree(Comparator<Key> comparator){
        super(comparator);
    }

    /**
     * Construct a new tree from an older tree.
     * @param root          Existing root node.
     * @param comparator    Comparator corresponding to current root node.
     */
    AVLTree(BinarySearchNode<Key> root, Comparator<Key> comparator){
        super(root, comparator);
    }


    @Override
    public AVLTree<Key> delete(Key key){
        if (root == null){
            return this;
        } else {
            BinarySearchNode<Key> newRoot = recursiveDelete(key, root);
            return new AVLTree<>(newRoot, comparator);
        }
    }

    private BinarySearchNode<Key> recursiveDelete(Key key, BinarySearchNode<Key> current){
        BinarySearchNode<Key> root;
        int comparison = comparator.compare(key, current.key);
        if (comparison < 0) {
            if (current.left != null) {
                BinarySearchNode<Key> newLeft = recursiveDelete(key, current.left);
                root = new BinarySearchNode<>(current.key, newLeft, current.right);

                // Rotate if necessary, replacing this node as the head of this tree.
                root = rotateLeftIfUnbalanced(root);
            } else {
                // Key is not in this tree; no need for change.
                root = current;
            }
        } else if (comparison > 0){
            if (current.right != null){
                BinarySearchNode<Key> newRight = recursiveDelete(key, current.right);
                root = new BinarySearchNode<>(current.key, current.left, newRight);

                // Rotate if necessary, replacing this node as the head of this tree.
                root = rotateRightIfUnbalanced(root);
            } else {
                // Key is not in this tree; no need for change.
                root = current;
            }
        } else {
            // Found key!  Now to delete. (delete = return left child, right child, find a replacement from further down, or null;
            if (current.hasLeft() && current.hasRight()){
                // Two children!  Find a replacement for this node from the longer subtree, which itself will have 1 or no children.
                Key replacementKey = findDeletionReplacement(current);

                // Delete replacement child from this node's subtree, preparing it to take over for this node.
                root = recursiveDelete(replacementKey, current);

                // Replace this with copy of replacement child.
                root = new BinarySearchNode<>(replacementKey, root.left, root.right);

            } else {
                if (current.hasLeft()){
                    root = current.left;
                } else if (current.hasRight()){
                    root = current.right;
                } else {
                    root = null;
                }
            }
        }
        return root;
    }

    @Override
    public AVLTree<Key> insert(Key key){
        if (root == null){
            BinarySearchNode<Key> newRoot = new BinarySearchNode<>(key);
            return new AVLTree<>(newRoot, comparator);
        } else {
            BinarySearchNode<Key> newRoot = recursiveInsert(key, root);
            return new AVLTree<>(newRoot, comparator);
        }
    }

    private BinarySearchNode<Key> recursiveInsert(Key key, BinarySearchNode<Key> current){
        // This position in the tree is currently occupied by current node.
        BinarySearchNode<Key> root;
        int comparison = comparator.compare(key, current.key);
        // If key is to left of current:
        if (comparison < 0){
            if (current.left != null) {
                // Insert down left subtree, contains new left subtree, and attach here.
                BinarySearchNode<Key> newLeft = recursiveInsert(key, current.left);
                root = new BinarySearchNode<>(current.key, newLeft, current.right);

                // Rotate if necessary, replacing this node as the head of this tree.
                root = rotateRightIfUnbalanced(root);
            } else {
                // I have no left, so I simply set it here.
                BinarySearchNode<Key> newLeft = new BinarySearchNode<>(key);
                root= new BinarySearchNode<>(current.key, newLeft, current.right);
            }

            // If key is to right of current:
        } else if (comparison > 0){
            // Insert down right subtree, contains new subtree head, and attach here.
            if (current.right != null){
                BinarySearchNode<Key> newRight = recursiveInsert(key, current.right);
                root = new BinarySearchNode<>(current.key, current.left, newRight);

                // Rotate if necessary, replacing this node as the head of this tree.
                root = rotateLeftIfUnbalanced(root);
            } else {
                // I have no right, so I simply set it here.
                BinarySearchNode<Key> newRight = new BinarySearchNode<>(key);
                root = new BinarySearchNode<>(current.key, current.left, newRight);
            }
        } else {
            // Duplicate key found; replace this.
            root = new BinarySearchNode<>(key, current.left, current.right);
        }

        // Return whatever occupies this position of the tree, which may still be me, or not.
        return root;
    }


    private BinarySearchNode<Key> rotateRightIfUnbalanced(BinarySearchNode<Key> root){
        if (root.getBalanceFactor() < -1){
            // Tree is unbalanced, so rotate right.

            // If left subtree is larger on the right, left subtree must be rotated left before this node rotates right.
            assert root.left != null;
            if (root.left.getBalanceFactor() > 0){
                BinarySearchNode<Key> oldLeft = root.left;
                BinarySearchNode<Key> newLeft = rotateLeft(oldLeft);
                root = new BinarySearchNode<>(root.key, newLeft, root.right);
            }

            root = rotateRight(root);
        }
        return root;
    }

    private BinarySearchNode<Key> rotateLeftIfUnbalanced(BinarySearchNode<Key> root){
        if (root.getBalanceFactor() > 1){
            // Tree is unbalanced, so rotate left.

            // If right subtree is larger on the left, right subtree must be rotated right before this node rotates left.
            assert root.right != null;
            if (root.right.getBalanceFactor() < 0){
                BinarySearchNode<Key> oldRight = root.right;
                BinarySearchNode<Key> newRight = rotateRight(oldRight);
                root = new BinarySearchNode<>(root.key, root.left, newRight);
            }

            root = rotateLeft(root);
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
    private BinarySearchNode<Key> rotateLeft(BinarySearchNode<Key> current){
        // Pivot is to my right.
        BinarySearchNode<Key> pivot = current.right;
        assert pivot != null;

        // Move self down and left.  My right is now pivot left.
        BinarySearchNode<Key> newThis = new BinarySearchNode<>(current.key, current.left, pivot.left);

        // Move pivot up and return.  I am now the new pivot's left.
        return new BinarySearchNode<>(pivot.key, newThis, pivot.right);
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
    private BinarySearchNode<Key> rotateRight(BinarySearchNode<Key> current){
        // Pivot is to my left.
        BinarySearchNode<Key> pivot = current.left;
        assert pivot != null;

        // Move self down and right.  My left is now pivot right.
        BinarySearchNode<Key> newThis = new BinarySearchNode<>(current.key, pivot.right, current.right);

        // Move pivot up and return.  I am now the new pivot's right.
        return new BinarySearchNode<>(pivot.key, pivot.left, newThis);
    }

//
//    public void validate() throws InvalidSearchTreeException {
//        if (root != null){
//            recursiveValidate(root);
//        }
//    }
//
//    private void recursiveValidate(AVLNode<Key> current) throws InvalidSearchTreeException{
//        // Validate left subtree.
//        if (current.hasLeft()){
//            AVLNode<Key> left = current.left;
//            if (left.getKey().compareTo(current.getKey()) >= 0){
//                throw new InvalidSearchTreeException(String.format("Invalid left key for key %s, left key %s", current.getKey().toString(), left.getKey().toString()));
//            }
//            recursiveValidate(left);
//        }
//
//        // Validate right subtree.
//        if (current.hasRight()){
//            AVLNode<Key> right = current.right;
//            if (right.getKey().compareTo(current.getKey()) <= 0) {
//                throw new InvalidSearchTreeException(String.format("Invalid right key for key %s, right key %s", current.getKey().toString(), right.getKey().toString()));
//            }
//            recursiveValidate(right);
//        }
//    }
}
