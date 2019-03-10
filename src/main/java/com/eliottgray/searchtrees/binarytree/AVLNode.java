package com.eliottgray.searchtrees.binarytree;

import java.util.ArrayList;
import java.util.List;

class AVLNode <Key extends Comparable<Key>, Value> {

    private final Key key;
    private final Value value;
    private int height;
    private int size;
    private AVLNode<Key, Value> left;
    private AVLNode<Key, Value> right;

    AVLNode (Key key, Value value){
        this.key = key;
        this.value = value;
        this.height = 1;
        this.size = 1;
    }

    Key getKey(){ return key; }
    Value getValue(){ return value; }
    int getHeight(){ return this.height; }
    int getSize(){ return this.size; }
    AVLNode<Key, Value> getLeft(){ return this.left; }
    AVLNode<Key, Value> getRight(){ return this.right; }

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
    AVLNode<Key, Value> insert(AVLNode<Key, Value> newAVLNode){
        // This position in the tree is currently occupied by me.
        AVLNode<Key, Value> root = this;   /// @todo is it less/more efficient to just return in the various blocks?

        // If key is to left of current:
        if (newAVLNode.key.compareTo(this.key) < 0){

            if (this.hasLeft()){
                // Insert down left subtree, retrieve new left subtree, and attach here.
                this.setLeftAndRecalculate(this.left.insert(newAVLNode));
                // Rotate if necessary, replacing this node as the head of this tree.
                root = rotateRightIfUnbalanced();
            } else {
                // I have no left, so I simply set it here.
                this.setLeftAndRecalculate(newAVLNode);
            }

        // If key is to right of current:
        } else if (newAVLNode.key.compareTo(this.key) > 0){
            // Insert down right subtree, retrieve new subtree head, and attach here.
            if (this.hasRight()){
                this.setRightAndRecalculate(this.right.insert(newAVLNode));
                // Rotate if necessary, replacing this node as the head of this tree.
                root = rotateLeftIfUnbalanced();
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
    Value retrieve(Key key){
        AVLNode<Key, Value> current = this;
        Value result = null;
        boolean complete = false;
        while(!complete){
            if (current.key.compareTo(key) == 0){
                result = current.value;
                complete = true;
            } else if (current.hasLeft() && (key.compareTo(current.key)) < 0){
                current = current.left;
            } else if (current.hasRight() && (key.compareTo(current.key)) > 0){
                current = current.right;
            } else {
                complete = true;
            }
        }
        return result;
    }

    List<Value> getRange(Key start, Key end){
        List<Value> result = new ArrayList<>();
        return this.getRange(start, end, result);
    }

    private List<Value> getRange(Key start, Key end, List<Value> result){
        boolean isLessThan = start.compareTo(this.key) <= 0;
        boolean isGreaterThan = end.compareTo(this.key) >= 0;
        if (isLessThan && this.hasLeft()){
            result = left.getRange(start, end, result);
        }
        if (isLessThan && isGreaterThan){
            result.add(this.value);
        }
        if (isGreaterThan && this.hasRight()){
            result = right.getRange(start, end, result);
        }
        return result;
    }

    List<Value> inOrderTraversal(){
        List<Value> result = new ArrayList<>(this.size);
        return this.inOrderTraversal(result);
    }

    private List<Value> inOrderTraversal(List<Value> result){
        if (this.hasLeft()){
            result = left.inOrderTraversal(result);
        }
        result.add(this.value);
        if (this.hasRight()){
            result = right.inOrderTraversal(result);
        }
        return result;
    }

    List<Value> outOrderTraversal(){
        List<Value> result = new ArrayList<>(this.size);
        return this.outOrderTraversal(result);
    }

    private List<Value> outOrderTraversal(List<Value> result){
        if (this.hasRight()){
            result = right.outOrderTraversal(result);
        }
        result.add(this.value);
        if (this.hasLeft()){
            result = left.outOrderTraversal(result);
        }
        return result;
    }

    private void setLeftAndRecalculate(AVLNode<Key, Value> left){
        this.left = left;
        recalculateHeightAndSize();
    }

    private void setRightAndRecalculate(AVLNode<Key, Value> right){
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

    private AVLNode<Key, Value> rotateRightIfUnbalanced(){

        if (this.getBalanceFactor() < -1){
            // Left side is longer, so rotate right.

            // If left subtree is larger on the right, left subtree must be rotated left before this node rotates right.
            if (this.left.getBalanceFactor() > 0){
                this.left = this.left.rotateLeft();
            }

            // Replace current node with left child, moving current down and right.
            return this.rotateRight();
        }
        return this;
    }

    private AVLNode<Key, Value> rotateLeftIfUnbalanced(){
        if (this.getBalanceFactor() > 1){
            // Right side is longer, so rotate left.

            // If right subtree is larger on the left, right subtree must be rotated right before this node rotates left.
            if (this.right.getBalanceFactor() < 0){
                this.right = this.right.rotateRight();
            }

            // Replace current node with left child, moving current down and right.
            return this.rotateLeft();
        }
        return this;
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
    private AVLNode<Key, Value> rotateLeft(){
        // Pivot is my right.
        AVLNode<Key, Value> pivot = this.right;

        // My new right is pivot left.
        this.right = pivot.left;

        // Pivot left is now me.
        pivot.left = this;

        // Recalculate heights.
        this.recalculateHeightAndSize();  // Recalculate child first, to get correct counts.
        pivot.recalculateHeightAndSize(); // Now we recalculate replacement.

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
    private AVLNode<Key, Value> rotateRight(){
        // Pivot is my left.
        AVLNode<Key, Value> pivot = this.left;

        // My new left is pivot right.
        this.left = pivot.right;

        // Pivvot right is now me.
        pivot.right = this;

        // Recalculate heights.
        this.recalculateHeightAndSize();  // Recalculate child first, to get correct counts.
        pivot.recalculateHeightAndSize(); // Now we recalculate replacement.

        return pivot;
    }

    /**
     * Recursive deletion.
     * Given a key to delete, remove the corresponding node from the tree.
     * @param key       Key to delete.
     * @return          Root node.
     */
    AVLNode<Key, Value> delete(Key key){
        AVLNode<Key, Value> root = this;  // Return nothing if I delete myself and there is no replacement.
        if (key.compareTo(this.key) < 0) {
            if (this.hasLeft()) {
                this.setLeftAndRecalculate(this.left.delete(key));
                root = rotateLeftIfUnbalanced();
            }
        } else if (key.compareTo(this.key) > 0){
            if (this.hasRight()){
                this.setRightAndRecalculate(this.right.delete(key));
                root = rotateRightIfUnbalanced();
            }
        } else {
            // Found key!  Now to delete. (delete = return left child, right child, or null;
            if (hasLeft() && hasRight()){
                // Two children!  Find a replacement for this node from the longer subtree, which itself will have 1 or no children.
                root = this.findReplacementChild();

                // Delete replacement itself, which removes its connections to its child (if any)
                this.delete(root.key);

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
    private AVLNode<Key, Value> findReplacementChild(){
        AVLNode<Key, Value> replacement;
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
