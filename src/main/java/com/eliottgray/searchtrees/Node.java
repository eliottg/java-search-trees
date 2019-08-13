package com.eliottgray.searchtrees;


abstract class Node <Key extends Comparable<Key>> {

    final Key key;
    final int height;
    final int size;

    /**
     * Construct a childless Node.
     * @param key   Key for Node.
     */
    Node (Key key){
        this.key = key;
        this.height = 1;
        this.size = 1;
    }

    /**
     * Construct a Node with at least one child.
     * @param key       Key for node.
     * @param left      Left child.
     * @param right     Right child.
     */
    Node(Key key, Node<Key> left, Node<Key> right){
        this.key = key;

        int leftHeight = 0;
        int rightHeight = 0;
        int leftSize = 0;
        int rightSize = 0;
        if (left != null){
            leftHeight = left.height;
            leftSize = left.size;
        }
        if (right != null){
            rightHeight = right.height;
            rightSize = right.size;
        }
        this.size = 1 + leftSize + rightSize;
        this.height = (rightHeight > leftHeight) ? (rightHeight + 1) : (leftHeight + 1);
    }

    int getHeight(){ return height; }
    int getSize(){ return size; }
    Key getKey(){ return key; }
}
