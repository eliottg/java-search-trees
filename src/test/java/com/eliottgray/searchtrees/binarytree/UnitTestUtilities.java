package com.eliottgray.searchtrees.binarytree;

class UnitTestUtilities {

    static boolean validateAVLTree(AVLTree avlTree){
        if (avlTree.isEmpty()){
            return true;
        } else {
            AVLNode root = avlTree.getRoot();
            return validateAVLNode(root);
        }
    }


    static boolean validateAVLNode(AVLNode node){

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
            throw new IllegalStateException(String.format("Invalid size for key %d, size %d, left size %d, right size %d", node.getKey(), node.getSize(), leftSize, rightSize));
        }

        // Validate height.
        int leftHeight = node.hasLeft() ? node.getLeft().getHeight() + 1 : 0;
        int rightHeight = node.hasRight() ? node.getRight().getHeight() + 1 : 0;
        int maxSubtreeHeight = leftHeight >= rightHeight ? leftHeight : rightHeight;
        if (maxSubtreeHeight != node.getHeight()){
            throw new IllegalStateException(String.format("Invalid height for key %d, height %d, left height %d, right height %d", node.getKey(), node.getHeight(), leftHeight, rightHeight));
        }

        // Validate relative order.
        if (node.hasLeft() && node.getLeft().getKey() >= node.getKey()){
            throw new IllegalStateException(String.format("Invalid left key for key %d, left key %d", node.getKey(), node.getLeft().getKey()));

        }
        if (node.hasRight() && node.getRight().getKey() <= node.getKey()){
            throw new IllegalStateException(String.format("Invalid right key for key %d, right key %d", node.getKey(), node.getRight().getKey()));
        }
        return true;
    }

}
