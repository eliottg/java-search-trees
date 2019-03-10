package com.eliottgray.searchtrees.binarytree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class AVLNode <Key extends Comparable<Key>> {

    private final Key key;
    private int height;
    private int size;
    private AVLNode<Key> left;
    private AVLNode<Key> right;

    AVLNode (Key key){
        this.key = key;
        this.height = 1;
        this.size = 1;
    }

    Key getKey(){ return key; }
    int getHeight(){ return this.height; }
    int getSize(){ return this.size; }
    AVLNode<Key> getLeft(){ return this.left; }
    AVLNode<Key> getRight(){ return this.right; }

    boolean hasLeft(){ return left != null; }
    boolean hasRight(){ return right != null; }

    /**
     * Describes the relative height of the left and right subtrees.
     * If right tree is greater, balance factor is positive.
     * If left tree is greater, balance factor is negative.
     * Else, balance factor is zero.
     * @return      integer describing the balance factor.
     */
    int getBalanceFactor(){ return getRightHeight() - getLeftHeight(); }

    /**
     * @return  Height of left subtree.
     */
    private int getLeftHeight(){ return hasLeft() ? left.height : 0; }

    /**
     * @return  Height of right subtree.
     */
    private int getRightHeight(){ return hasRight() ? right.height : 0; }

    /**
     * Perform recursive insertion.
     * @param newAVLNode    Node to insert.
     * @return              Root node of tree.
     */
    AVLNode<Key> insert(AVLNode<Key> newAVLNode, Comparator<Key> comparator){
        // This position in the tree is currently occupied by current node.
        AVLNode<Key> root = this;

        // If key is to left of current:
        if (comparator.compare(newAVLNode.key, this.key) < 0){
            if (this.hasLeft()){
                // Insert down left subtree, contains new left subtree, and attach here.
                this.setLeftAndRecalculate(this.left.insert(newAVLNode, comparator));
                // Rotate if necessary, replacing this node as the head of this tree.
                root = this.rotateRightIfUnbalanced();
            } else {
                // I have no left, so I simply set it here.
                this.setLeftAndRecalculate(newAVLNode);
            }

        // If key is to right of current:
        } else if (comparator.compare(newAVLNode.key, this.key) > 0){
            // Insert down right subtree, contains new subtree head, and attach here.
            if (this.hasRight()){
                this.setRightAndRecalculate(this.right.insert(newAVLNode, comparator));
                // Rotate if necessary, replacing this node as the head of this tree.
                root = this.rotateLeftIfUnbalanced();
            } else {
                // I have no right, so I simply set it here.
                this.setRightAndRecalculate(newAVLNode);
            }
        } else {
            // Duplicate key found; replace with parent.
            newAVLNode.left = this.left;
            newAVLNode.right = this.right;
            newAVLNode.recalculateHeightAndSize();
            root = newAVLNode;
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
        AVLNode<Key> current = this;
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

    List<Key> outOrderTraversal(){
        List<Key> result = new ArrayList<>(this.size);
        return this.outOrderTraversal(result);
    }

    private List<Key> outOrderTraversal(List<Key> result){
        if (this.hasRight()){
            result = right.outOrderTraversal(result);
        }
        result.add(this.key);
        if (this.hasLeft()){
            result = left.outOrderTraversal(result);
        }
        return result;
    }

    private void setLeftAndRecalculate(AVLNode<Key> left){
        this.left = left;
        recalculateHeightAndSize();
    }

    private void setRightAndRecalculate(AVLNode<Key> right){
        this.right = right;
        recalculateHeightAndSize();
    }

    private void recalculateHeightAndSize(){
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
        this.height = rightHeight > leftHeight ? rightHeight : leftHeight;
        this.height += 1;

    }

    private AVLNode<Key> rotateRightIfUnbalanced(){
        // This position in the tree is currently occupied by current node.
        AVLNode<Key> root = this;

        if (this.getBalanceFactor() < -1){
            // Tree is unbalanced, so rotate right.

            // If left subtree is larger on the right, left subtree must be rotated left before this node rotates right.
            if (this.left.getBalanceFactor() > 0){
                this.left = this.left.rotateLeft();
            }

            // Replace current node with left child, moving current down and right.
            root = this.rotateRight();
        }
        return root;
    }

    private AVLNode<Key> rotateLeftIfUnbalanced(){
        // This position in the tree is currently occupied by me.
        AVLNode<Key> root = this;

        if (this.getBalanceFactor() > 1){
            // Tree is unbalanced, so rotate left.

            // If right subtree is larger on the left, right subtree must be rotated right before this node rotates left.
            if (this.right.getBalanceFactor() < 0){
                this.right = this.right.rotateRight();
            }

            // Replace current node with left child, moving current down and right.
            root = this.rotateLeft();
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
        // Rotate self with pivot.
        AVLNode<Key> pivot = this.right;
        this.right = pivot.left;
        pivot.left = this;

        // Recalculate heights.
        this.recalculateHeightAndSize();  // Recalculate child first, to contains correct counts for pivot..
        pivot.recalculateHeightAndSize();

        return pivot;
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
        // Rotate self with pivot.
        AVLNode<Key> pivot = this.left;
        this.left = pivot.right;
        pivot.right = this;

        // Recalculate heights.
        this.recalculateHeightAndSize();  // Recalculate child first, to contains correct counts for pivot..
        pivot.recalculateHeightAndSize();

        return pivot;
    }

    /**
     * Recursive deletion.
     * Given a key to delete, remove the corresponding node from the tree.
     * @param key       Key to delete.
     * @return          Root node.
     */
    AVLNode<Key> delete(Key key, Comparator<Key> comparator){
        AVLNode<Key> root = this;
        if (comparator.compare(key, this.key) < 0) {
            if (this.hasLeft()) {
                this.setLeftAndRecalculate(this.left.delete(key, comparator));
                root = rotateLeftIfUnbalanced();
            }
        } else if (comparator.compare(key, this.key) > 0){
            if (this.hasRight()){
                this.setRightAndRecalculate(this.right.delete(key, comparator));
                root = rotateRightIfUnbalanced();
            }
        } else {
            // Found key!  Now to delete. (delete = return left child, right child, or null;
            if (hasLeft() && hasRight()){
                // Two children!  Find a replacement for this node from the longer subtree, which itself will have 1 or no children.
                root = this.findReplacementChild();

                // Delete replacement itself, which removes its connections to its child (if any)
                this.delete(root.key, comparator);

                // Delete replacement's connections.
                root.left = this.left;
                root.right = this.right;
                root.recalculateHeightAndSize();
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
    private AVLNode<Key> findReplacementChild(){
        AVLNode<Key> replacement;
        if (getBalanceFactor() > -1){
            // Right subtree is longer or tree is equal.
            replacement = this.right;
            while (replacement.hasLeft()){
                replacement = replacement.getLeft();
            }
        } else {
            // Left subtree is longer.
            assert(getBalanceFactor() == -1);
            replacement = this.left;
            while (replacement.hasRight()){
                replacement = replacement.getRight();
            }
        }
        return replacement;
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
