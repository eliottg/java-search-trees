package com.eliottgray.searchtrees;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Why use Binary Search Trees over, say, HashTables?
 *
 * 1) Range queries.  Given a tree, find all elements between range X, Y.
 *      a) variations on this for multiple dimensions, e.g. R-Tree, K-DTree, etc.
 * 2) Space Complexity.  Given that each new data point creates a separate object, more memory that is necessary will not be allocated.
 * 3) Easy ordering.  Traversal of all elements in-order results in an ordered list; not something typically done with Hash Tables.
 * */
public class BinarySearchTree<Key extends Comparable<Key>> extends Tree<Key> {

    final BinarySearchNode<Key> root;

    /**
     * Empty tree. Comparison of Keys to be performed with default compareTo method.
     */
    public BinarySearchTree(){
        super();
        root = null;
    }

    /**
     * Empty tree, with comparator override.
     * @param comparator    Comparison function with which to override default compareTo of Key.
     */
    public BinarySearchTree(Comparator<Key> comparator){
        super(comparator);
        root = null;
    }

    /**
     * Construct a new tree from an older tree.
     * @param root          Existing root node.
     * @param comparator    Comparator corresponding to current root node.
     */
    BinarySearchTree(BinarySearchNode<Key> root, Comparator<Key> comparator){  /// @todo Test for null values.
        super(comparator);
        this.root = root;
    }

    protected BinarySearchNode<Key> getRoot(){ return root; }

    /**
     * Determine whether or not the given Key is contained within the tree.
     * @param key   Key to search for.
     * @return      Presence of Key in tree.
     */
    public boolean contains(Key key){
        if (isEmpty()){
            return false;
        } else {
            BinarySearchNode<Key> current = root;
            Boolean contains = null;
            while(contains == null){
                int comparison = comparator.compare(key, current.getKey());
                if (comparison == 0){
                    contains = true;
                } else if (current.hasLeft() && comparison < 0){
                    current = current.left;
                } else if (current.hasRight() && comparison > 0){
                    current = current.right;
                } else {
                    contains = false;
                }
            }
            return contains;
        }
    }

    public BinarySearchTree<Key> delete(Key key){
        if (root == null){
            return this;
        } else {
            BinarySearchNode<Key> newRoot = recursiveDelete(key, root);
            return new BinarySearchTree<>(newRoot, comparator);
        }
    }

