package com.eliottgray.orchard.binarytree;

import java.util.ArrayList;
import java.util.List;

class AVLNode {

    private final int key;
    private int height;
    private int size;
    private AVLNode left;
    private AVLNode right;
    private AVLNode parent;

    AVLNode (int key){
        this.key = key;
        this.height = 0;
        this.size = 1;
    }

    int getKey(){ return key; }
    int getHeight(){ return this.height; }
    int getSize(){ return this.size; }
    AVLNode getLeft(){ return this.left; }
    AVLNode getRight(){ return this.right; }
    AVLNode getParent(){ return parent; }

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
     * @param newAVLNode       com.eliottgray.orchard.binarytree.AVLNode to insert.
     * @return              Root node of tree.
     */
    AVLNode insert(AVLNode newAVLNode){
        validateIsRoot();
        return insert(newAVLNode, this);
    }

    /**
     * Perform recursive insertion.
     * @param newAVLNode       com.eliottgray.orchard.binarytree.AVLNode to insert.
     * @param root          Root node of tree, to be overridden in case of replacement by a tree rotation.
     * @return              Root node of tree.
     */
    private AVLNode insert(AVLNode newAVLNode, AVLNode root){
        if (newAVLNode.key < this.key){
            if (this.hasLeft()){
                root = this.left.insert(newAVLNode, root);
                root = rotateRightIfUnbalanced(root);
            } else {
                this.setLeft(newAVLNode);
            }
        } else if (newAVLNode.key > this.key){
            if (this.hasRight()){
                root = this.right.insert(newAVLNode, root);
                root = rotateLeftIfUnbalanced(root);
            } else {
                this.setRight(newAVLNode);
            }
        } else {
//            System.out.println("Duplicate value found.");
            /// @todo Once Key-Value Pair paradigm is implemented, replace the Value here instead of doing nothing.
        }
        return root;
    }

    /**
     * Determine if given key is present within the tree.
     * @param key   Key to find.
     * @return      Tree contains key.
     */
    public boolean contains(int key){
        validateIsRoot();
        AVLNode current = this;
        Boolean result = null;
        while(result == null){
            if (current.key == key){
                result = true;
            } else if (current.hasLeft() && (key < current.key)){
                current = current.left;
            } else if (current.hasRight() && (key > current.key)){
                current = current.right;
            } else {
                result = false;
            }
        }
        return result;
    }

    public List<Integer> inOrderTraversal(){
        validateIsRoot();
        List<Integer> result = new ArrayList<>(this.size);
        return this.inOrderTraversal(result);
    }

    private List<Integer> inOrderTraversal(List<Integer> result){
        if (this.hasLeft()){
            result = left.inOrderTraversal(result);
        }
        result.add(this.key);
        if (this.hasRight()){
            result = right.inOrderTraversal(result);
        }
        return result;
    }

    public List<Integer> outOrderTraversal(){
        validateIsRoot();
        List<Integer> result = new ArrayList<>(this.size);
        return this.outOrderTraversal(result);
    }

    private List<Integer> outOrderTraversal(List<Integer> result){
        if (this.hasRight()){
            result = right.outOrderTraversal(result);
        }
        result.add(this.key);
        if (this.hasLeft()){
            result = left.outOrderTraversal(result);
        }
        return result;
    }

    private void setLeft(AVLNode left){
        this.left = left;
        if (left != null){
            left.setParent(this);
        }
        recalculateHeightAndSize();
    }

    private void setRight(AVLNode right){
        this.right = right;
        if (right != null){
            right.setParent(this);
        }
        recalculateHeightAndSize();
    }

    private void setParent(AVLNode parent) { this.parent = parent; }

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

    private AVLNode rotateRightIfUnbalanced(AVLNode root){
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

    private AVLNode rotateLeftIfUnbalanced(AVLNode root){
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
        AVLNode pivot = this.right;
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
        AVLNode pivot = this.left;
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
    private void supplantChildNodeWithParent(AVLNode pivotAVLNode){
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
     * @param value     Key to delete.
     * @return          Root node.
     */
    AVLNode delete(int value){
        validateIsRoot();
        return delete(value, this);
    }

    /**
     * Recursive deletion.
     * Given a key to delete, remove the corresponding node from the tree.
     * @param value     Key to delete.
     * @param root      Current root node.
     * @return          Root node.
     */
    private AVLNode delete(int value, AVLNode root){
        if (value < this.key){
            if (this.hasLeft()){
                root = this.left.delete(value, root);
                root = rotateLeftIfUnbalanced(root);
            }
        } else if (value > this.key){
            if (this.hasRight()){
                root = this.right.delete(value, root);
                root = rotateRightIfUnbalanced(root);
            }
        } else {
            // Found key!  Now to delete.
            AVLNode replacement;
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
    private AVLNode findReplacementChild(){
        AVLNode replacement;
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
            throw new IllegalStateException("Illegal method call on non-root com.eliottgray.orchard.binarytree.AVLNode.");
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
