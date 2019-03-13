package com.eliottgray.searchtrees.binarytree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class BSTNode<Key extends Comparable<Key>> {

    final Key key;
    final int height;
    final int size;
    final BSTNode<Key> left;
    final BSTNode<Key> right;

    /**
     * Construct new leaf node, with no children.
     * @param key   Comparable Key for node.
     */
    BSTNode(Key key){
        this.key = key;
        this.left = null;
        this.right = null;
        this.height = 1;
        this.size = 1;
    }

    /**
     * Construct replacement root node, with existing children.
     * @param key       Comparable Key for node.
     * @param left      Existing left child.
     * @param right     Existing right child.
     */
    private BSTNode(Key key, BSTNode<Key> left, BSTNode<Key> right){
        this.key = key;
        this.left = left;
        this.right = right;

        int leftHeight = 0;
        int rightHeight = 0;
        int leftSize = 0;
        int rightSize = 0;
        if (hasLeft()){
            leftHeight = left.height;
            leftSize = left.size;
        }
        if (hasRight()){
            rightHeight = right.height;
            rightSize = right.size;
        }
        this.size = 1 + leftSize + rightSize;
        this.height = (rightHeight > leftHeight) ? (rightHeight + 1) : (leftHeight + 1);

    }

    boolean hasLeft(){ return left != null; }
    boolean hasRight(){ return right != null; }

    /**
     * Describes the relative height of the left and right subtrees.
     * If right tree is greater, balance factor is positive.
     * If left tree is greater, balance factor is negative.
     * Else, balance factor is zero.
     * @return      integer describing the balance factor.
     */
    private int getBalanceFactor(){ return (hasRight() ? right.height : 0) - (hasLeft() ? left.height : 0); }

    /**
     * Perform recursive insertion.
     * @param key           Key to insert.
     * @return              Root node of tree.
     */
    BSTNode<Key> insert(Key key, Comparator<Key> comparator){
        // This position in the tree is currently occupied by current node.
        BSTNode<Key> root;

        // If key is to left of current:
        if (comparator.compare(key, this.key) < 0){
            if (this.hasLeft()){
                // Insert down left subtree, contains new left subtree, and attach here.
                BSTNode<Key> newLeft = this.left.insert(key, comparator);
                root = new BSTNode<>(this.key, newLeft, this.right);
            } else {
                // I have no left, so I simply set it here.
                BSTNode<Key> newLeft = new BSTNode<>(key);
                root= new BSTNode<>(this.key, newLeft, this.right);
            }

        // If key is to right of current:
        } else if (comparator.compare(key, this.key) > 0){
            // Insert down right subtree, contains new subtree head, and attach here.
            if (this.hasRight()){
                BSTNode<Key> newRight = this.right.insert(key, comparator);
                root = new BSTNode<>(this.key, this.left, newRight);
            } else {
                // I have no right, so I simply set it here.
                BSTNode<Key> newRight = new BSTNode<>(key);
                root = new BSTNode<>(this.key, this.left, newRight);
            }
        } else {
            // Duplicate key found; replace this.
            root = new BSTNode<>(key, this.left, this.right);
        }

        // Return whatever occupies this position of the tree, which may still be me, or not.
        return root;
    }

    /**
     * Retrieve the value for a given key if present within the tree; return null otherwise.
     * @param key   Key to find.
     * @return      Value for key; else null.
     */
    boolean contains(Key key, Comparator<Key> comparator){
        BSTNode<Key> current = this;
        Boolean contains = null;
        while(contains == null){
            if (comparator.compare(current.key, key) == 0){
                contains = true;
            } else if (current.hasLeft() && (key.compareTo(current.key)) < 0){
                current = current.left;
            } else if (current.hasRight() && (key.compareTo(current.key)) > 0){
                current = current.right;
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
        boolean isLessThan = comparator.compare(start, this.key) <= 0;
        boolean isGreaterThan = comparator.compare(end, this.key) >= 0;
        if (isLessThan && this.hasLeft()){
            result = left.getRange(start, end, result, comparator);
        }
        if (isLessThan && isGreaterThan){
            result.add(this.key);
        }
        if (isGreaterThan && this.hasRight()){
            result = right.getRange(start, end, result, comparator);
        }
        return result;
    }

    List<Key> inOrderTraversal(){
        List<Key> result = new ArrayList<>(this.size);
        return this.inOrderTraversal(result);
    }

    private List<Key> inOrderTraversal(List<Key> result){
        if (this.hasLeft()){
            result = left.inOrderTraversal(result);
        }
        result.add(this.key);
        if (this.hasRight()){
            result = right.inOrderTraversal(result);
        }
        return result;
    }

    /**
     * Recursive deletion.
     * Given a key to delete, remove the corresponding node from the tree.
     * @param key       Key to delete.
     * @return          Root node.
     */
    BSTNode<Key> delete(Key key, Comparator<Key> comparator){
        BSTNode<Key> root;
        if (comparator.compare(key, this.key) < 0) {
            if (this.hasLeft()) {
                BSTNode<Key> newLeft = this.left.delete(key, comparator);
                root = new BSTNode<>(this.key, newLeft, this.right);

            } else {
                // Key is not in this tree; no need for change.
                root = this;
            }
        } else if (comparator.compare(key, this.key) > 0){
            if (this.hasRight()){
                BSTNode<Key> newRight = this.right.delete(key, comparator);
                root = new BSTNode<>(this.key, this.left, newRight);

            } else {
                // Key is not in this tree; no need for change.
                root = this;
            }
        } else {
            // Found key!  Now to delete. (delete = return left child, right child, find a replacement from further down, or null;
            if (hasLeft() && hasRight()){
                // Two children!  Find a replacement for this node from the longer subtree, which itself will have 1 or no children.
                Key replacementKey = this.findReplacementChild();

                // Delete replacement child from this node's subtree, preparing it to take over for this node.
                root = this.delete(replacementKey, comparator);

                // Replace this with copy of replacement child.
                root = new BSTNode<>(replacementKey, root.left, root.right);

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

    /**
     * Given that this node has two children, find the optimal child to replace the current node
     * in a delete; optimal child is either the largest child in the left subtree,
     * or the smallest child in the right subtree.
     *
     * Which is chosen, right or left, depends on which subtree is higher; picking the higher subtree
     * eliminates the need to rotate the tree after deletion.
     *
     * @return      Node to replace the current node in a deletion.
     */
    private Key findReplacementChild(){
        BSTNode<Key> replacement;
        if (getBalanceFactor() > -1){
            // Right subtree is longer or tree is equal.
            replacement = this.right;
            while (replacement.hasLeft()){
                replacement = replacement.left;
            }
        } else {
            // Left subtree is longer.
            replacement = this.left;
            while (replacement.hasRight()){
                replacement = replacement.right;
            }
        }
        return replacement.key;
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