    private BinarySearchNode<Key> recursiveDelete(Key key, BinarySearchNode<Key> current){
        BinarySearchNode<Key> root;
        int comparison = comparator.compare(key, current.key);
        if (comparison < 0) {
            if (current.left != null) {
                BinarySearchNode<Key> newLeft = recursiveDelete(key, current.left);
                root = new BinarySearchNode<>(current.key, newLeft, current.right);

            } else {
                // Key is not in this tree; no need for change.
                root = current;
            }
        } else if (comparison > 0){
            if (current.right != null){
                BinarySearchNode<Key> newRight = recursiveDelete(key, current.right);
                root = new BinarySearchNode<>(current.key, current.left, newRight);

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
    Key findDeletionReplacement(BinarySearchNode<Key> node){
        if (node.getBalanceFactor() > -1){
            return findLeftMostChildOfRightSubtree(node);
        } else {
            return findRightMostChildOfLeftSubtree(node);
        }
    }

    /**
     * @return      Key belonging to the immediate in-order successor.
     */
    private Key findLeftMostChildOfRightSubtree(BinarySearchNode<Key> node){
        BinarySearchNode<Key> child = node.right;
        while (child.hasLeft()){
            child = child.left;
        }
        return child.getKey();
    }

    /**
     * @return      Key belonging to the immediate in-order predecessor.
     */
    private Key findRightMostChildOfLeftSubtree(BinarySearchNode<Key> node){
        BinarySearchNode<Key> child = node.left;
        while (child.hasRight()){
            child = child.right;
        }
        return child.getKey();
    }

    public BinarySearchTree<Key> insert(Key key){
        if (root == null){
            BinarySearchNode<Key> newRoot = new BinarySearchNode<>(key);
            return new BinarySearchTree<>(newRoot, comparator);
        } else {
            BinarySearchNode<Key> newRoot = recursiveInsert(key, root);
            return new BinarySearchTree<>(newRoot, comparator);
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

    public List<Key> toAscendingList(){
        if (root == null) {
            return new ArrayList<>();
        } else {
            List<Key> orderedList = new ArrayList<>(root.getSize());
            return recursiveToAscendingList(root, orderedList);
        }
    }

    private List<Key> recursiveToAscendingList(BinarySearchNode<Key> current, List<Key> result){
        if (current.hasLeft()){
            result = recursiveToAscendingList(current.left, result);
        }
        result.add(current.key);
        if (current.hasRight()){
            result = recursiveToAscendingList(current.right, result);
        }
        return result;
    }

    public Key getMax(){
        if (root == null){
            return null;
        } else {
            BinarySearchNode<Key> current = root;
            while (current.hasRight()){
                current = current.right;
            }
            return current.getKey();
        }
    }

    public Key getMin(){
        if (root == null){
            return null;
        } else {
            BinarySearchNode<Key> current = root;
            while (current.hasLeft()){
                current = current.left;
            }
            return current.getKey();
        }
    }

    public List<Key> getRange(Key start, Key end){
        if (root == null){
            return new ArrayList<>();
        } else {
            return recursiveGetRange(start, end, new ArrayList<>(), root);
        }
    }

    private List<Key> recursiveGetRange(Key start, Key end, List<Key> result, BinarySearchNode<Key> current){
        boolean isLessThan = comparator.compare(start, current.getKey()) <= 0;
        boolean isGreaterThan = comparator.compare(end, current.getKey()) >= 0;
        if (isLessThan && current.hasLeft()){
            result = recursiveGetRange(start, end, result, current.left);
        }
        if (isLessThan && isGreaterThan){
            result.add(current.getKey());
        }
        if (isGreaterThan && current.hasRight()){
            result = recursiveGetRange(start, end, result, current.right);
        }
        return result;
    }

    public void validate() throws InvalidSearchTreeException {
        if (root != null){
            recursiveValidate(root);
        }
    }

    private void recursiveValidate(BinarySearchNode<Key> current) throws InvalidSearchTreeException{

        // Validate size.
        int leftSize = current.hasLeft() ? current.getLeft().getSize() : 0;
        int rightSize = current.hasRight() ? current.getRight().getSize() : 0;
        int expectedSize = leftSize + rightSize + 1;
        if (expectedSize != current.getSize()){
            throw new InvalidSearchTreeException(String.format("Invalid size for key %s, size %d, left size %d, right size %d", current.getKey().toString(), current.getSize(), leftSize, rightSize));
        }

        // Validate height.
        int leftHeight = current.hasLeft() ? current.getLeft().getHeight() : 0;
        int rightHeight = current.hasRight() ? current.getRight().getHeight() : 0;
        int expectedHeight = 1 + Math.max(leftHeight, rightHeight);
        if (expectedHeight != current.height){
            throw new InvalidSearchTreeException(String.format("Invalid height for key %s, height %d, left height %d, right height %d", current.getKey().toString(), current.getHeight(), leftHeight, rightHeight));
        }

        // Validate left subtree.
        if (current.hasLeft()){
            if (current.left.getKey().compareTo(current.getKey()) >= 0){
                throw new InvalidSearchTreeException(String.format("Invalid left key for key %s, left key %s", current.getKey().toString(), current.left.getKey().toString()));
            }
            recursiveValidate(current.left);
        }

        // Validate right subtree.
        if (current.hasRight()){
            if (current.right.getKey().compareTo(current.getKey()) <= 0) {
                throw new InvalidSearchTreeException(String.format("Invalid right key for key %s, right key %s", current.getKey().toString(), current.right.getKey().toString()));
            }
            recursiveValidate(current.right);
        }
    }
}
