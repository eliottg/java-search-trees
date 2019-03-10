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
            validateAVLNode(node.getLeft());
        }

        // Validate right subtree.
        if (node.hasRight()){
            validateAVLNode(node.getRight());
        }

        // Validate size.
        int leftSize = node.hasLeft() ? node.getLeft().getSize() : 0;
        int rightSize = node.hasRight() ? node.getRight().getSize() : 0;
        if ((leftSize + rightSize + 1) != node.getSize()){
            throw new IllegalStateException(String.format("Invalid size for key %s, size %d, left size %d, right size %d", node.getKey().toString(), node.getSize(), leftSize, rightSize));
        }

        // Validate height.
        int leftHeight = node.hasLeft() ? node.getLeft().getHeight() : 0;
        int rightHeight = node.hasRight() ? node.getRight().getHeight() : 0;
        int maxSubtreeHeight = leftHeight >= rightHeight ? leftHeight : rightHeight;
        int expectedHeight = maxSubtreeHeight + 1;
        if (expectedHeight != node.getHeight()){
            throw new IllegalStateException(String.format("Invalid height for key %s, height %d, left height %d, right height %d", node.getKey().toString(), node.getHeight(), leftHeight, rightHeight));
        }

        // Validate relative order.
        if (node.hasLeft() && node.getLeft().getKey().compareTo(node.getKey()) >= 0){
            throw new IllegalStateException(String.format("Invalid left key for key %s, left key %s", node.getKey().toString(), node.getLeft().getKey().toString()));

        }
        if (node.hasRight() && node.getRight().getKey().compareTo(node.getKey()) <= 0){
            throw new IllegalStateException(String.format("Invalid right key for key %s, right key %s", node.getKey().toString(), node.getRight().getKey().toString()));
        }
        return true;
    }

}
