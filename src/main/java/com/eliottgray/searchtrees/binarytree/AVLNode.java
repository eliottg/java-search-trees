package com.eliottgray.searchtrees.binarytree;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class AVLNode <Key extends Comparable<Key>, Value> {

    private final Key key;
    private final Value value;
    private int height;
    private int size;
    private AVLNode<Key, Value> left;
    private AVLNode<Key, Value> right;
    private AVLNode<Key, Value> parent;

    AVLNode (Key key, Value value){
        this.key = key;
        this.value = value;
        this.height = 0;
        this.size = 1;
    }

    Key getKey(){ return key; }
    Value getValue(){ return value; }
    int getHeight(){ return this.height; }
    int getSize(){ return this.size; }
    AVLNode<Key, Value> getLeft(){ return this.left; }
    AVLNode<Key, Value> getRight(){ return this.right; }
    AVLNode<Key, Value> getParent(){ return parent; }

    boolean hasLeft(){ return left != null; }
    boolean hasRight(){ return right != null; }
    boolean hasParent(){ return parent != null; }

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
    private int getLeftHeight(){
        if (hasLeft()){
            return 1 + left.height;
        } else {
            return 0;
        }
    }

    /**
     * @return  Height of right subtree.
     */
    private int getRightHeight(){
        if (hasRight()){
            return 1 + right.height;
        } else {
            return 0;
        }
    }

    /**
     * Perform recursive insertion, starting from root node.  This method must only be called on the Root node.
     * If a node other than the root is called for insertion, the root will be found, and the insertion begin there.
     * @param newAVLNode    Node to insert.
     * @return              Root node of tree.
     */
    AVLNode<Key, Value> insert(AVLNode<Key, Value> newAVLNode){
        validateIsRoot();
        return insert(newAVLNode, this);
    }

    /**
     * Perform recursive insertion.
     * @param newAVLNode    Node to insert.
     * @param root          Root node of tree, to be overridden in case of replacement by a tree rotation.
     * @return              Root node of tree.
     */
    private AVLNode<Key, Value> insert(AVLNode<Key, Value> newAVLNode, AVLNode<Key, Value> root){
        if (newAVLNode.key.compareTo(this.key) < 0){
            if (this.hasLeft()){
                root = this.left.insert(newAVLNode, root);
                root = rotateRightIfUnbalanced(root);
            } else {
                this.setLeft(newAVLNode);
            }
        } else if (newAVLNode.key.compareTo(this.key) > 0){
            if (this.hasRight()){
                root = this.right.insert(newAVLNode, root);
                root = rotateLeftIfUnbalanced(root);
            } else {
                this.setRight(newAVLNode);
            }
        } else {
            // Duplicate key found; replace with parent.
            supplantChildNodeWithParent(newAVLNode);
            newAVLNode.setLeft(this.left);
            newAVLNode.setRight(this.right);
            recalculateHeightAndSize();
            if (newAVLNode.parent == null){
                root = newAVLNode;
            }
        }
        return root;
    }

    /**
     * Retrieve the value for a given key if present within the tree; return null otherwise.
     * @param key   Key to find.
     * @return      Value for key; else null.
     */
    public Value retrieve(Key key){
        validateIsRoot();
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

    public List<Value> inOrderTraversal(){
        validateIsRoot();
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

    public List<Value> outOrderTraversal(){
        validateIsRoot();
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

    private void setLeft(AVLNode<Key, Value> left){
        this.left = left;
        if (left != null){
            left.setParent(this);
        }
        recalculateHeightAndSize();
    }

    private void setRight(AVLNode<Key, Value> right){
        this.right = right;
        if (right != null){
            right.setParent(this);
        }
        recalculateHeightAndSize();
    }

    private void setParent(AVLNode<Key, Value> parent) { this.parent = parent; }

    private void recalculateHeightAndSize(){
        int leftHeight = 0;
        int rightHeight = 0;
        int leftSize = 0;
        int rightSize = 0;
        if (hasLeft()){
            leftHeight = 1 + left.height;
            leftSize = left.size;
        }
        if (hasRight()){
            rightHeight = 1 + right.height;
            rightSize = right.size;
        }
        this.size = 1 + leftSize + rightSize;
        this.height = rightHeight > leftHeight ? rightHeight : leftHeight;

        if (parent != null){
            this.parent.recalculateHeightAndSize();
        }
    }

    private AVLNode<Key, Value> rotateRightIfUnbalanced(AVLNode<Key, Value> root){
        if (this.getBalanceFactor() < -1){
            // Left side is longer, so rotate right.

            // If left subtree is larger on the right, left subtree must be rotated left before this node rotates right.
            if (this.left.getBalanceFactor() > 0){
                this.left.rotateLeft();
            }

            // If current node is Root of tree, rotation will replace root with child; left child is now root.
            if (this.parent == null){
                root = this.left;
            }

            // Replace current node with left child, moving current down and right.
            this.rotateRight();
        }
        return root;
    }

    private AVLNode<Key, Value> rotateLeftIfUnbalanced(AVLNode<Key, Value> root){
        if (this.getBalanceFactor() > 1){
            // Right side is longer, so rotate left.

            // If right subtree is larger on the left, right subtree must be rotated right before this node rotates left.
            if (this.right.getBalanceFactor() < 0){
                this.right.rotateRight();
            }

            // If current node is Root of tree, rotation will replace root with child; right child is now root.
            if (this.parent == null){
                root = this.right;
            }

            // Replace current node with left child, moving current down and right.
            this.rotateLeft();
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
    private void rotateLeft(){
        AVLNode<Key, Value> pivot = this.right;
        supplantChildNodeWithParent(pivot);

        this.right = pivot.left;
        if (right != null){
            right.setParent(this);
        }
        pivot.setLeft(this); // causes height recalculation.
        recalculateHeightAndSize();  /// @todo This lets height be recalculated on the other leg of the pivot; This is duplicated work for higher nodes.
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
    private void rotateRight(){
        AVLNode<Key, Value> pivot = this.left;
        supplantChildNodeWithParent(pivot);

        this.left = pivot.right;
        if (left != null){
            left.setParent(this);
        }
        pivot.setRight(this); // causes height recalculation. on one leg of the pivot only.
        recalculateHeightAndSize();  /// @todo This lets height be recalculated on the other leg of the pivot; This is duplicated work for higher nodes.
    }

    /**
     * Given a current node, and a pivot node to rotate with, pivot supplants the current node with its parent.
     *
     * If the parent is null (current node has no parent) pivot now has no parent.
     *
     * @param pivotAVLNode     Pivot node to take the place of current node with its parent.
     */
    private void supplantChildNodeWithParent(AVLNode<Key, Value> pivotAVLNode){
        if (this.parent == null){
            if (pivotAVLNode != null){
                pivotAVLNode.setParent(null);
            }
        } else {
            if (this.parent.right == this){
                this.parent.setRight(pivotAVLNode);
            } else {
                assert(this.parent.left == this);
                this.parent.setLeft(pivotAVLNode);
            }
        }
    }

    /**
     * Given a key to delete, remove the corresponding node from the tree.
     * @param key       Key to delete.
     * @return          Root node.
     */
    AVLNode<Key, Value> delete(Key key){
        validateIsRoot();
        return delete(key, this);
    }

    /**
     * Recursive deletion.
     * Given a key to delete, remove the corresponding node from the tree.
     * @param key       Key to delete.
     * @param root      Current root node.
     * @return          Root node.
     */
    private AVLNode<Key, Value> delete(Key key, AVLNode<Key, Value> root){
        if (key.compareTo(this.key) < 0) {
            if (this.hasLeft()) {
                root = this.left.delete(key, root);
                root = rotateLeftIfUnbalanced(root);
            }
        } else if (key.compareTo(this.key) > 0){
            if (this.hasRight()){
                root = this.right.delete(key, root);
                root = rotateRightIfUnbalanced(root);
            }
        } else {
            // Found key!  Now to delete.
            AVLNode<Key, Value> replacement;
            if (hasLeft() && hasRight()){
                // Two children!  Find a replacement from the longer subtree.
                replacement = this.findReplacementChild();
                // Delete replacement's connections.
                root = root.delete(replacement.key);
                replacement.setParent(null);
                replacement.setLeft(this.left);
                replacement.setRight(this.right);
            } else {
                if (hasLeft()){
                    replacement = this.left;
                } else if (hasRight()){
                    replacement = this.right;
                } else {
                    replacement = null;
                }
            }
            // Handle cases where the node being deleted was Root of tree.
            if (this.parent == null){
                root = replacement;
            }
            // Replacement node, which takes the place of the node to be deleted, must be inserted into parent.
            supplantChildNodeWithParent(replacement);
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

    /**
     * Throw exception if this node is not the root node.  This is useful because certain recursive operations MUST be
     * called on root, to correctly maintain valid tree structure.
     */
    private void validateIsRoot(){
        if (this.parent != null) {
            throw new IllegalStateException("Illegal method call on non-root Node.");
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
