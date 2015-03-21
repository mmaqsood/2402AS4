package comp2402TreeEditor;
import java.util.*;
import java.awt.*;
import java.io.*;

public class AVLTree extends BSTree implements BTreeADT{

	AVLTreeNode lastInsertedNode;
	public void insert(String dataString){
    	
    	/*A binary search tree inserts nodes based on the key value of their Data item.
    	 *The tree maintains nodes in a total ordering. Balance of the tree depends on
    	 *the randomness of the inserts. Inserting an already sorted sequence will produce
    	 *the least efficient (unbalanced) tree
    	
    	O(height)
   	    */
   	    
   	    //System.out.println("BSTree::insert(String)");
    	Data data = new Data(dataString);
    	
    	TreeNode newChildNode = new AVLTreeNode(data);
    	
    	if(isEmpty()) {
    		setRoot(newChildNode);
    		newChildNode.setLocation(getOwner().getDefaultRootLocation());
    	}
        else getRoot().insertNode(newChildNode);
    	
    	lastInsertedNode = (AVLTreeNode) newChildNode;
    	checkAndRebalance();
    	
    }
	
	private void checkAndRebalance(){
		// Decide what node we are going to be iterating starting with
		AVLTreeNode iterateeNode = lastInsertedNode;
		
		// Tell us if we should stop balancing the tree
		boolean completedBalancing = false;
		
		// No need to check for balance if this is the first node added.
		if (!iterateeNode.isRoot()){
			// While we have not completed a balance
			while (!completedBalancing){
				// Check the balance only if there is a last child,
				// indicating we have performed a traversal
				if (iterateeNode.getLastChild() != null){
					// NOTE: For now, based on the knowledge we have on rotations, we will stop
					//		 attempting to balance once we rotate ONCE as that is what we think will
					//		 fix ALL balance problems.
					
					// We attempt to balance the tree if we need to, and this function will return true if it performed 
					// a balance.
					completedBalancing = this.balanceIfNeeded(iterateeNode /*z*/, iterateeNode.getLastChild()/*y*/);
				}
				
				// We do not need to do the following (moving up) if we have reached the top, the root.
				if (!iterateeNode.isRoot()) {
					// Finally, in order to track nodes we have hit from the path to the last node added to the 
					// node that is imbalanced, we attach it to each node.
					((AVLTreeNode) iterateeNode.parent()).setLastChid(iterateeNode);
					// Move up the tree.
					iterateeNode = (AVLTreeNode) iterateeNode.parent();
				}
				else {
					// Right away, for the next iteration, we flip the flag
					// if we have reached the root, meaning we will no longer
					// further process
					completedBalancing = iterateeNode.isRoot();
				}
				
			}
		}
	}
	private boolean balanceIfNeeded(AVLTreeNode z, AVLTreeNode y){
		// Get the balance of z, the tree that we fear might be imbalanced.
		int balance = z.getBalance();
		
		// Three values determine an imbalance, 1, -1, 0.
		boolean isBalanced = balance == 1 || balance == -1 || balance == 0;
		
		// Balance the tree based on the case.
		if (!isBalanced) { return determineCaseAndRebalance(z, y); }
		
		// Return false, indicating no balance has been performed.
		return false;
	}
	
	private boolean determineCaseAndRebalance(AVLTreeNode z, AVLTreeNode y){
		
		// We have two of the nodes we need, z and y, but we need to define w and x.
		
		// w is the node we just inserted.
		AVLTreeNode w = lastInsertedNode;
		
		// x is the child of y, that was on the path from w to z.
		AVLTreeNode x = y.getLastChild();
		
		// Case 1: Left Left
		if (y.isLeftChild() && x.isLeftChild()){
			// Rotate right on z
			performRightRotation(z);
		}
		// Case 2: Left Right
		if (y.isLeftChild() & x.isRightChild()){
			// Rotate left on y
			// Rotate right on z
			doubleRight(z);
		}
		// Case 3: Right Right
		if (y.isRightChild() & x.isRightChild()){
			// Rotate left on z
			performLeftRotation(z);
		}
		// Case 4: Right Left
		if (y.isRightChild() & x.isLeftChild()){
			// Rotate right on y
			// Rotate left on z
			doubleLeft(z);
		}
		
		return true;
	}
	
