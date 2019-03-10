package com.eliottgray.searchtrees.binarytree;

class UnitTestUtilities {

    static <Key extends Comparable<Key>> boolean validateAVLTree(AVLTree<Key> avlTree){
        if (avlTree.isEmpty()){
            return true;
        } else {
            AVLNode<Key> root = avlTree.getRoot();
            return validateAVLNode(root);
        }
    }


    static <Key extends Comparable<Key>> boolean validateAVLNode(AVLNode<Key> node){

        if (node == null){
            return true;
        }

        // Validate left subtree.
        if (node.hasLeft()){
            validateAVLNode(node.left);
        }

        // Validate right subtree.
        if (node.hasRight()){
            validateAVLNode(node.right);
        }

        // Validate size.
        int leftSize = node.hasLeft() ? node.left.size : 0;
        int rightSize = node.hasRight() ? node.right.size : 0;
        if ((leftSize + rightSize + 1) != node.size){
            throw new IllegalStateException(String.format("Invalid size for key %s, size %d, left size %d, right size %d", node.key.toString(), node.size, leftSize, rightSize));
        }

        // Validate height.
        int leftHeight = node.hasLeft() ? node.left.height : 0;
        int rightHeight = node.hasRight() ? node.right.height : 0;
        int maxSubtreeHeight = leftHeight >= rightHeight ? leftHeight : rightHeight;
        int expectedHeight = maxSubtreeHeight + 1;
        if (expectedHeight != node.height){
            throw new IllegalStateException(String.format("Invalid height for key %s, height %d, left height %d, right height %d", node.key.toString(), node.height, leftHeight, rightHeight));
        }

        // Validate relative order.
        if (node.hasLeft() && node.left.key.compareTo(node.key) >= 0){
            throw new IllegalStateException(String.format("Invalid left key for key %s, left key %s", node.key.toString(), node.left.key.toString()));

        }
        if (node.hasRight() && node.right.key.compareTo(node.key) <= 0){
            throw new IllegalStateException(String.format("Invalid right key for key %s, right key %s", node.key.toString(), node.right.key.toString()));
        }
        return true;
    }

}