	private void performRightRotation(AVLTreeNode nodeToRotate){
		// We will right rotate this root
		
		// Grab the children
		AVLTreeNode leftChild = (AVLTreeNode) nodeToRotate.leftChild();
		AVLTreeNode rightChild = (AVLTreeNode) nodeToRotate.rightChild();
		
		
		// First, if the node we are rotating is the root, we are going 
		// to update the root
		if (nodeToRotate.isRoot()) { this.setRoot(leftChild);}
		
		// Deal with the simple cases first.
		
		//1. If we have nothing on the left, we can simply attach ourselves 
		//	 as our child's left child.
		if (rightChild == null){
			// Update the leftChild's parent
			if (nodeToRotate.parent() != null){
				leftChild.setParent(nodeToRotate.parent());
				((BTreeNode) leftChild.parent()).setLeftChild(leftChild);
			}
			else {
				leftChild.setParent(null);
			}
			// Make the nodeToRotate a rightChild of leftChild
			nodeToRotate.setParent(leftChild);
			nodeToRotate.setLeftChild(null);
			leftChild.setRightChild(nodeToRotate);
		}
		else if (leftChild != null) {
			// Now we enter the more complex cases.
			
			// If this is a sub-tree, we will update the parent with new node
			// with the next node in line.
			if (!nodeToRotate.isRoot() && nodeToRotate.isRightChild()){
				((BTreeNode) nodeToRotate.parent()).setRightChild(rightChild);
			}
			else if (!nodeToRotate.isRoot() && nodeToRotate.isLeftChild()){
				((BTreeNode) nodeToRotate.parent()).setLeftChild(leftChild);
			}
			
			// We also need to switch parents and link the rightChild to the parent of the 
			// node we are rotating and vice-versa
			if (nodeToRotate.isRoot()) leftChild.setParent(null);
			else leftChild.setParent(nodeToRotate.parent());
						
			nodeToRotate.setParent(leftChild);
			if (leftChild.rightChild() != null) leftChild.rightChild().setParent(nodeToRotate);
			if (leftChild.rightChild() != null) nodeToRotate.setLeftChild((BTreeNode) leftChild.rightChild());
			// Old root will be the right child of the new root (left child)
			leftChild.setRightChild(nodeToRotate);
			
		}
	}
	
	private void performLeftRotation(AVLTreeNode nodeToRotate){
		// We will right rotate this root
		
		// Grab the children
		AVLTreeNode leftChild = (AVLTreeNode) nodeToRotate.leftChild();
		AVLTreeNode rightChild = (AVLTreeNode) nodeToRotate.rightChild();
		
		
		// First, if the node we are rotating is the root, we are going 
		// to update the root
		if (nodeToRotate.isRoot()) { this.setRoot(rightChild);}
		
		// Deal with the simple cases first.
		
		//1. If we have nothing on the left, we can simply attach ourselves 
		//	 as our child's left child.
		if (leftChild == null){
			// Update the leftChild's parent
			if (nodeToRotate.parent() != null){
				rightChild.setParent(nodeToRotate.parent());
				((BTreeNode) nodeToRotate.parent()).setRightChild(rightChild);
			}
			else {
				rightChild.setParent(null);
			}
			// Make the nodeToRotate a rightChild of leftChild
			nodeToRotate.setParent(rightChild);
			nodeToRotate.setRightChild(null);
			rightChild.setLeftChild(nodeToRotate);
		}
		else if (rightChild != null) {
			// Now we enter the more complex cases.
			
			// If this is a sub-tree, we will update the parent with new node
			// with the next node in line.
			if (!nodeToRotate.isRoot() && nodeToRotate.isRightChild()){
				((BTreeNode) nodeToRotate.parent()).setRightChild(rightChild);
			}
			else if (!nodeToRotate.isRoot() && nodeToRotate.isLeftChild()){
				((BTreeNode) nodeToRotate.parent()).setLeftChild(leftChild);
			}
			
			// We also need to switch parents and link the rightChild to the parent of the 
			// node we are rotating and vice-versa
			if (nodeToRotate.isRoot()) rightChild.setParent(null);
			else rightChild.setParent(nodeToRotate.parent());
			
			nodeToRotate.setParent(rightChild);
			if (rightChild.leftChild() != null) rightChild.leftChild().setParent(nodeToRotate);
			if (rightChild.leftChild() != null) nodeToRotate.setRightChild((BTreeNode) rightChild.leftChild());
			// Old root will be the right child of the new root (left child)
			rightChild.setLeftChild(nodeToRotate);
			
		}
	}

    private AVLTreeNode rightRotate(AVLTreeNode rightRotate) {
        AVLTreeNode temp = rightRotate;
        temp.setParent(rightRotate.getParent());
        rightRotate.setRightChild((BTreeNode) temp.leftChild());
        if (temp.leftChild() != null) {
            temp.leftChild().setParent(rightRotate);
        }

        temp.setLeftChild(rightRotate);
        rightRotate.setParent(temp);

        ((AVLTreeNode) temp.leftChild()).setHeight();
        ((AVLTreeNode) temp.rightChild()).setHeight();

        temp.setHeight();

        return temp;
    }

    private void doubleLeft(AVLTreeNode v) {
    	//performRightRotation((AVLTreeNode) v.rightChild());
        //performLeftRotation(v);
    }
    private void doubleRight(AVLTreeNode v) {
    	//performLeftRotation((AVLTreeNode) v.leftChild());
    	//performRightRotation(v);
    }
}